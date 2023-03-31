package com.example.familybudget.controller;

import com.example.familybudget.dto.CategoryDto;
import com.example.familybudget.dto.NewCategoryDto;
import com.example.familybudget.exception.ForbiddenException;
import com.example.familybudget.security.JwtProvider;
import com.example.familybudget.service.CategoryService;
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
    private final JwtProvider jwtProvider;
    private static final String AUTHORIZATION = "Authorization";

    @GetMapping("/income")
    public ResponseEntity<List<CategoryDto>> getCategoriesIncome(
            @RequestHeader(AUTHORIZATION) String token,
            @Email @RequestParam String email,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        String emailDec = email.contains("%40") ? email.replace("%40" , "@"): email;
        String emailJwt = jwtProvider.getEmailFromToken(token.substring(7));
        if (!emailDec.equals(emailJwt)) {
            throw new ForbiddenException("The email from the requestBody does not match the email from the token");
        }
        List<CategoryDto> categoryIncomeList = categoryService.getCategoriesIncomeByUserId(email, from, size);
        return new ResponseEntity<>(categoryIncomeList, HttpStatus.OK);
    }

    @GetMapping("/expense")
    public ResponseEntity<List<CategoryDto>> getCategoriesExpense(
            @RequestHeader(AUTHORIZATION) String token,
            @Email @RequestParam String email,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        String emailDec = email.contains("%40") ? email.replace("%40" , "@"): email;
        String emailJwt = jwtProvider.getEmailFromToken(token.substring(7));
        if (!emailDec.equals(emailJwt)) {
            throw new ForbiddenException("The email from the requestBody does not match the email from the token");
        }
        List<CategoryDto> categoryExpenseList = categoryService.getCategoriesExpenseByUserId(email, from, size);
        return new ResponseEntity<>(categoryExpenseList, HttpStatus.OK);
    }

    @PostMapping("/income")
    public ResponseEntity<CategoryDto> addCategoriesIncome(
            @RequestHeader(AUTHORIZATION) String token,
            @Email @RequestParam String email,
            @Valid @RequestBody NewCategoryDto newCategoryDto) {

        String emailDec = email.contains("%40") ? email.replace("%40" , "@"): email;
        String emailJwt = jwtProvider.getEmailFromToken(token.substring(7));
        if (!emailDec.equals(emailJwt)) {
            throw new ForbiddenException("The email from the requestBody does not match the email from the token");
        }
        CategoryDto categoryDto = categoryService.addCategoryIncome(newCategoryDto, email);

        return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
    }

    @PostMapping("/expense")
    public ResponseEntity<CategoryDto> addCategoriesExpense(
            @RequestHeader(AUTHORIZATION) String token,
            @Email @RequestParam String email,
            @Valid @RequestBody NewCategoryDto newCategoryDto) {

        String emailDec = email.contains("%40") ? email.replace("%40" , "@"): email;
        String emailJwt = jwtProvider.getEmailFromToken(token.substring(7));
        if (!emailDec.equals(emailJwt)) {
            throw new ForbiddenException("The email from the requestBody does not match the email from the token");
        }
        CategoryDto categoryDto = categoryService.addCategoryExpense(newCategoryDto, email);

        return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
    }

    @PutMapping("/income")
    public ResponseEntity<CategoryDto> updateCategoriesIncome(
            @RequestHeader(AUTHORIZATION) String token,
            @Email @RequestParam String email,
            @Valid @RequestBody CategoryDto updateCategoryDto) {

        String emailDec = email.contains("%40") ? email.replace("%40" , "@"): email;
        String emailJwt = jwtProvider.getEmailFromToken(token.substring(7));
        if (!emailDec.equals(emailJwt)) {
            throw new ForbiddenException("The email from the requestBody does not match the email from the token");
        }
        CategoryDto categoryDto = categoryService.updateCategoryIncome(updateCategoryDto, email);

        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @PutMapping("/expense")
    public ResponseEntity<CategoryDto> updateCategoriesExpense(
            @RequestHeader(AUTHORIZATION) String token,
            @Email @RequestParam String email,
            @Valid @RequestBody CategoryDto updateCategoryDto) {

        String emailDec = email.contains("%40") ? email.replace("%40" , "@"): email;
        String emailJwt = jwtProvider.getEmailFromToken(token.substring(7));
        if (!emailDec.equals(emailJwt)) {
            throw new ForbiddenException("The email from the requestBody does not match the email from the token");
        }
        CategoryDto categoryDto = categoryService.updateCategoryExpense(updateCategoryDto, email);

        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @DeleteMapping("/income/{categoryId}")
    public ResponseEntity<?> deleteCategoriesIncome(
            @RequestHeader(AUTHORIZATION) String token,
            @Email @RequestParam String email,
            @PathVariable Long categoryId) {

        String emailDec = email.contains("%40") ? email.replace("%40" , "@"): email;
        String emailJwt = jwtProvider.getEmailFromToken(token.substring(7));
        if (!emailDec.equals(emailJwt)) {
            throw new ForbiddenException("The email from the requestBody does not match the email from the token");
        }
        categoryService.deleteCategoryIncomeById(categoryId, email);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/expense/{categoryId}")
    public ResponseEntity<?> deleteCategoriesExpense(
            @RequestHeader(AUTHORIZATION) String token,
            @Email @RequestParam String email,
            @PathVariable Long categoryId) {

        String emailDec = email.contains("%40") ? email.replace("%40" , "@"): email;
        String emailJwt = jwtProvider.getEmailFromToken(token.substring(7));
        if (!emailDec.equals(emailJwt)) {
            throw new ForbiddenException("The email from the requestBody does not match the email from the token");
        }
        categoryService.deleteCategoryExpenseById(categoryId, email);

        return ResponseEntity.ok().build();
    }
}
