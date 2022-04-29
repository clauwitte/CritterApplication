package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.dto.PetDTO;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    PetService petService;

    @Autowired
    CustomerService customerService;

    public PetController(PetService petService, CustomerService customerService){
        this.petService = petService;
        this.customerService = customerService;
    }

    private static PetDTO convertEntityToPetDTO(Pet pet){
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        //matching the customerId with the ownerId for visualization.
        //Translate DTO's!!!
        if (pet.getOwnerId() != null) {
            petDTO.setOwnerId(pet.getOwnerId().getId());
        }
        return petDTO;
    }

    private Pet convertPetDTOToEntity(PetDTO petDTO){
        ModelMapper modelMapper = new ModelMapper();
        Pet pet = modelMapper.map(petDTO, Pet.class);
        return pet;
    }


    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = convertPetDTOToEntity(petDTO);
        Pet petSaved = petService.save(pet);
        Customer customer =null;
        if(petDTO.getOwnerId() != 0) {
            customer = customerService.getCustomerById(petDTO.getOwnerId());
            customer.getPet().add(petSaved);
        }

        Long petId= petSaved.getId();
        customerService.save(customer);
        petDTO.setId(petId);
        return petDTO;
    }
    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = petService.findPetById(petId);
        return convertEntityToPetDTO(pet);
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<PetDTO> petDTO = new ArrayList<>();
        List<Pet> savedPets = petService.findAllPets();
        for(int i=0;i< savedPets.size();i++){
            petDTO.add(convertEntityToPetDTO(savedPets.get(i)));
        }
        return petDTO;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> wantedPet = petService.findPetByOwnerId(ownerId);
        List<PetDTO> petDTO = new ArrayList<>();
        for(int i=0;i< wantedPet.size();i++){
            petDTO.add(convertEntityToPetDTO(wantedPet.get(i)));
        }
        return petDTO;
    }
}
