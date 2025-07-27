package com.petcare.petowner.service.impl;

import com.petcare.petowner.dto.*;
import com.petcare.petowner.entity.*;
import com.petcare.petowner.exception.NotFoundException;
import com.petcare.petowner.exception.ResourceNotFoundException;
import com.petcare.petowner.exception.Sha256Exception;
import com.petcare.petowner.mapper.UserMapper;
import com.petcare.petowner.repository.*;
import com.petcare.petowner.service.UserService;
import com.petcare.petowner.util.AddressHashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) throws Sha256Exception {
        var addressDTO = userRequestDTO.address();
        String hash = AddressHashUtil.normalizeAndHash(
                addressDTO.city(),
                addressDTO.type(),
                addressDTO.addressName(),
                addressDTO.number()
        );

        Address address = addressRepository.findByAddressHash(hash)
                .orElseGet(() -> {
                    Address newAddress = new Address();
                    newAddress.setCity(addressDTO.city());
                    newAddress.setType(addressDTO.type());
                    newAddress.setAddressName(addressDTO.addressName());
                    newAddress.setNumber(addressDTO.number());
                    newAddress.setAddressHash(hash);
                    return addressRepository.save(newAddress);
                });

        User user = new User();
        user.setName(userRequestDTO.name());
        user.setFirstName(userRequestDTO.firstName());
        user.setGender(userRequestDTO.gender());
        user.setAge(userRequestDTO.age());
        user.setAddress(address);

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDTO updateUser(Long userId, UserRequestDTO requestDTO) throws Sha256Exception{
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        AddressRequestDTO addressDTO = requestDTO.address();
        String hash = AddressHashUtil.normalizeAndHash(
                addressDTO.city(),
                addressDTO.type(),
                addressDTO.addressName(),
                addressDTO.number()
        );

        Address address = addressRepository.findByAddressHash(hash)
                .orElseGet(() -> {
                    Address newAddress = new Address();
                    newAddress.setCity(addressDTO.city());
                    newAddress.setType(addressDTO.type());
                    newAddress.setAddressName(addressDTO.addressName());
                    newAddress.setNumber(addressDTO.number());
                    newAddress.setAddressHash(hash);
                    return addressRepository.save(newAddress);
                });

        user.setName(requestDTO.name());
        user.setFirstName(requestDTO.firstName());
        user.setAge(requestDTO.age());
        user.setGender(requestDTO.gender());
        user.setAddress(address);

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public List<UserResponseDTO> getUsersByPetTypeAndCity(String city, String type) {
        return userRepository.findUsersByPetTypeAndCity(type, city).stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public List<UserResponseDTO> getUsersByNameAndFirstName(String name, String firstName) {
        List<User> users = userRepository.findByNameIgnoreCaseAndFirstNameIgnoreCase(name, firstName);
        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public void markUserAsDeceased(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));

        user.setDeceased(true);
        userRepository.save(user);
    }
}
