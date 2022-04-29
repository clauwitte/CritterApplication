package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.exceptions.CustomerNotFoundException;
import com.udacity.jdnd.course3.critter.exceptions.PetNotFoundException;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PetService {
    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerRepository customerRepository;

    public PetService(PetRepository petRepository, CustomerRepository customerRepository){
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }

    public Pet save(Pet pet){
        return petRepository.save(pet);
    }

    public Pet findPetById(Long id){
        return petRepository.findById(id).orElseThrow(PetNotFoundException::new);
    }

    public List<Pet> findAllPets(){
        return petRepository.findAll();
    }

    public List<Pet> findAllPetsByIds(List<Long> petIds) {
        return petRepository.findAllById(petIds);
    }

    public List<Pet> findPetByOwnerId(Long id){
        Customer customer = customerRepository.findById(id).orElseThrow(CustomerNotFoundException::new);
        List<Pet> pets = customer.getPet();
        return pets;
    }


}
