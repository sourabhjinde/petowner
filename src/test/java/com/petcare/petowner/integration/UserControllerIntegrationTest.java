package com.petcare.petowner.integration;

import com.petcare.petowner.dto.AddressRequestDTO;
import com.petcare.petowner.dto.UserRequestDTO;
import com.petcare.petowner.entity.Address;
import com.petcare.petowner.entity.Gender;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petcare.petowner.entity.User;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @BeforeEach
    void setup() {
        petRepository.deleteAll();
        userRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void createUser_shouldReturn201() throws Exception {
        AddressRequestDTO addressRequestDTO = new AddressRequestDTO("Mumbai", "road", "wallstreet", "918745987745");
        UserRequestDTO requestDTO = new UserRequestDTO("Doe", "John", 30, Gender.MALE, addressRequestDTO);


        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Doe"));
    }

    @Test
    void updateUser_shouldReturn200() throws Exception {
        Address address = new Address();
        address.setCity("Old City");
        address.setType("road");
        address.setNumber("1234567890");
        Address savedAddress = addressRepository.save(address);

        // Create a user and save
        User user = new User();
        user.setName("OldName");
        user.setFirstName("firstName");
        user.setAge(30);
        user.setGender(Gender.MALE);
        user.setAddress(savedAddress);
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getId();

        AddressRequestDTO addressRequestDTO = new AddressRequestDTO("Mumbai", "road", "wallstreet", "918745987745");
        UserRequestDTO requestDTO = new UserRequestDTO("Smith", "Jane", 28, Gender.MALE, addressRequestDTO);

        mockMvc.perform(put("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Smith"));
    }

    @Test
    void markUserAsDeceased_shouldReturn204() throws Exception {
        Address address = new Address();
        address.setCity("Old City");
        address.setType("road");
        address.setNumber("1234567890");
        Address savedAddress = addressRepository.save(address);

        User user = new User();
        user.setName("OldName");
        user.setFirstName("firstName");
        user.setAge(30);
        user.setGender(Gender.MALE);
        user.setAddress(savedAddress);
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getId();

        mockMvc.perform(patch("/users/" + userId + "/death"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getByNameAndFirstName_shouldReturn200() throws Exception {
        Address address = new Address();
        address.setCity("Old City");
        address.setType("road");
        address.setNumber("1234567890");
        Address savedAddress = addressRepository.save(address);

        User user = new User();
        user.setName("Doe");
        user.setFirstName("John");
        user.setAge(30);
        user.setGender(Gender.MALE);
        user.setAddress(savedAddress);
        userRepository.save(user);

        mockMvc.perform(get("/users/by-name")
                        .param("name", "Doe")
                        .param("firstName", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Doe"));
    }

    @Test
    void getUsersByPetTypeAndCity_shouldReturn200() throws Exception {
        mockMvc.perform(get("/users/by-pet")
                        .param("type", "DOG")
                        .param("city", "Pune"))
                .andExpect(status().isOk());
    }
}

