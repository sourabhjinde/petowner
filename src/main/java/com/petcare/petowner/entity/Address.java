package com.petcare.petowner.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private String type;          // road, street, avenue
    private String addressName;
    private String number;

    @Column(unique = true, length = 64)
    private String addressHash;

    @Column(name = "address_key")
    private String normalizedAddressKey;
}
