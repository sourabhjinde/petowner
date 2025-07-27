package com.petcare.petowner.service;


import com.petcare.petowner.dto.PetRequestDTO;
import com.petcare.petowner.dto.PetResponseDTO;

import java.util.List;
import java.util.Set;

public interface PetService {
    PetResponseDTO createPet(PetRequestDTO petRequestDTO, Set<Long> ownerIds);
    PetResponseDTO updatePet(Long id, PetRequestDTO updatedPetRequestDTO);
    void markPetAsDeceased(Long petId);
    List<PetResponseDTO> getPetsByUserId(Long userId);
    List<PetResponseDTO> getPetsByCity(String city);
    List<PetResponseDTO> getPetsByUser(Long userId);
    List<PetResponseDTO> findPetsByFemaleOwnersInCity(String city);
    List<PetResponseDTO> findPetsOwnedByUser(String name, String firstName);
}

