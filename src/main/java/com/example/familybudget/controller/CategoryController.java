package com.example.familybudget.controller;

import com.example.familybudget.controller.util.ControllerUtil;
import com.example.familybudget.dto.CategoryDto;
import com.example.familybudget.dto.NewCategoryDto;
import com.example.familybudget.service.CategoryService;
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
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final ControllerUtil controllerUtil;
    private static final String AUTHORIZATION = "Authorization";

    @ApiOperation(value = "Get income categories by user email",
            notes = "This code is an endpoint for retrieving income categories by user email. " +
                    "It takes in several parameters, including an authorization token, the user's email," +
                    " a starting index (from) for pagination, and a maximum (size) number of results to return.")
    @GetMapping("/income")
    public ResponseEntity<List<CategoryDto>> getCategoriesIncome(
            @RequestHeader(AUTHORIZATION) String token,
            @Email @RequestParam String email,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        controllerUtil.validateTokenAndEmail(email, token);
        List<CategoryDto> categoryIncomeList = categoryService.getCategoriesIncomeByUserId(email, from, size);
        return new ResponseEntity<>(categoryIncomeList, HttpStatus.OK);
    }

    @ApiOperation(value = "Get expense categories by user email",
            notes = "This code is an endpoint for retrieving income categories by user email. " +
                    "It takes in several parameters, including an authorization token, the user's email," +
                    " a starting index (from) for pagination, and a maximum (size) number of results to return.")
    @GetMapping("/expense")
    public ResponseEntity<List<CategoryDto>> getCategoriesExpense(
            @RequestHeader(AUTHORIZATION) String token,
            @Email @RequestParam String email,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        controllerUtil.validateTokenAndEmail(email, token);
        List<CategoryDto> categoryExpenseList = categoryService.getCategoriesExpenseByUserId(email, from, size);
        return new ResponseEntity<>(categoryExpenseList, HttpStatus.OK);
    }

    @ApiOperation(value = "Create a new income category",
            notes = "Creates a new income category for the user with the specified email.\n\n" +
                    "The email should be provided as a request parameter and the category details should be provided in the request body.\n\n" +
                    "If the email provided in the request parameter does not match the email in the JWT token, a ForbiddenException will be thrown.\n\n" +
                    "Returns the created category.")
    @PostMapping("/income")
    public ResponseEntity<CategoryDto> addCategoriesIncome(
            @RequestHeader(AUTHORIZATION) String token,
            @Email @RequestParam String email,
            @Valid @RequestBody NewCategoryDto newCategoryDto) {

        controllerUtil.validateTokenAndEmail(email, token);
        CategoryDto categoryDto = categoryService.addCategoryIncome(newCategoryDto, email);

        return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Create a new expense category",
            notes = "Creates a new expense category for the user with the specified email.\n\n" +
                    "The email should be provided as a request parameter and the category details should be provided in the request body.\n\n" +
                    "If the email provided in the request parameter does not match the email in the JWT token, a ForbiddenException will be thrown.\n\n" +
                    "Returns the created category.")
    @PostMapping("/expense")
    public ResponseEntity<CategoryDto> addCategoriesExpense(
            @RequestHeader(AUTHORIZATION) String token,
            @Email @RequestParam String email,
            @Valid @RequestBody NewCategoryDto newCategoryDto) {

        controllerUtil.validateTokenAndEmail(email, token);
        CategoryDto categoryDto = categoryService.addCategoryExpense(newCategoryDto, email);

        return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
    }

    @GetMapping("/income/{categoryId}")
    public ResponseEntity<CategoryDto>  getCategoryIncomeById(@RequestHeader(AUTHORIZATION) String token,
                                                @Email @RequestParam String email,
                                                @PathVariable long categoryId) {

        controllerUtil.validateTokenAndEmail(email, token);
        CategoryDto categoryDto = categoryService.getCategoryIncomeById(categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @GetMapping("/expense/{categoryId}")
    public ResponseEntity<CategoryDto>  getCategoryExpenseById(@RequestHeader(AUTHORIZATION) String token,
                                                              @Email @RequestParam String email,
                                                              @PathVariable long categoryId) {

        controllerUtil.validateTokenAndEmail(email, token);
        CategoryDto categoryDto = categoryService.getCategoryExpenseById(categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Update an existing income category",
            notes = "Updates an existing income category for the user with the specified email.\n\n" +
                    "The email should be provided as a request parameter and the updated category details should be provided in the request body.\n\n" +
                    "If the email provided in the request parameter does not match the email in the JWT token, a ForbiddenException will be thrown.\n\n" +
                    "Returns the updated category.")
    @PutMapping("/income")
    public ResponseEntity<CategoryDto> updateCategoriesIncome(
            @RequestHeader(AUTHORIZATION) String token,
            @Email @RequestParam String email,
            @Valid @RequestBody CategoryDto updateCategoryDto) {

        controllerUtil.validateTokenAndEmail(email, token);
        CategoryDto categoryDto = categoryService.updateCategoryIncome(updateCategoryDto, email);

        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Update an existing expense category",
            notes = "Updates an existing expense category for the user with the specified email.\n\n" +
                    "The email should be provided as a request parameter and the updated category details should be provided in the request body.\n\n" +
                    "If the email provided in the request parameter does not match the email in the JWT token, a ForbiddenException will be thrown.\n\n" +
                    "Returns the updated category.")
    @PutMapping("/expense")
    public ResponseEntity<CategoryDto> updateCategoriesExpense(
            @RequestHeader(AUTHORIZATION) String token,
            @Email @RequestParam String email,
            @Valid @RequestBody CategoryDto updateCategoryDto) {

        controllerUtil.validateTokenAndEmail(email, token);
        CategoryDto categoryDto = categoryService.updateCategoryExpense(updateCategoryDto, email);

        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete an existing income category",
            notes = "Deletes an existing income category for the user with the specified email.\n\n" +
                    "The email should be provided as a request parameter and the category ID should be provided as a path variable.\n\n" +
                    "If the email provided in the request parameter does not match the email in the JWT token, a ForbiddenException will be thrown.\n\n" +
                    "Returns a ResponseEntity with a status code of 200 OK.")
    @DeleteMapping("/income/{categoryId}")
    public ResponseEntity<?> deleteCategoriesIncome(
            @RequestHeader(AUTHORIZATION) String token,
            @Email @RequestParam String email,
            @PathVariable Long categoryId) {

        controllerUtil.validateTokenAndEmail(email, token);
        categoryService.deleteCategoryIncomeById(categoryId, email);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Delete an existing expense category",
            notes = "Deletes an existing expense category for the user with the specified email.\n\n" +
                    "The email should be provided as a request parameter and the category ID should be provided as a path variable.\n\n" +
                    "If the email provided in the request parameter does not match the email in the JWT token, a ForbiddenException will be thrown.\n\n" +
                    "Returns a ResponseEntity with a status code of 200 OK.")
    @DeleteMapping("/expense/{categoryId}")
    public ResponseEntity<?> deleteCategoriesExpense(
            @RequestHeader(AUTHORIZATION) String token,
            @Email @RequestParam String email,
            @PathVariable Long categoryId) {

        controllerUtil.validateTokenAndEmail(email, token);
        categoryService.deleteCategoryExpenseById(categoryId, email);

        return ResponseEntity.ok().build();
    }
}
