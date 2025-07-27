package com.petcare.petowner.dto;

public record UserResponseDTO(
        Long id,
        String name,
        String firstName,
        Integer age,
        String gender,
        AddressResponseDTO address
) {}
