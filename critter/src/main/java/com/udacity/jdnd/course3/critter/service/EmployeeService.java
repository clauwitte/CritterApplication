package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.data.EmployeeSkill;
import com.udacity.jdnd.course3.critter.exceptions.EmployeeNotFoundException;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class EmployeeService {
    private static final Logger log =  LoggerFactory.getLogger(EmployeeService.class);
    @Autowired
    EmployeeRepository employeeRepository;

    public EmployeeService (EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    public Employee save(Employee employee){
        return employeeRepository.save(employee);
    }

    public Employee findEmployeeById(Long id){
        return employeeRepository.findById(id).orElseThrow(EmployeeNotFoundException::new);
    }

    public List<Employee> getAllEmployees(List<Long> id) {
        return employeeRepository.findAllById(id);
    }

    public List<Employee> getEmployeesAvailable(Set<EmployeeSkill> skills, DayOfWeek daysAvailable) {
        log.info("getting employees available");
        List<Employee> employeesAvailableByDay = employeeRepository.findAllEmployeesAvailableByDaysAvailable(daysAvailable);
        if(employeesAvailableByDay == null){
            log.error("no employees available");
        }
        List<Employee> availableEmployees = new ArrayList<>();
        for (Employee employee : employeesAvailableByDay) {
            if (employee.getSkills().containsAll(skills)) {
                availableEmployees.add(employee);
            }
        }
        log.info("Available employees found!");
        return availableEmployees;
    }
}
