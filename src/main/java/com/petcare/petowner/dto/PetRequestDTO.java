package com.petcare.petowner.dto;

import com.petcare.petowner.entity.PetType;
import jakarta.validation.constraints.*;

import java.util.Set;

public record PetRequestDTO(
        @NotBlank String name,
        @NotNull @Min(0) Integer age,
        @NotNull PetType type,
        @NotNull Set<Long> ownerIds
) {}

