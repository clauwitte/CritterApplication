package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.data.Schedule;
import com.udacity.jdnd.course3.critter.exceptions.CustomerNotFoundException;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ScheduleService {
    private static final Logger log =  LoggerFactory.getLogger(ScheduleService.class);

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    public ScheduleService (ScheduleRepository scheduleRepository, PetRepository petRepository, CustomerRepository customerRepository, EmployeeRepository employeeRepository){
        this.scheduleRepository = scheduleRepository;
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
    }

    public Schedule save(Schedule schedule){
        log.info("ScheduleService Schedule save is being called");
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> findAllSchedules(){
        log.info("ScheduleService Schedule findAllSchedules is being called");
        return scheduleRepository.findAll();
    }

    public List<Schedule> findScheduleByEmployeeIds(Long employeeId){
        log.info("ScheduleService Schedule findScheduleByEmployeeIds is being called");
        log.info("looking for schedule for employee with id {}", employeeId);
        return scheduleRepository.findScheduleByEmployeeId(employeeId);
    }

    public List<Schedule> findScheduleByPetId(Long petIds){
        log.info("ScheduleService Schedule findScheduleByPetId is being called");
        log.info("looking for schedule for pet with id {}", petIds);
        return scheduleRepository.findScheduleByPetId(petIds);
    }

    public List<Schedule> findScheduleByCustomerId(Long id){
        Customer customer = customerRepository.findById(id).orElseThrow(CustomerNotFoundException::new);
        List<Pet> pets = customer.getPet();
        List<Schedule> schedules = new ArrayList<>();
        for(Pet pet : pets){
            schedules.addAll(scheduleRepository.findScheduleByPetId(pet.getId()));
        }
        List<Schedule> schedules1 = schedules;
        log.info("Schedules for customer with id {} has been found", id);
        return schedules1;
    }
}
