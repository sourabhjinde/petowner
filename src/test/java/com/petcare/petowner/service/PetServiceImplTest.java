package com.petcare.petowner.service;

import com.petcare.petowner.dto.PetRequestDTO;
import com.petcare.petowner.dto.PetResponseDTO;
import com.petcare.petowner.entity.Address;
import com.petcare.petowner.entity.Pet;
import com.petcare.petowner.entity.PetType;
import com.petcare.petowner.entity.User;
import com.petcare.petowner.exception.NotFoundException;
import com.petcare.petowner.mapper.PetMapper;
import com.petcare.petowner.repository.PetRepository;
import com.petcare.petowner.repository.UserRepository;
import com.petcare.petowner.service.impl.PetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetServiceImplTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PetMapper petMapper;

    @InjectMocks
    private PetServiceImpl petService;

    private PetRequestDTO requestDTO;
    private Pet pet;
    private PetResponseDTO responseDTO;
    private User user;

    @BeforeEach
    void setUp() {
        Address address = new Address();
        address.setId(1L);
        address.setCity("TestCity");

        user = new User();
        user.setId(1L);
        user.setAddress(address);

        requestDTO = new PetRequestDTO("Buddy", 3, PetType.DOG, Set.of(1L));

        pet = new Pet();
        pet.setId(10L);
        pet.setName("Buddy");
        pet.setAge(3);
        pet.setType(PetType.DOG);
        pet.setOwners(Set.of(user));

        responseDTO = new PetResponseDTO(10L, "Buddy", 3, "DOG");
    }

    @Test
    void testCreatePet_Success() {
        when(petMapper.toEntity(requestDTO)).thenReturn(pet);
        when(userRepository.findAllById(Set.of(1L))).thenReturn(List.of(user));
        when(petRepository.save(pet)).thenReturn(pet);
        when(petMapper.toDto(pet)).thenReturn(responseDTO);

        PetResponseDTO result = petService.createPet(requestDTO, Set.of(1L));

        assertEquals("Buddy", result.name());
        verify(petRepository).save(pet);
    }

    @Test
    void testCreatePet_DifferentAddresses_ShouldThrowException() {
        Set<Long> ownerIds = Set.of(1L);
        Address anotherAddress = new Address();
        anotherAddress.setId(2L);

        User anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setAddress(anotherAddress);

        when(petMapper.toEntity(requestDTO)).thenReturn(pet);
        when(userRepository.findAllById(Set.of(1L))).thenReturn(List.of(user, anotherUser));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> petService.createPet(requestDTO, ownerIds));

        assertEquals("All owners must share the same address to own the same pet.", ex.getMessage());
    }

    @Test
    void testUpdatePet_Success() {
        when(userRepository.findAllById(requestDTO.ownerIds())).thenReturn(List.of(user));
        when(petRepository.findById(10L)).thenReturn(Optional.of(pet));
        when(petRepository.save(any())).thenReturn(pet);
        when(petMapper.toDto(any())).thenReturn(responseDTO);

        PetResponseDTO result = petService.updatePet(10L, requestDTO);

        assertEquals("Buddy", result.name());
    }

    @Test
    void testUpdatePet_NotFound() {
        when(userRepository.findAllById(requestDTO.ownerIds())).thenReturn(List.of(user));
        when(petRepository.findById(10L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> petService.updatePet(10L, requestDTO));

        assertEquals("Pet not found", ex.getMessage());
    }

    @Test
    void testMarkPetAsDeceased() {
        when(petRepository.findById(10L)).thenReturn(Optional.of(pet));

        petService.markPetAsDeceased(10L);

        assertTrue(pet.isDeceased());
        verify(petRepository).save(pet);
    }

    @Test
    void testGetPetsByUserId() {
        when(petRepository.findPetsByUserId(1L)).thenReturn(List.of(pet));
        when(petMapper.toDtoList(List.of(pet))).thenReturn(List.of(responseDTO));

        List<PetResponseDTO> result = petService.getPetsByUserId(1L);

        assertEquals(1, result.size());
        assertEquals("Buddy", result.getFirst().name());
    }

    @Test
    void testFindPetsByFemaleOwnersInCity() {
        when(petRepository.findPetsByFemaleOwnersInCity("TestCity")).thenReturn(List.of(pet));
        when(petMapper.toDto(pet)).thenReturn(responseDTO);

        List<PetResponseDTO> result = petService.findPetsByFemaleOwnersInCity("TestCity");

        assertEquals(1, result.size());
    }

    @Test
    void testGetPetsByUser() {
        user.setPets(Set.of(pet));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        List<PetResponseDTO> result = petService.getPetsByUser(1L);

        assertEquals(1, result.size());
        assertEquals("Buddy", result.getFirst().name());
    }

    @Test
    void testGetPetsByUser_UserNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> petService.getPetsByUser(2L));

        assertEquals("User not found with ID: 2", ex.getMessage());
    }




}

