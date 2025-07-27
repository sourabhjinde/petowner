package com.petcare.petowner.mapper;

import com.petcare.petowner.dto.*;
import com.petcare.petowner.entity.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address.addressHash", ignore = true)
    @Mapping(target = "address.normalizedAddressKey", ignore = true)
    @Mapping(target = "address.id", ignore = true)
    @Mapping(target = "pets", ignore = true)
    @Mapping(target = "deceased", ignore = true)
    User toEntity(UserRequestDTO userRequestDTO);

    @Mapping(source = "gender", target = "gender", qualifiedByName = "genderToString")
    UserResponseDTO toDto(User user);

    List<UserResponseDTO> toDtoList(List<User> users);

    @Named("genderToString")
    default String genderToString(Gender gender) {
        return gender.name();
    }
}

