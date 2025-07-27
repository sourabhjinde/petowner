package com.petcare.petowner.dto;

public record AddressResponseDTO (
     Long id,
     String city,
     String type,
     String addressName,
     String number
){}