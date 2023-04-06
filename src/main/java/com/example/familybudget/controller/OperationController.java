package com.example.familybudget.controller;

import com.example.familybudget.controller.util.ControllerUtil;
import com.example.familybudget.dto.OperationDto;
import com.example.familybudget.dto.ResponseOperation;
import com.example.familybudget.exception.ForbiddenException;
import com.example.familybudget.service.OperationService;
import com.example.familybudget.service.SortParameter;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/operations")
public class OperationController {

    private final OperationService operationService;
    private final ControllerUtil controllerUtil;
    private static final String AUTHORIZATION = "Authorization";

    @ApiOperation(value = "Get expense operations by user email")
    @GetMapping("/expense")
    public ResponseEntity<List<ResponseOperation>> getOperationsExpense(
            @RequestHeader(AUTHORIZATION) String token,
            @Email @RequestParam String email,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam(required = false) LocalDateTime startDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false, defaultValue = "DATE") String sort,
            @RequestParam(required = false, defaultValue = "true") Boolean sortDesc,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {

        SortParameter sortParameter;

        try {
            sortParameter =  SortParameter.valueOf(sort);
        } catch (IllegalArgumentException e) {
            throw new ForbiddenException("Unknown sort: " + sort);
        }

        controllerUtil.validateTokenAndEmail(email, token);
        List<ResponseOperation> operationsDto = operationService
                .getOperationsExpense(email, startDate, endDate, sortParameter, sortDesc, from, size);
        return new ResponseEntity<>(operationsDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Get income operations by user email")
    @GetMapping("/income")
    public ResponseEntity<List<ResponseOperation>> getOperationsIncome(
            @RequestHeader(AUTHORIZATION) String token,
            @Email @RequestParam String email,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam(required = false) LocalDateTime startDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false, defaultValue = "DATE") String sort,
            @RequestParam(required = false, defaultValue = "true") Boolean sortDesc,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {

        SortParameter sortParameter;

        try {
            sortParameter =  SortParameter.valueOf(sort);
        } catch (IllegalArgumentException e) {
            throw new ForbiddenException("Unknown sort: " + sort);
        }

        controllerUtil.validateTokenAndEmail(email, token);
        List<ResponseOperation> operationsDto = operationService
                .getOperationsIncome(email, startDate, endDate, sortParameter, sortDesc, from, size);
        return new ResponseEntity<>(operationsDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Create a new operation of income")
    @PostMapping("/income")
    ResponseEntity<ResponseOperation> addOperationIncome(
            @RequestBody @Valid OperationDto newOperation,
            @RequestParam @Email String email,
            @RequestHeader(AUTHORIZATION) String token) {

        controllerUtil.validateTokenAndEmail(email, token);
        ResponseOperation operationDto = operationService.addOperationIncome(newOperation, email);

        return new ResponseEntity<>(operationDto, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Create a new operation of expense")
    @PostMapping("/expense")
    ResponseEntity<ResponseOperation> addOperationExpense(
            @RequestBody @Valid OperationDto newOperation,
            @RequestParam @Email String email,
            @RequestHeader(AUTHORIZATION) String token) {

        controllerUtil.validateTokenAndEmail(email, token);
        ResponseOperation operationDto = operationService.addOperationExpense(newOperation, email);

        return new ResponseEntity<>(operationDto, HttpStatus.CREATED);
    }
}
