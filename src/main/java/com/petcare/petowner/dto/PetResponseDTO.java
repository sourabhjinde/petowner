package com.petcare.petowner.dto;

public record PetResponseDTO(
        Long id,
        String name,
        Integer age,
        String type
) {}

