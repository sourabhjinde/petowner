package com.petcare.petowner.mapper;

import com.petcare.petowner.dto.PetRequestDTO;
import com.petcare.petowner.dto.PetResponseDTO;
import com.petcare.petowner.dto.PetShortDTO;
import com.petcare.petowner.entity.Pet;
import com.petcare.petowner.entity.PetType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PetMapperTest {
    private final PetMapper petMapper;

    @Autowired
    PetMapperTest(PetMapper petMapper) {
        this.petMapper = petMapper;
    }

    @Test
    void testToEntity_ShouldMapFieldsCorrectly() {
        PetRequestDTO dto = new PetRequestDTO("Bruno", 3, PetType.DOG, null);

        Pet pet = petMapper.toEntity(dto);

        assertNotNull(pet);
        assertEquals("Bruno", pet.getName());
        assertEquals(3, pet.getAge());
        assertEquals(PetType.DOG, pet.getType());

        // Ignored fields
        assertNull(pet.getId());
        assertFalse(pet.isDeceased());
        assertTrue(pet.getOwners().isEmpty());
    }

    @Test
    void testToDto_ShouldMapFieldsCorrectly() {
        Pet pet = new Pet();
        pet.setId(100L);
        pet.setName("Whiskers");
        pet.setAge(2);
        pet.setType(PetType.CAT);

        PetResponseDTO dto = petMapper.toDto(pet);

        assertNotNull(dto);
        assertEquals(100L, dto.id());
        assertEquals("Whiskers", dto.name());
        assertEquals(2, dto.age());
        assertEquals("CAT", dto.type());
    }

    @Test
    void testToDtoList_ShouldMapListCorrectly() {
        Pet pet1 = new Pet(1L, "Leo", 4, PetType.DOG, new HashSet<>(), false);
        Pet pet2 = new Pet(2L, "Milo", 1, PetType.SNAKE, new HashSet<>(), false);

        List<PetResponseDTO> dtoList = petMapper.toDtoList(List.of(pet1, pet2));

        assertEquals(2, dtoList.size());
        assertEquals("Leo", dtoList.get(0).name());
        assertEquals("Milo", dtoList.get(1).name());
    }

    @Test
    void testToShortDto_ShouldReturnShortDTO() {
        Pet pet = new Pet(10L, "Coco", 5, PetType.SPIDER, new HashSet<>(), false);

        PetShortDTO dto = petMapper.toShortDto(pet);

        assertNotNull(dto);
        assertEquals(10L, dto.id());
        assertEquals("Coco", dto.name());
        assertEquals("SPIDER", dto.type());
    }

    @Test
    void testTypeToString_ShouldReturnEnumName() {
        String typeStr = petMapper.typeToString(PetType.CAT);
        assertEquals("CAT", typeStr);
    }

    @Test
    void testStringToType_ShouldReturnEnum_WhenValid() {
        PetType type = petMapper.stringToType("dog");
        assertEquals(PetType.DOG, type);
    }

    @Test
    void testStringToType_ShouldReturnOther_WhenInvalid() {
        PetType type = petMapper.stringToType("dragon");
        assertEquals(PetType.OTHER, type);
    }

    @Test
    void testStringToType_ShouldReturnNull_WhenNullInput() {
        PetType type = petMapper.stringToType(null);
        assertNull(type);
    }
}

