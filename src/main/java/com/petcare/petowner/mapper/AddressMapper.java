package com.petcare.petowner.mapper;

import com.petcare.petowner.dto.AddressRequestDTO;
import com.petcare.petowner.dto.AddressResponseDTO;
import com.petcare.petowner.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addressHash", ignore = true)
    @Mapping(target = "normalizedAddressKey", ignore = true)
    Address toEntity(AddressRequestDTO addressRequestDTO);
    AddressResponseDTO toDto(Address address);
    List<AddressResponseDTO> toDtoList(List<Address> addressList);
}

