package com.example.familybudget.controller;

import com.example.familybudget.controller.util.ControllerUtil;
import com.example.familybudget.dto.UserDto;
import com.example.familybudget.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/users")
@Validated
public class UserController {

    private final UserService userService;
    private final ControllerUtil controllerUtil;
    private static final String AUTHORIZATION = "Authorization";

    @GetMapping
    public ResponseEntity<UserDto> getUserByEmail(@RequestHeader(AUTHORIZATION) String token,
                                                  @NotBlank @Email @RequestParam String email) {

        controllerUtil.validateTokenAndEmail(email, token);
        UserDto userDto = userService.getUserByEmail(email);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?>  deleteUserByEmail(@RequestHeader(AUTHORIZATION) String token,
                                                      @NotBlank
                                                      @Email
                                                      @RequestParam String email) {

        controllerUtil.validateTokenAndEmail(email, token);
        userService.deleteUserByEmail(email);
        return ResponseEntity.ok().build();
    }
}