package com.example.familybudget.controller;

import com.example.familybudget.controller.util.ControllerUtil;
import com.example.familybudget.dto.AccountDto;
import com.example.familybudget.dto.NewAccountDto;
import com.example.familybudget.service.AccountService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final ControllerUtil controllerUtil;
    private static final String AUTHORIZATION = "Authorization";

    @ApiOperation(value = "Get accounts user email",
            notes = "This code is an endpoint for retrieving accounts by user email. " +
                    "It takes in several parameters, including an authorization token, the user's email," +
                    " a starting index (from) for pagination, and a maximum (size) number of results to return.")
    @GetMapping
    public ResponseEntity<List<AccountDto>> getAccounts(
            @RequestHeader(AUTHORIZATION) String token,
            @Email @RequestParam String email,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        controllerUtil.validateTokenAndEmail(email, token);
        List<AccountDto> accountDtoList = accountService.getAccountsByEmail(email, from, size);
        return new ResponseEntity<>(accountDtoList, HttpStatus.OK);
    }

    @ApiOperation(value = "Create a new account",
            notes = "Creates a new account for the user with the specified email.\n\n" +
                    "The email should be provided as a request parameter and the account details should be provided in the request body.\n\n" +
                    "If the email provided in the request parameter does not match the email in the JWT token, a ForbiddenException will be thrown.\n\n" +
                    "Returns the created account.")
    @PostMapping
    ResponseEntity<AccountDto> addAccount(
            @RequestBody @Valid NewAccountDto newAccountDto,
            @RequestParam @Email String email,
            @RequestHeader(AUTHORIZATION) String token) {

        controllerUtil.validateTokenAndEmail(email, token);
        AccountDto accountDto = accountService.addAccount(newAccountDto, email);

        return new ResponseEntity<>(accountDto, HttpStatus.CREATED);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDto>  getAccountById(@RequestHeader(AUTHORIZATION) String token,
                                                              @Email @RequestParam String email,
                                                              @PathVariable long accountId) {

        controllerUtil.validateTokenAndEmail(email, token);
        AccountDto accountDto = accountService.getAccountById(accountId, email);
        return new ResponseEntity<>(accountDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Update an existing account",
            notes = "Updates an existing account for the user with the specified email.\n\n" +
                    "The email should be provided as a request parameter and the updated account details should be provided in the request body.\n\n" +
                    "If the email provided in the request parameter does not match the email in the JWT token, a ForbiddenException will be thrown.\n\n" +
                    "Returns the updated account.")
    @PutMapping
    public ResponseEntity<AccountDto> updateAccount(
            @RequestHeader(AUTHORIZATION) String token,
            @RequestParam @Email String email,
            @RequestBody @Valid AccountDto updateAccountDto) {

        controllerUtil.validateTokenAndEmail(email, token);
        AccountDto accountDto = accountService.updateAccount(updateAccountDto, email);

        return new ResponseEntity<>(accountDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete an existing account",
            notes = "Deletes an existing account for the user with the specified email.\n\n" +
                    "The email should be provided as a request parameter and the account ID should be provided as a path variable.\n\n" +
                    "If the email provided in the request parameter does not match the email in the JWT token, a ForbiddenException will be thrown.\n\n" +
                    "Returns a ResponseEntity with a status code of 200 OK.")
    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> deleteAccount(
            @RequestHeader(AUTHORIZATION) String token,
            @Email @RequestParam String email,
            @PathVariable Long accountId) {

        controllerUtil.validateTokenAndEmail(email, token);
        accountService.deleteAccountById(accountId, email);

        return ResponseEntity.ok().build();
    }
}
