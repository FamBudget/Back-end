package com.example.familybudget.mapper;

import com.example.familybudget.dto.RegistrationRequest;
import com.example.familybudget.dto.UserDto;
import com.example.familybudget.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "currency", target = "currency")
    UserDto toUserDto(User user);

    User registrationToUser(RegistrationRequest registrationRequest);
}