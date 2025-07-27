package com.petcare.petowner.service;

import com.petcare.petowner.dto.AddressRequestDTO;
import com.petcare.petowner.dto.AddressResponseDTO;

import java.util.List;

public interface AddressService {
    AddressResponseDTO createAddress(AddressRequestDTO address);
    List<AddressResponseDTO> getAllAddresses();
}

