package com.petcare.petowner.service;

import com.petcare.petowner.dto.AddressRequestDTO;
import com.petcare.petowner.dto.AddressResponseDTO;
import com.petcare.petowner.entity.Address;
import com.petcare.petowner.mapper.AddressMapper;
import com.petcare.petowner.repository.AddressRepository;
import com.petcare.petowner.service.impl.AddressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AddressServiceImplTest {

    private AddressRepository addressRepository;
    private AddressMapper addressMapper;
    private AddressServiceImpl addressService;

    @BeforeEach
    void setUp() {
        addressRepository = mock(AddressRepository.class);
        addressMapper = mock(AddressMapper.class);
        addressService = new AddressServiceImpl(addressRepository, addressMapper);
    }

    @Test
    void createAddress_shouldReturnMappedResponseDTO() {
        AddressRequestDTO requestDTO = new AddressRequestDTO("Line1", "City", "State", "123456");
        Address addressEntity = new Address();
        Address savedEntity = new Address();
        AddressResponseDTO responseDTO = new AddressResponseDTO(1L, "Line1", "City", "State", "123456");

        when(addressMapper.toEntity(requestDTO)).thenReturn(addressEntity);
        when(addressRepository.save(addressEntity)).thenReturn(savedEntity);
        when(addressMapper.toDto(savedEntity)).thenReturn(responseDTO);

        AddressResponseDTO result = addressService.createAddress(requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO, result);
        verify(addressMapper).toEntity(requestDTO);
        verify(addressRepository).save(addressEntity);
        verify(addressMapper).toDto(savedEntity);
    }

    @Test
    void getAllAddresses_shouldReturnMappedList() {
        // Arrange
        List<Address> entityList = List.of(new Address(), new Address());
        List<AddressResponseDTO> responseDTOList = List.of(
                new AddressResponseDTO(1L, "L1", "C", "S", "123"),
                new AddressResponseDTO(2L, "L2", "C2", "S2", "456")
        );

        when(addressRepository.findAll()).thenReturn(entityList);
        when(addressMapper.toDtoList(entityList)).thenReturn(responseDTOList);

        // Act
        List<AddressResponseDTO> result = addressService.getAllAddresses();

        // Assert
        assertEquals(2, result.size());
        assertEquals(responseDTOList, result);
        verify(addressRepository).findAll();
        verify(addressMapper).toDtoList(entityList);
    }

    @Test
    void getAllAddresses_shouldReturnEmptyListWhenNoData() {
        when(addressRepository.findAll()).thenReturn(Collections.emptyList());
        when(addressMapper.toDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<AddressResponseDTO> result = addressService.getAllAddresses();

        assertTrue(result.isEmpty());
        verify(addressRepository).findAll();
        verify(addressMapper).toDtoList(Collections.emptyList());
    }
}

