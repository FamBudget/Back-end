package com.example.familybudget.controller;

import com.example.familybudget.controller.util.ControllerUtil;
import com.example.familybudget.dto.NewPasswordRequest;
import com.example.familybudget.dto.ResponseUserSecurityStatus;
import com.example.familybudget.dto.UpdateUserRequest;
import com.example.familybudget.dto.UserDto;
import com.example.familybudget.security.JwtProvider;
import com.example.familybudget.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/users")
@Validated
public class UserController {

    private final UserService userService;
    private final ControllerUtil controllerUtil;
    private final JwtProvider jwtProvider;
    private static final String AUTHORIZATION = "Authorization";

    @ApiOperation(value = "Get user info", notes = "Get user info by email")
    @GetMapping
    public ResponseEntity<UserDto> getUserByEmail(@RequestHeader(AUTHORIZATION) String token,
                                                  @NotBlank @Email @RequestParam String email) {

        controllerUtil.validateTokenAndEmail(email, token);
        UserDto userDto = userService.getUserByEmail(email.toLowerCase());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete user", notes = "Delete user by email")
    @DeleteMapping
    public ResponseEntity<?>  deleteUserByEmail(@RequestHeader(AUTHORIZATION) String token,
                                                      @NotBlank
                                                      @Email
                                                      @RequestParam String email) {

        controllerUtil.validateTokenAndEmail(email, token);
        userService.deleteUserByEmail(email.toLowerCase());
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Change password", notes = "Input new password")
    @PutMapping("/change-password")
    public ResponseEntity<ResponseUserSecurityStatus>  changePassword(@RequestHeader(AUTHORIZATION) String token,
                                                                      @NotBlank @Email @RequestParam String email,
                                                                      @RequestBody @Valid
                                                                          NewPasswordRequest newPasswordRequest) {
        controllerUtil.validateTokenAndEmail(email, token);
        ResponseUserSecurityStatus resetPassword = userService.changePassword(email.toLowerCase(), newPasswordRequest);
        return new ResponseEntity<>(resetPassword, HttpStatus.OK);
    }

    @ApiOperation(value = "Update user info", notes = "Update user info")
    @PutMapping
    public ResponseEntity<UserDto>  updateUser(@RequestHeader(AUTHORIZATION) String token,
                                                                      @NotBlank @Email @RequestParam String email,
                                                                      @RequestBody @Valid
                                                                  UpdateUserRequest updateUser) {
        controllerUtil.validateTokenAndEmail(email, token);
        UserDto userDto = userService.updateUser(email.toLowerCase(), updateUser);
        if (!userDto.getEmail().equals(email)) {
            jwtProvider.blacklistToken(token.substring(7));
        }
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}