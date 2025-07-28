package com.petcare.petowner.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petcare.petowner.dto.AddressRequestDTO;
import com.petcare.petowner.entity.Address;
import com.petcare.petowner.repository.AddressRepository;
import com.petcare.petowner.repository.PetRepository;
import com.petcare.petowner.repository.UserRepository;
import com.petcare.petowner.util.AddressHashUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AddressControllerIntegrationTest {

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
    void shouldCreateAddressSuccessfully() throws Exception {
        AddressRequestDTO dto = new AddressRequestDTO("Mumbai", "road", "Link Road", "400001");

        mockMvc.perform(post("/api/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.city", is("Mumbai")))
                .andExpect(jsonPath("$.type", is("road")))
                .andExpect(jsonPath("$.addressName", is("Link Road")))
                .andExpect(jsonPath("$.number", is("400001")));
    }

    @Test
    void shouldReturnAllAddresses() throws Exception {
        Address address1 = new Address();
        address1.setAddressName("MG Road");
        address1.setCity("Pune");
        address1.setType("road");
        address1.setNumber("10");
        String hash = AddressHashUtil.normalizeAndHash(
                address1.getCity(),
                address1.getType(),
                address1.getAddressName(),
                address1.getNumber()
        );
        address1.setAddressHash(hash);

        Address address2 = new Address();
        address2.setAddressName("Linking Road");
        address2.setCity("Mumbai");
        address2.setType("road");
        address2.setNumber("11");
        String hash2 = AddressHashUtil.normalizeAndHash(
                address2.getCity(),
                address2.getType(),
                address2.getAddressName(),
                address2.getNumber()
        );
        address1.setAddressHash(hash2);

        addressRepository.save(address1);
        addressRepository.save(address2);

        mockMvc.perform(get("/api/addresses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].city", is("Pune")))
                .andExpect(jsonPath("$[1].city", is("Mumbai")));
    }

    @Test
    void shouldReturnBadRequestOnInvalidInput() throws Exception {
        AddressRequestDTO dto = new AddressRequestDTO("", "", "", ""); // all blank

        mockMvc.perform(post("/api/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}

