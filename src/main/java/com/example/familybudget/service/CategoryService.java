package com.example.familybudget.service;

import com.example.familybudget.dto.CategoryDto;
import com.example.familybudget.dto.NewCategoryDto;
import com.example.familybudget.entity.CategoryExpense;
import com.example.familybudget.entity.CategoryIncome;
import com.example.familybudget.entity.User;
import com.example.familybudget.mapper.CategoryMapper;
import com.example.familybudget.repository.CategoryExpenseRepository;
import com.example.familybudget.repository.CategoryIncomeRepository;
import com.example.familybudget.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryIncomeRepository categoryIncomeRepository;
    private final CategoryExpenseRepository categoryExpenseRepository;
    private final UserRepository userRepository;

    public CategoryDto addCategoryIncome(NewCategoryDto categoryDto, Long userId) {
        User user = userRepository.getById(userId);
        CategoryIncome category = CategoryMapper.INSTANCE.toCategoryIncome(categoryDto, user);
        categoryIncomeRepository.save(category);
        log.debug("Added new category of income: {} by user: {}", category, user);
        return CategoryMapper.INSTANCE.toCategoryDto(category);
    }

    public CategoryDto addCategoryExpense(NewCategoryDto categoryDto, Long userId) {
        User user = userRepository.getById(userId);
        CategoryExpense category = CategoryMapper.INSTANCE.toCategoryExpense(categoryDto, user);
        categoryExpenseRepository.save(category);
        log.debug("Added new category of expense: {} by user: {}", category, user);
        return CategoryMapper.INSTANCE.toCategoryDto(category);
    }

    public CategoryDto updateCategoryIncome(CategoryDto categoryDto) {
        CategoryIncome category = categoryIncomeRepository.getById(categoryDto.getId());
        category.setName(categoryDto.getName());
        CategoryDto categoryDtoResponse = CategoryMapper.INSTANCE.toCategoryDto(categoryIncomeRepository.save(category));
        log.debug("Category of income: {} was updated", category.getId());
        return categoryDtoResponse;
    }

    public CategoryDto updateCategoryExpense(CategoryDto categoryDto) {
        CategoryExpense category = categoryExpenseRepository.getById(categoryDto.getId());
        category.setName(categoryDto.getName());
        CategoryDto categoryDtoResponse = CategoryMapper.INSTANCE.toCategoryDto(categoryExpenseRepository.save(category));
        log.debug("Category of income: {} was updated", category.getId());
        return categoryDtoResponse;
    }

    public void deleteCategoryIncomeById(long id) {
        categoryIncomeRepository.getById(id);
        categoryIncomeRepository.deleteById(id);
        log.debug("Category of income with id ({}) was deleted", id);
    }

    public void deleteCategoryExpenseById(long id) {
        categoryExpenseRepository.getById(id);
        categoryExpenseRepository.deleteById(id);
        log.debug("Category of expense with id ({}) was deleted", id);
    }
}
