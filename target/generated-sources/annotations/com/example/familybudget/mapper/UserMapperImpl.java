package com.example.familybudget.mapper;

import com.example.familybudget.dto.RegistrationRequest;
import com.example.familybudget.dto.UserDto;
import com.example.familybudget.entity.Currency;
import com.example.familybudget.entity.User;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-29T17:52:34+0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.14.1 (Amazon.com Inc.)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( user.getId() );
        userDto.setEmail( user.getEmail() );
        userDto.setFirstName( user.getFirstName() );
        userDto.setLastName( user.getLastName() );
        userDto.setCurrency( user.getCurrency() );

        return userDto;
    }

    @Override
    public User registrationToUser(RegistrationRequest registrationRequest) {
        if ( registrationRequest == null ) {
            return null;
        }

        User user = new User();

        user.setEmail( registrationRequest.getEmail() );
        user.setFirstName( registrationRequest.getFirstName() );
        user.setLastName( registrationRequest.getLastName() );
        user.setPassword( registrationRequest.getPassword() );
        user.setConfirmPassword( registrationRequest.getConfirmPassword() );
        if ( registrationRequest.getCurrency() != null ) {
            user.setCurrency( Enum.valueOf( Currency.class, registrationRequest.getCurrency() ) );
        }

        return user;
    }
}
