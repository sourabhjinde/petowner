package com.petcare.petowner.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressRequestDTO (
    @NotBlank(message = "City is required")
    String city,

    @NotBlank(message = "Type is required")
    String type,

    @NotBlank(message = "Address name is required")
    String addressName,

    @NotBlank(message = "Number is required")
    String number
){}
