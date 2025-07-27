package com.petcare.petowner.dto;

import java.util.List;

public record UserResponseDTO(
        Long id,
        String name,
        String firstName,
        Integer age,
        String gender,
        AddressResponseDTO address,
        List<PetShortDTO> pets
) {}
