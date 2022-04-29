package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.dto.CustomerDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.exceptions.PetWithoutOwnerException;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger log =  LoggerFactory.getLogger(ScheduleController.class);
    @Autowired
    EmployeeService employeeService;

    @Autowired
    PetService petService;

    @Autowired
    CustomerService customerService;

    public UserController(EmployeeService employeeService, PetService petService, CustomerService customerService){
        this.employeeService=employeeService;
        this.petService=petService;
        this.customerService=customerService;
    }

    private static CustomerDTO convertEntityToCustomerDTO(Customer customer){
        log.info("Converting entity to CustomerDTO");
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        List<Pet> pets = customer.getPet();
        if (pets != null) {
            List<Long> petIds = new ArrayList<>();
            for (int i = 0; i<pets.size();i++){
                long petId = pets.get(i).getId();
                petIds.add(petId);
            }
            customerDTO.setPetIds(petIds);
        }
        log.info("Entity has been converted to CustomerDTO!");
        return customerDTO;
    }

    private Customer convertCustomerDTOToEntity(CustomerDTO customerDTO) {
        log.info("Converting CustomerDTO into Entity");
        ModelMapper modelMapper = new ModelMapper();
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        List<Long> petIds = customerDTO.getPetIds();
        if (petIds != null) {
            List<Pet> pet = new ArrayList<>();
            for (long i = 0l; i< pet.size();i++) {
                pet.add(petService.findPetById(i));
            }
            customer.setPet(pet);
        }
        log.info("CustomerDTO converted into Entity!");
        return customer;
    }

    private static EmployeeDTO convertEntityToEmployeeDTO(Employee employee){
        log.info("Converting Entity to EmployeeDTO");
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        employeeDTO.setId(employee.getId());
        log.info("Entity has been converted into EmployeeDTO");
        return employeeDTO;
    }

    private Employee convertEmployeeDTOToEntity(EmployeeDTO employeeDTO){
        log.info("Converting EmployeeDTO into Entity");
        ModelMapper modelMapper = new ModelMapper();
        Employee employee = modelMapper.map(employeeDTO, Employee.class);
        if(employeeDTO.getDaysAvailable()!=null){
            employee.setDaysAvailable(employeeDTO.getDaysAvailable());
        }
        log.info("EmployeeDTO has been converted into Entity!");
        return employee;
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        log.info("Saving customer");
        List<Long> petIds = customerDTO.getPetIds();
        List<Pet> pets = new ArrayList<>();
        if (petIds != null) {
            for (long i = 0l; i< pets.size();i++) {
                pets.add(petService.findPetById(i));
            }
        }
        Customer customer= convertCustomerDTOToEntity(customerDTO);
        customer.setPet(pets);
        Customer savedCustomer = customerService.save(customer);
        log.info("Customer has been saved");
        return convertEntityToCustomerDTO(savedCustomer);
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
            List<Customer> newCostumer = customerService.getAllCustomers();
            List<CustomerDTO> customerDTO= new ArrayList<>();
            for(int i=0;i< newCostumer.size();i++){
                customerDTO.add(convertEntityToCustomerDTO(newCostumer.get(i)));
            }
            return customerDTO;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Pet pet = petService.findPetById(petId);
        Customer customer = pet.getOwnerId();
        if(customer != null){
            return convertEntityToCustomerDTO(customer);
        } else {
            throw new PetWithoutOwnerException();
        }
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("Saving employee");
        Employee employee = convertEmployeeDTOToEntity(employeeDTO);
        Employee employeeSaved = employeeService.save(employee);
        log.info("Employee has been saved");
       return convertEntityToEmployeeDTO(employeeSaved);

    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        log.info("Getting employee with id {}", employeeId);
        Employee employee = employeeService.findEmployeeById(employeeId);
        log.info("employee with id {} has been found", employeeId);
        return convertEntityToEmployeeDTO(employee);
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> day, @PathVariable long employeeId) {
        log.info("Setting availability for employee with id {}", employeeId);
        log.info("looking for employee with id {}", employeeId);
        Employee employee = employeeService.findEmployeeById(employeeId);
        employee.setDaysAvailable(day);
        log.info("Setting availability");
        log.info("Saving employee");
        employeeService.save(employee);

    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employees = employeeService.getEmployeesAvailable(employeeDTO.getSkills(), employeeDTO.getDate().getDayOfWeek());
        List<EmployeeDTO> employeesDTO = new ArrayList<>();
        for(int i = 0; i<employees.size();i++){
            employeesDTO.add(convertEntityToEmployeeDTO(employees.get(i)));
        }
        return employeesDTO;
    }
}
