package com.example.familybudget.controller;

import com.example.familybudget.controller.util.ControllerUtil;
import com.example.familybudget.dto.UserDto;
import com.example.familybudget.entity.User;
import com.example.familybudget.mapper.UserMapper;
import com.example.familybudget.repository.UserRepository;
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
    private final ControllerUtil controllerUtil;
    private static final String AUTHORIZATION = "Authorization";

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto>  getUserById(@RequestHeader(AUTHORIZATION) String token,
                                                @Email @RequestParam String email,
                                                @PathVariable long userId) {

        controllerUtil.validateTokenAndEmail(email, token);
        User user = userRepository.getById(userId);
        return new ResponseEntity<>(UserMapper.INSTANCE.toUserDto(user), HttpStatus.OK);
    }
}