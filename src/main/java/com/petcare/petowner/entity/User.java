package com.petcare.petowner.entity;

import jakarta.persistence.*;
import java.util.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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

    @ManyToOne
    private Address address;

    @ManyToMany(mappedBy = "owners", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Pet> pets = new HashSet<>();

    private boolean deceased;
}

