package com.petcare.petowner.mapper;

import com.petcare.petowner.dto.*;
import com.petcare.petowner.entity.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PetMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deceased", ignore = true)
    @Mapping(target = "owners", ignore = true)
    Pet toEntity(PetRequestDTO dto);

    @Mapping(source = "type", target = "type", qualifiedByName = "typeToString")
    PetResponseDTO toDto(Pet pet);

    List<PetResponseDTO> toDtoList(List<Pet> pets);

    PetShortDTO toShortDto(Pet pet);

    @Named("typeToString")
    default String typeToString(PetType petType) {
        return petType != null ? petType.name() : null;
    }

    @Named("stringToType")
    default PetType stringToType(String type) {
        try {
            return type != null ? PetType.valueOf(type.toUpperCase()) : null;
        } catch (IllegalArgumentException e) {
            return PetType.OTHER;
        }
    }
}

