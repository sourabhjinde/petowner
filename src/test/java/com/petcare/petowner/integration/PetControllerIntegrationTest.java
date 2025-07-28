package com.petcare.petowner.integration;

import com.petcare.petowner.dto.PetRequestDTO;
import com.petcare.petowner.entity.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petcare.petowner.repository.AddressRepository;
import com.petcare.petowner.repository.PetRepository;
import com.petcare.petowner.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PetControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Long existingUserId;


    @BeforeEach
    void setUp() {
        petRepository.deleteAll();
        userRepository.deleteAll();
        addressRepository.deleteAll();

        Address address = new Address();
        address.setCity("Pune");
        address = addressRepository.save(address);

        User user = new User();
        user.setName("Doe");
        user.setFirstName("John");
        user.setAddress(address);
        user.setGender(Gender.MALE);

        user = userRepository.save(user);
        existingUserId = user.getId();
    }

    @Test
    void shouldCreatePetSuccessfully() throws Exception {
        PetRequestDTO requestDTO = new PetRequestDTO("Koko", 11, PetType.DOG, Set.of(existingUserId));

        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("DOG"))
                .andExpect(jsonPath("$.name").value("Koko"));
    }

    @Test
    void shouldUpdatePetSuccessfully() throws Exception {
        Pet pet = new Pet();
        pet.setName("Kitty");
        pet.setType(PetType.CAT);
        pet.setAge(12);
        pet.setOwners(Set.of(userRepository.findById(existingUserId).orElseThrow()));
        pet = petRepository.save(pet);

        PetRequestDTO updateDTO = new PetRequestDTO("Cat", 12 ,PetType.DOG , Set.of(existingUserId));

        mockMvc.perform(put("/pets/" + pet.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("DOG"))
                .andExpect(jsonPath("$.name").value("Cat"));
    }

    @Test
    void shouldMarkPetAsDeceased() throws Exception {
        Pet pet = new Pet();
        pet.setType(PetType.CAT);
        pet.setAge(12);
        pet.setOwners(Set.of(userRepository.findById(existingUserId).orElseThrow()));
        pet = petRepository.save(pet);

        mockMvc.perform(patch("/pets/" + pet.getId() + "/death"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetPetsByCity() throws Exception {
        Pet pet = new Pet();
        pet.setType(PetType.SNAKE);
        pet.setAge(13);
        pet.setOwners(Set.of(userRepository.findById(existingUserId).orElseThrow()));
        petRepository.save(pet);

        mockMvc.perform(get("/pets/by-city")
                        .param("city", "Pune"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].age").value(13));
    }

    @Test
    void shouldGetPetsByFemaleOwnersInCity() throws Exception {
        User user = userRepository.findById(existingUserId).orElseThrow();
        user.setGender(Gender.FEMALE);
        userRepository.save(user);

        Pet pet = new Pet();
        pet.setType(PetType.SPIDER);
        pet.setAge(12);
        pet.setOwners(Set.of(user));
        petRepository.save(pet);

        mockMvc.perform(get("/pets/women-owned-pets/by-city")
                        .param("city", "Pune"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].age").value(12));
    }

    @Test
    void shouldGetPetsByOwnerNameAndFirstName() throws Exception {
        Pet pet = new Pet();
        pet.setType(PetType.OTHER);
        pet.setAge(20);
        pet.setOwners(Set.of(userRepository.findById(existingUserId).orElseThrow()));
        petRepository.save(pet);

        mockMvc.perform(get("/pets/by-user")
                        .param("name", "Doe")
                        .param("firstName", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].age").value(20));
    }




}

