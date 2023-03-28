package com.example.familybudget.controller;

import com.example.familybudget.dto.AuthenticationRequest;
import com.example.familybudget.dto.AuthenticationResponse;
import com.example.familybudget.dto.RegistrationRequest;
import com.example.familybudget.entity.Status;
import com.example.familybudget.entity.User;
import com.example.familybudget.exception.ForbiddenException;
import com.example.familybudget.mapper.UserMapper;
import com.example.familybudget.security.JwtProvider;
import com.example.familybudget.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/registration")
    public ResponseEntity<Object> registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        User user = userService.registerUser(registrationRequest);
        return new ResponseEntity<>(UserMapper.INSTANCE.toUserDto(user), HttpStatus.CREATED);
    }

    @PostMapping("/authentication")
    public ResponseEntity<Object> authentication(@RequestBody @Valid AuthenticationRequest request) {
        User user = userService.findByEmailAndPassword(request.getEmail(), request.getPassword());
        if (user.getStatus() != Status.ACTIVE) {
            throw new ForbiddenException("User not activated");
        }
        String token = jwtProvider.generateToken(user.getEmail());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(token);
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }

    @GetMapping("/activate/{code}")
    public ResponseEntity<Object> activate(@PathVariable String code) {
        userService.activateUser(code);
            return new ResponseEntity<>("User activated", HttpStatus.OK);
    }
}
