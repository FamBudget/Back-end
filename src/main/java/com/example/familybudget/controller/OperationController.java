package com.example.familybudget.controller;

import com.example.familybudget.controller.util.ControllerUtil;
import com.example.familybudget.dto.OperationDto;
import com.example.familybudget.service.OperationService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/operations")
public class OperationController {

    private final OperationService operationService;
    private final ControllerUtil controllerUtil;
    private static final String AUTHORIZATION = "Authorization";

    @ApiOperation(value = "Create a new operation of income")
    @PostMapping("/income")
    ResponseEntity<OperationDto> addOperationIncome(
            @RequestBody @Valid OperationDto newOperation,
            @RequestParam @Email String email,
            @RequestHeader(AUTHORIZATION) String token) {

        controllerUtil.validateTokenAndEmail(email, token);
        OperationDto operationDto = operationService.addOperationIncome(newOperation, email);

        return new ResponseEntity<>(operationDto, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Create a new operation of expense")
    @PostMapping("/expense")
    ResponseEntity<OperationDto> addOperationExpense(
            @RequestBody @Valid OperationDto newOperation,
            @RequestParam @Email String email,
            @RequestHeader(AUTHORIZATION) String token) {

        controllerUtil.validateTokenAndEmail(email, token);
        OperationDto operationDto = operationService.addOperationExpense(newOperation, email);

        return new ResponseEntity<>(operationDto, HttpStatus.CREATED);
    }
}
