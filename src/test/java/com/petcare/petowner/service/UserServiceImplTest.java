package com.petcare.petowner.service;

import com.petcare.petowner.dto.AddressRequestDTO;
import com.petcare.petowner.dto.UserRequestDTO;
import com.petcare.petowner.dto.UserResponseDTO;
import com.petcare.petowner.entity.Address;
import com.petcare.petowner.entity.Gender;
import com.petcare.petowner.entity.PetType;
import com.petcare.petowner.entity.User;
import com.petcare.petowner.exception.NotFoundException;
import com.petcare.petowner.exception.ResourceNotFoundException;
import com.petcare.petowner.exception.Sha256Exception;
import com.petcare.petowner.mapper.UserMapper;
import com.petcare.petowner.repository.AddressRepository;
import com.petcare.petowner.repository.UserRepository;
import com.petcare.petowner.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private Address address;
    private UserRequestDTO userRequestDTO;
    private User user;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setup() {
        AddressRequestDTO addressRequestDTO = new AddressRequestDTO("Mumbai", "Residential", "MG Road", "101");

        address = new Address();
        address.setId(1L);
        address.setCity("Mumbai");
        address.setType("Residential");
        address.setAddressName("MG Road");
        address.setNumber("101");
        address.setAddressHash("hashed123");

        userRequestDTO = new UserRequestDTO("Jinde", "Sourabh", 32, Gender.MALE, addressRequestDTO);

        user = new User();
        user.setId(1L);
        user.setName("Jinde");
        user.setFirstName("Sourabh");
        user.setGender(Gender.MALE);
        user.setAge(32);
        user.setAddress(address);

        userResponseDTO = new UserResponseDTO(1L, "Jinde", "Sourabh", 32, "MALE", null);
    }

    @Test
    void testCreateUser_WhenAddressExists() throws Sha256Exception {
        when(addressRepository.findByAddressHash(anyString())).thenReturn(Optional.of(address));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.createUser(userRequestDTO);

        assertEquals("Jinde", result.name());
        verify(addressRepository, never()).save(any(Address.class));
    }

    @Test
    void testCreateUser_WhenAddressDoesNotExist() throws Sha256Exception {
        when(addressRepository.findByAddressHash(anyString())).thenReturn(Optional.empty());
        when(addressRepository.save(any())).thenReturn(address);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.createUser(userRequestDTO);

        assertEquals("Sourabh", result.firstName());
        verify(addressRepository).save(any(Address.class));
    }

    @Test
    void testUpdateUser_Success() throws Sha256Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(addressRepository.findByAddressHash(anyString())).thenReturn(Optional.of(address));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.updateUser(1L, userRequestDTO);

        assertEquals("Jinde", result.name());
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateUser_UserNotFound_ShouldThrow() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(99L, userRequestDTO));

        assertEquals("User not found with id: 99", ex.getMessage());
    }

    @Test
    void testGetUsersByNameAndFirstName() {
        when(userRepository.findByNameIgnoreCaseAndFirstNameIgnoreCase("Jinde", "Sourabh"))
                .thenReturn(List.of(user));
        when(userMapper.toDto(user)).thenReturn(userResponseDTO);

        List<UserResponseDTO> result = userService.getUsersByNameAndFirstName("Jinde", "Sourabh");

        assertEquals(1, result.size());
        assertEquals("Sourabh", result.getFirst().firstName());
    }

    @Test
    void testGetUsersByPetTypeAndCity() {
        when(userRepository.findUsersByPetTypeAndCity(PetType.DOG, "Mumbai")).thenReturn(List.of(user));
        when(userMapper.toDto(user)).thenReturn(userResponseDTO);

        List<UserResponseDTO> result = userService.getUsersByPetTypeAndCity("Mumbai", "Dog");

        assertEquals(1, result.size());
        assertEquals("Jinde", result.getFirst().name());
    }

    @Test
    void testMarkUserAsDeceased_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.markUserAsDeceased(1L);

        assertTrue(user.isDeceased());
        verify(userRepository).save(user);
    }

    @Test
    void testMarkUserAsDeceased_UserNotFound() {
        when(userRepository.findById(100L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userService.markUserAsDeceased(100L));

        assertEquals("User not found with ID: 100", ex.getMessage());
    }
}
