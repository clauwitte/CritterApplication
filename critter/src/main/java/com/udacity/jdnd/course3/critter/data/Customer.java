package com.udacity.jdnd.course3.critter.data;

import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="customer")
public class Customer {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    Long id;

    @Nationalized
    String name;

    private String phoneNumber;

    private String notes;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "ownerId",
            cascade = CascadeType.ALL,
            targetEntity = Pet.class
            )
    private List<Pet> pet;


    public Customer(){

    }
   public Customer(Long id, String name, String phoneNumber, String notes){
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.notes=notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Pet> getPet() {
        return pet;
    }

    public void setPet(List<Pet> pet) {
        this.pet = pet;
    }
}
