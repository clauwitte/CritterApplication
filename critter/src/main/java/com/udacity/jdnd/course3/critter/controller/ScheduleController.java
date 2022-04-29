package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.data.EmployeeSkill;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.data.Schedule;
import com.udacity.jdnd.course3.critter.dto.ScheduleDTO;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    private static final Logger log =  LoggerFactory.getLogger(ScheduleController.class);

    @Autowired
    PetService petService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    CustomerService customerService;

    @Autowired
    EmployeeService employeeService;

    public ScheduleController (PetService petService, ScheduleService scheduleService, CustomerService customerService, EmployeeService employeeService){
        this.petService = petService;
        this.scheduleService = scheduleService;
        this.customerService = customerService;
        this.employeeService = employeeService;
    }

    private static ScheduleDTO convertEntityToScheduleDTO(Schedule schedule){
        log.info("Converting ScheduleEntity to ScheduleDTO");
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        Long scheduleId = schedule.getId();
        scheduleDTO.setId(scheduleId);
        LocalDate scheduleDate = schedule.getDate();
        scheduleDTO.setDate(scheduleDate);
        log.info("ScheduleDate has been added to DTO");
        Set<EmployeeSkill> scheduleActivities = schedule.getActivities();
        scheduleDTO.setActivities(scheduleActivities);

        List<Long> employeesIds = schedule.getEmployee().stream().map(Employee::getId).collect(Collectors.toList());
        scheduleDTO.setEmployeeIds(employeesIds);
        log.info("employeesIds have been added to DTO");
        List<Long> petsIds = schedule.getPet().stream().map(Pet::getId).collect(Collectors.toList());
        scheduleDTO.setPetIds(petsIds);
        log.info("petsIds have been added to DTO");
        log.info("ScheduleEntity to ScheduleDTO has been converted");
        return scheduleDTO;
    }

    private Schedule convertScheduleDTOtoEntity(ScheduleDTO scheduleDTO){
        log.info("Converting ScheduleDTO to ScheduleEntity");
        Schedule schedule = new Schedule();

        schedule.setDate(scheduleDTO.getDate());
        schedule.setActivities(scheduleDTO.getActivities());

        List<Long> employeeIds = scheduleDTO.getEmployeeIds();
        List<Employee> employees = employeeService.getAllEmployees(employeeIds);
        schedule.setEmployee(employees);

        List<Long> petIds = scheduleDTO.getPetIds();
        List<Pet> pets = petService.findAllPetsByIds(petIds);
        schedule.setPet(pets);
        log.info("ScheduleDTO to ScheduleEntity succeeded");
        return schedule;

    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        log.info("Creating schedule");
        Schedule newSchedule = scheduleService.save(convertScheduleDTOtoEntity(scheduleDTO));
        scheduleDTO.setId(newSchedule.getId());
        log.info("Schedule created");
        return scheduleDTO;
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        log.info("Calling for all schedules");
        List<Schedule> entitySchedule = scheduleService.findAllSchedules();
        List<ScheduleDTO> scheduleDTO = new ArrayList<>();
       for(int i=0;i< entitySchedule.size();i++) {
           scheduleDTO.add(convertEntityToScheduleDTO(entitySchedule.get(i)));
       }
       log.info("All schedules has been retrieved");
       return scheduleDTO;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        log.info("Calling for schedules for pet with id {}", petId);
        List<Schedule> newSchedule = scheduleService.findScheduleByPetId(petId);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for(int i = 0; i<newSchedule.size(); i++){
            scheduleDTOS.add(convertEntityToScheduleDTO(newSchedule.get(i)));
        }
        log.info("Schedules found for pet with id {}", petId);
        return scheduleDTOS;
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        log.info("Calling for schedules for employee with id {}", employeeId);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        List<Schedule> newSchedule = scheduleService.findScheduleByEmployeeIds(employeeId);
        for(int i = 0; i<newSchedule.size();i++){
            scheduleDTOS.add(convertEntityToScheduleDTO(newSchedule.get(i)));
        }
        log.info("Schedules found for employee with id {}", employeeId);
        return scheduleDTOS;
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        log.info("Calling for Schedules for customer with id {}", customerId);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        List<Schedule> newSchedule = scheduleService.findScheduleByCustomerId(customerId);
        for(int i = 0; i<newSchedule.size();i++){
            scheduleDTOS.add(convertEntityToScheduleDTO(newSchedule.get(i)));
        }
        log.info("Schedules found for customer with id {}", customerId);
        return scheduleDTOS;
    }
}
