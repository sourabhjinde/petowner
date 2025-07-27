package com.petcare.petowner.service.impl;

import com.petcare.petowner.dto.PetRequestDTO;
import com.petcare.petowner.dto.PetResponseDTO;
import com.petcare.petowner.entity.*;
import com.petcare.petowner.exception.NotFoundException;
import com.petcare.petowner.mapper.PetMapper;
import com.petcare.petowner.repository.PetRepository;
import com.petcare.petowner.repository.UserRepository;
import com.petcare.petowner.service.PetService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final PetMapper petMapper;

    @Override
    public PetResponseDTO createPet(PetRequestDTO petRequestDTO, Set<Long> ownerIds) {
        Pet pet = petMapper.toEntity(petRequestDTO);
        Set<User> owners = new HashSet<>(userRepository.findAllById(ownerIds));

        Set<Address> distinctAddresses = owners.stream()
                .map(User::getAddress)
                .collect(Collectors.toSet());

        if (distinctAddresses.size() > 1) {
            throw new IllegalArgumentException("All owners must share the same address to own the same pet.");
        }

        pet.setOwners(owners);
        var petEntity = petRepository.save(pet);
        return petMapper.toDto(petEntity);
    }

    @Override
    public PetResponseDTO updatePet(Long id, PetRequestDTO updatedPetDto) {
        Set<User> owners = new HashSet<>(userRepository.findAllById(updatedPetDto.ownerIds()));

        Set<Address> distinctAddresses = owners.stream()
                .map(User::getAddress)
                .collect(Collectors.toSet());

        if (distinctAddresses.size() > 1) {
            throw new IllegalArgumentException("All owners must share the same address to own the same pet.");
        }

        return petRepository.findById(id).map(pet -> {
            pet.setName(updatedPetDto.name());
            pet.setAge(updatedPetDto.age());
            pet.setType(updatedPetDto.type());
            pet.setOwners(owners);
            var petEntity = petRepository.save(pet);
            return petMapper.toDto(petEntity);
        }).orElseThrow(() -> new RuntimeException("Pet not found"));
    }

    @Override
    public void markPetAsDeceased(Long petId) {
        petRepository.findById(petId).ifPresent(pet -> {
            pet.setDeceased(true);
            petRepository.save(pet);
        });
    }

    @Override
    public List<PetResponseDTO> getPetsByUserId(Long userId) {
        var petEntityList = petRepository.findPetsByUserId(userId);
        return petMapper.toDtoList(petEntityList);
    }

    @Override
    public List<PetResponseDTO> getPetsByCity(String city) {
        var petEntityList = petRepository.findPetsByCity(city);
        return petMapper.toDtoList(petEntityList);
    }

    @Override
    public List<PetResponseDTO> findPetsByFemaleOwnersInCity(String city) {
        List<Pet> pets = petRepository.findPetsByFemaleOwnersInCity(city);
        return pets.stream()
                .map(petMapper::toDto)
                .toList();
    }

    @Override
    public List<PetResponseDTO> getPetsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));

        return user.getPets().stream()
                .map(pet -> new PetResponseDTO(pet.getId(), pet.getName(), pet.getAge(), pet.getType().toStringValue()))
                .toList();
    }
}

