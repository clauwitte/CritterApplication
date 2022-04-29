package com.udacity.jdnd.course3.critter.data;

import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="pet")
@Inheritance(strategy = InheritanceType.JOINED)
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Nationalized
    private String name;

    private LocalDate birthDate;

    private String notes;


    @Enumerated(EnumType.STRING)
    private PetType type;

    //ownerId at PetDTO
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Customer.class)
    @JoinColumn(name = "customer_id")
    private Customer ownerId;


    public Pet(){}
    public Pet(Long id, String name, LocalDate birthDate, String notes){
        this.id=id;
        this.name=name;
        this.birthDate=birthDate;
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public Customer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Customer ownerId) {
        this.ownerId = ownerId;
    }
}
