package com.example.familybudget.controller;

import com.example.familybudget.dto.UserDto;
import com.example.familybudget.entity.User;
import com.example.familybudget.exception.ForbiddenException;
import com.example.familybudget.mapper.UserMapper;
import com.example.familybudget.repository.UserRepository;
import com.example.familybudget.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/users")
@Validated
public class UserController {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private static final String AUTHORIZATION = "Authorization";

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto>  getUserById(@RequestHeader(AUTHORIZATION) String token,
                                                @Email @RequestParam String email,
                                                @PathVariable long userId) {
        String emailDec = email.contains("%40") ? email.replace("%40" , "@"): email;
        String emailJwt = jwtProvider.getEmailFromToken(token.substring(7));
        if (!emailDec.equals(emailJwt)) {
            throw new ForbiddenException("The email from the requestBody does not match the email from the token");
        }
        User user = userRepository.getById(userId);
        return new ResponseEntity<>(UserMapper.INSTANCE.toUserDto(user), HttpStatus.OK);
    }
}