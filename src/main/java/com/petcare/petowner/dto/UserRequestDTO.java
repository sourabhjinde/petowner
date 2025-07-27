package com.petcare.petowner.dto;

import com.petcare.petowner.entity.Gender;
import jakarta.validation.constraints.*;

public record UserRequestDTO(
        @NotBlank String name,
        @NotBlank String firstName,
        @NotNull @Min(0) Integer age,
        @NotNull Gender gender,
        @NotNull AddressRequestDTO address
) {}

