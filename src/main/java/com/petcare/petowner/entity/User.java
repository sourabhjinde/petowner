package com.petcare.petowner.entity;

import jakarta.persistence.*;
import java.util.*;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String firstName;
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne(cascade = CascadeType.ALL)
    private Address address;

    @ManyToMany(mappedBy = "owners", fetch = FetchType.LAZY)
    private Set<Pet> pets = new HashSet<>();

    private boolean deceased;

    // Getters, setters, equals, hashCode, toString
}

