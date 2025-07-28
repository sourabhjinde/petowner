package com.petcare.petowner.mapper;

import com.petcare.petowner.dto.AddressRequestDTO;
import com.petcare.petowner.dto.AddressResponseDTO;
import com.petcare.petowner.entity.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AddressMapperTest {

    @Autowired
    @SuppressWarnings("unused")
    private AddressMapper addressMapper;

    @Test
    void testToEntity_ShouldMapFieldsCorrectly() {
        AddressRequestDTO dto = new AddressRequestDTO(
                "Mumbai",
                "Street",
                "Linking Road",
                "42A"
        );

        Address entity = addressMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals("Mumbai", entity.getCity());
        assertEquals("Street", entity.getType());
        assertEquals("Linking Road", entity.getAddressName());
        assertEquals("42A", entity.getNumber());

        // Ignored fields should be null
        assertNull(entity.getId());
        assertNull(entity.getAddressHash());
        assertNull(entity.getNormalizedAddressKey());
    }

    @Test
    void testToDto_ShouldMapFieldsCorrectly() {
        Address address = new Address();
        address.setId(10L);
        address.setCity("Delhi");
        address.setType("Avenue");
        address.setAddressName("Janpath");
        address.setNumber("17");

        AddressResponseDTO dto = addressMapper.toDto(address);

        assertNotNull(dto);
        assertEquals(10L, dto.id());
        assertEquals("Delhi", dto.city());
        assertEquals("Avenue", dto.type());
        assertEquals("Janpath", dto.addressName());
        assertEquals("17", dto.number());
    }

    @Test
    void testToDtoList_ShouldMapListCorrectly() {
        Address addr1 = new Address();
        addr1.setId(1L);
        addr1.setCity("Pune");
        addr1.setType("Road");
        addr1.setAddressName("FC Road");
        addr1.setNumber("101");

        Address addr2 = new Address();
        addr2.setId(2L);
        addr2.setCity("Nagpur");
        addr2.setType("Street");
        addr2.setAddressName("Mahatma Gandhi");
        addr2.setNumber("202");

        List<Address> list = List.of(addr1, addr2);

        List<AddressResponseDTO> dtoList = addressMapper.toDtoList(list);

        assertEquals(2, dtoList.size());
        assertEquals("Pune", dtoList.get(0).city());
        assertEquals("Nagpur", dtoList.get(1).city());
    }
}
