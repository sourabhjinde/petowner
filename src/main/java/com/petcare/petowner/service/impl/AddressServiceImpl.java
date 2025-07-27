package com.petcare.petowner.service.impl;

import com.petcare.petowner.dto.AddressRequestDTO;
import com.petcare.petowner.dto.AddressResponseDTO;
import com.petcare.petowner.mapper.AddressMapper;
import com.petcare.petowner.repository.AddressRepository;
import com.petcare.petowner.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    public AddressResponseDTO createAddress(AddressRequestDTO addressRequestDTO) {
        var addressEntity = addressMapper.toEntity(addressRequestDTO);
        var addressResponseEntity = addressRepository.save(addressEntity);
        return addressMapper.toDto(addressResponseEntity);
    }

    @Override
    public List<AddressResponseDTO> getAllAddresses() {
        var addressList = addressRepository.findAll();
        return addressMapper.toDtoList(addressList);
    }
}

