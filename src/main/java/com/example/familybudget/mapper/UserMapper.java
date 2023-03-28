package com.example.familybudget.mapper;

import com.example.familybudget.dto.RegistrationRequest;
import com.example.familybudget.dto.UserDto;
import com.example.familybudget.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toUserDto(User user);
    User registrationToUser(RegistrationRequest registrationRequest);
}