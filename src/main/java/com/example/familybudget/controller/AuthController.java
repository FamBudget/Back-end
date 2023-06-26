package com.example.familybudget.controller;

import com.example.familybudget.controller.util.ControllerUtil;
import com.example.familybudget.dto.*;
import com.example.familybudget.dto.ResponseUserSecurityStatus;
import com.example.familybudget.entity.Currency;
import com.example.familybudget.entity.Status;
import com.example.familybudget.entity.User;
import com.example.familybudget.exception.CurrencyNotValidException;
import com.example.familybudget.exception.ForbiddenException;
import com.example.familybudget.mapper.UserMapper;
import com.example.familybudget.security.JwtProvider;
import com.example.familybudget.service.Platform;
import com.example.familybudget.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@RestController
@Api(description = "Controller for registration and activation users and for authentication")
public class AuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final ControllerUtil controllerUtil;
    private static final String AUTHORIZATION = "Authorization";
    private static final String X_CLIENT_PLATFORM = "X-Client-Platform";

    @ApiOperation(value = "Registration new user", notes = "The email, firstName, lastName, currency, password and " +
            "confirmPassword are sent in json format. " +
            "The body of the request is the RegistrationRequest class. Add new user with password to database. " +
            "The body of the response is the UserDto class (it contains id, email, firstName, lastName and currency). " +
            "If a user with the same email already exists or password has not correct format or " +
            "password and confirmPassword don't match or currency has incorrect name, " +
            "an appropriate exception will be thrown")
    @PostMapping("/registration")
    public ResponseEntity<UserDto> registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) throws Exception {
        Currency currency = Currency.valid(registrationRequest.getCurrency())
                .orElseThrow(() -> new CurrencyNotValidException("Unknown state: " + registrationRequest.getCurrency()));
        User user = UserMapper.INSTANCE.registrationToUser(registrationRequest);
        user.setEmail(registrationRequest.getEmail().toLowerCase());
        user.setCurrency(currency);
        userService.registerUser(user);
        UserDto userDto = UserMapper.INSTANCE.toUserDto(user);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @ApiOperation(value = "User authentication", notes =
            "The username and password are sent in json format. " +
            "The body of the request is the AuthenticationRequest class. " +
            "If the name and password match the entry in the database, " +
            "then a token is generated and sent in json format. The body of the " +
            "response is the AuthenticationResponse class. Else an appropriate exception will be thrown.")
    @PostMapping("/authentication")
    public ResponseEntity<AuthenticationResponse> authentication(@RequestBody @Valid AuthenticationRequest request) {

        User user = userService.findByEmailAndPassword(request.getEmail(), request.getPassword());
        if (user.getStatus() != Status.ACTIVE) {
            throw new ForbiddenException("User not activated");
        }
        String token = jwtProvider.generateToken(user.getEmail().toLowerCase());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(token);
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "Logout user", notes = "Returns the token success lock result")
    @PostMapping("/auth/logout")
    public ResponseEntity<ResponseUserSecurityStatus>  logout(@RequestHeader(AUTHORIZATION) String token,
                                                              @NotBlank @Email @RequestParam String email) {

        controllerUtil.validateTokenAndEmail(email, token);
        jwtProvider.blacklistToken(token.substring(7));

        ResponseUserSecurityStatus result = new ResponseUserSecurityStatus();
        result.setEmail(email);
        result.setStatus("Logout success");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "Repair password send link", notes = "First step of password recovery. Send link to email")
    @PostMapping("/reset-password")
    public ResponseEntity<ResponseUserSecurityStatus>  requestResetPassword(
            @RequestHeader(value = X_CLIENT_PLATFORM, defaultValue = "ANGULAR") Platform clientPlatform,
            @NotBlank @Email @RequestParam String email) throws Exception {
        ResponseUserSecurityStatus resetPassword = userService.requestResetPassword(email.toLowerCase(), clientPlatform);
        return new ResponseEntity<>(resetPassword, HttpStatus.OK);
    }

    @ApiOperation(value = "verify code", notes = "verification code for repair password")
    @GetMapping("/verify-code/{code}")
    public ResponseEntity<ResponseUserSecurityStatus> verifyCode(@NotBlank @Email @RequestParam String email,
                                                                 @PathVariable @NotBlank String code) {
        ResponseUserSecurityStatus resetPassword = userService.verifyCode(email.toLowerCase(), code);
        return new ResponseEntity<>(resetPassword, HttpStatus.OK);
    }

    @ApiOperation(value = "Change password", notes = "Second step of password recovery. Input new password")
    @PutMapping("/change-password/{code}")
    public ResponseEntity<ResponseUserSecurityStatus>  changePassword(@NotBlank @Email @RequestParam String email,
                                                                      @PathVariable @NotBlank String code,
                                                                      @RequestBody @Valid NewPasswordRequest newPasswordRequest) {
        ResponseUserSecurityStatus resetPassword = userService.repairPassword(email.toLowerCase(), code, newPasswordRequest);
        return new ResponseEntity<>(resetPassword, HttpStatus.OK);
    }
}
