package com.petcare.petowner.service;

import com.petcare.petowner.dto.UserRequestDTO;
import com.petcare.petowner.dto.UserResponseDTO;
import com.petcare.petowner.exception.Sha256Exception;

import java.util.List;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO userRequestDTO) throws Sha256Exception;
    UserResponseDTO updateUser(Long id, UserRequestDTO updatedUserRequestDTO) throws Sha256Exception;
    void markUserAsDeceased(Long userId);
    List<UserResponseDTO> getUsersByPetTypeAndCity(String petType, String city);
    List<UserResponseDTO> getUsersByNameAndFirstName(String name, String firstName);
}

