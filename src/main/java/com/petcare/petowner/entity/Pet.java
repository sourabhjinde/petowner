package com.petcare.petowner.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer age;

    @Enumerated(EnumType.STRING)
    private PetType type;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "pet_owner",
            joinColumns = @JoinColumn(name = "pet_id"),
            inverseJoinColumns = @JoinColumn(name = "owner_id")
    )
    @JsonBackReference
    private Set<User> owners = new HashSet<>();

    private boolean deceased;

    // Getters, setters, equals, hashCode
}
