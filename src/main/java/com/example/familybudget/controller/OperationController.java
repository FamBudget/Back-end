package com.example.familybudget.controller;

import com.example.familybudget.Created;
import com.example.familybudget.controller.util.ControllerUtil;
import com.example.familybudget.dto.OperationDto;
import com.example.familybudget.dto.OperationMovingDto;
import com.example.familybudget.dto.ResponseOperation;
import com.example.familybudget.dto.ResponseOperationMoving;
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
import javax.validation.constraints.NotBlank;
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
            @NotBlank @Email @RequestParam String email,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam(required = false) LocalDateTime startDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false, defaultValue = "DATE") SortParameter sort,
            @RequestParam(required = false, defaultValue = "true") Boolean sortDesc,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {

        controllerUtil.validateTokenAndEmail(email, token);
        List<ResponseOperation> operationsDto = operationService
                .getOperationsExpense(email.toLowerCase(), startDate, endDate, sort, sortDesc, from, size);
        return new ResponseEntity<>(operationsDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Get income operations by user email")
    @GetMapping("/income")
    public ResponseEntity<List<ResponseOperation>> getOperationsIncome(
            @RequestHeader(AUTHORIZATION) String token,
            @NotBlank @Email @RequestParam String email,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam(required = false) LocalDateTime startDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false, defaultValue = "DATE") SortParameter sort,
            @RequestParam(required = false, defaultValue = "true") Boolean sortDesc,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {

        controllerUtil.validateTokenAndEmail(email, token);
        List<ResponseOperation> operationsDto = operationService
                .getOperationsIncome(email.toLowerCase(), startDate, endDate, sort, sortDesc, from, size);
        return new ResponseEntity<>(operationsDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Get moving operations by user email")
    @GetMapping("/moving")
    public ResponseEntity<List<ResponseOperationMoving>> getOperationsMoving(
            @RequestHeader(AUTHORIZATION) String token,
            @NotBlank @Email @RequestParam String email,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam(required = false) LocalDateTime startDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false, defaultValue = "DATE") SortParameter sort,
            @RequestParam(required = false, defaultValue = "true") Boolean sortDesc,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {

        controllerUtil.validateTokenAndEmail(email, token);
        List<ResponseOperationMoving> operationsDto = operationService
                .getOperationsMoving(email.toLowerCase(), startDate, endDate, sort, sortDesc, from, size);
        return new ResponseEntity<>(operationsDto, HttpStatus.OK);
    }

    @GetMapping("/expense/{operationId}")
    public ResponseEntity<ResponseOperation>  getOperationExpenseById(@RequestHeader(AUTHORIZATION) String token,
                                                                      @NotBlank @Email @RequestParam String email,
                                                               @PathVariable long operationId) {

        controllerUtil.validateTokenAndEmail(email, token);
        ResponseOperation operationDto = operationService.getOperationExpenseById(operationId, email.toLowerCase());
        return new ResponseEntity<>(operationDto, HttpStatus.OK);
    }

    @GetMapping("/income/{operationId}")
    public ResponseEntity<ResponseOperation>  getOperationIncomeById(@RequestHeader(AUTHORIZATION) String token,
                                                                     @NotBlank @Email @RequestParam String email,
                                                                     @PathVariable long operationId) {

        controllerUtil.validateTokenAndEmail(email, token);
        ResponseOperation operationDto = operationService.getOperationIncomeById(operationId, email.toLowerCase());
        return new ResponseEntity<>(operationDto, HttpStatus.OK);
    }

    @GetMapping("/moving/{operationId}")
    public ResponseEntity<ResponseOperationMoving>  getOperationMovingById(@RequestHeader(AUTHORIZATION) String token,
                                                                     @NotBlank @Email @RequestParam String email,
                                                                     @PathVariable long operationId) {

        controllerUtil.validateTokenAndEmail(email, token);
        ResponseOperationMoving operationDto = operationService.getOperationMovingById(operationId, email.toLowerCase());
        return new ResponseEntity<>(operationDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Create income transaction")
    @PostMapping("/income")
    ResponseEntity<ResponseOperation> addOperationIncome(
            @RequestBody @Validated(Created.class) OperationDto newOperation,
            @NotBlank @RequestParam @Email String email,
            @RequestHeader(AUTHORIZATION) String token) {

        controllerUtil.validateTokenAndEmail(email, token);
        ResponseOperation operationDto = operationService.addOperationIncome(newOperation, email.toLowerCase());

        return new ResponseEntity<>(operationDto, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Create expense transaction")
    @PostMapping("/expense")
    ResponseEntity<ResponseOperation> addOperationExpense(
            @RequestBody @Validated(Created.class) OperationDto newOperation,
            @NotBlank @RequestParam @Email String email,
            @RequestHeader(AUTHORIZATION) String token) {

        controllerUtil.validateTokenAndEmail(email, token);
        ResponseOperation operationDto = operationService.addOperationExpense(newOperation, email.toLowerCase());

        return new ResponseEntity<>(operationDto, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Create moving transaction")
    @PostMapping("/moving")
    ResponseEntity<ResponseOperationMoving> addOperationMoving(
            @RequestBody @Validated(Created.class) OperationMovingDto newOperation,
            @NotBlank @RequestParam @Email String email,
            @RequestHeader(AUTHORIZATION) String token) {

        controllerUtil.validateTokenAndEmail(email, token);
        ResponseOperationMoving operationDto = operationService.addOperationMoving(newOperation, email.toLowerCase());

        return new ResponseEntity<>(operationDto, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update income transaction")
    @PutMapping("/income")
    ResponseEntity<ResponseOperation> updateOperationIncome(
            @RequestBody @Valid OperationDto updateOperation,
            @NotBlank @RequestParam @Email String email,
            @RequestHeader(AUTHORIZATION) String token) {

        controllerUtil.validateTokenAndEmail(email, token);
        ResponseOperation operationDto = operationService.updateOperationIncome(updateOperation, email.toLowerCase());

        return new ResponseEntity<>(operationDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Update expense transaction")
    @PutMapping("/expense")
    ResponseEntity<ResponseOperation> updateOperationExpense(
            @RequestBody @Valid OperationDto updateOperation,
            @NotBlank @RequestParam @Email String email,
            @RequestHeader(AUTHORIZATION) String token) {

        controllerUtil.validateTokenAndEmail(email, token);
        ResponseOperation operationDto = operationService.updateOperationExpense(updateOperation, email.toLowerCase());

        return new ResponseEntity<>(operationDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete income transaction")
    @DeleteMapping("/income/{operationId}")
    ResponseEntity<?> deleteOperationIncome(
            @NotBlank @RequestParam @Email String email,
            @RequestHeader(AUTHORIZATION) String token,
            @PathVariable Long operationId) {

        controllerUtil.validateTokenAndEmail(email, token);
        operationService.deleteOperationIncomeById(operationId, email.toLowerCase());

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Delete expense transaction")
    @DeleteMapping("/expense/{operationId}")
    ResponseEntity<?> deleteOperationExpense(
            @NotBlank @RequestParam @Email String email,
            @RequestHeader(AUTHORIZATION) String token,
            @PathVariable Long operationId) {

        controllerUtil.validateTokenAndEmail(email, token);
        operationService.deleteOperationExpenseById(operationId, email.toLowerCase());

        return ResponseEntity.ok().build();
    }
}
