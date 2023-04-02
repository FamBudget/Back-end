package com.example.familybudget.service;

import com.example.familybudget.dto.CategoryDto;
import com.example.familybudget.dto.NewCategoryDto;
import com.example.familybudget.entity.CategoryExpense;
import com.example.familybudget.entity.CategoryIncome;
import com.example.familybudget.entity.User;
import com.example.familybudget.exception.ForbiddenException;
import com.example.familybudget.mapper.CategoryMapper;
import com.example.familybudget.repository.CategoryExpenseRepository;
import com.example.familybudget.repository.CategoryIncomeRepository;
import com.example.familybudget.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryIncomeRepository categoryIncomeRepository;
    private final CategoryExpenseRepository categoryExpenseRepository;
    private final UserRepository userRepository;

    public List<CategoryDto> getCategoriesExpenseByUserId(String email, int from, int size) {
        User user = findUserByEmail(email);
        Pageable page = PageRequest.of(from / size, size);
        List<CategoryDto> categoriesDto = categoryExpenseRepository.findAllByUser(user, page)
                .stream().map(CategoryMapper.INSTANCE::toCategoryDto).collect(Collectors.toList());

        log.debug("Got categories by user: {}. Categories counts: {}", user, categoriesDto.size());
        return categoriesDto;
    }

    public List<CategoryDto> getCategoriesIncomeByUserId(String email, int from, int size) {
        User user = findUserByEmail(email);
        Pageable page = PageRequest.of(from / size, size);
        List<CategoryDto> categoriesDto = categoryIncomeRepository.findAllByUser(user, page)
                .stream().map(CategoryMapper.INSTANCE::toCategoryDto).collect(Collectors.toList());

        log.debug("Got categories by user: {}. Categories counts: {}", user, categoriesDto.size());
        return categoriesDto;
    }

    public CategoryDto getCategoryIncomeById(long id) {
        CategoryIncome category = categoryIncomeRepository.getById(id);
        CategoryDto categoryDto = CategoryMapper.INSTANCE.toCategoryDto(category);

        log.debug("Got category income {} by id", category);
        return categoryDto;
    }

    public CategoryDto getCategoryExpenseById(long id) {
        CategoryExpense category = categoryExpenseRepository.getById(id);
        CategoryDto categoryDto = CategoryMapper.INSTANCE.toCategoryDto(category);

        log.debug("Got category expense {} by id", category);
        return categoryDto;
    }

    public CategoryDto addCategoryIncome(NewCategoryDto categoryDto, String email) {
        User user = findUserByEmail(email);
        CategoryIncome category = CategoryMapper.INSTANCE.toCategoryIncome(categoryDto, user);
        categoryIncomeRepository.save(category);
        log.debug("Added new category of income: {} by user: {}", category, user);
        return CategoryMapper.INSTANCE.toCategoryDto(category);
    }

    public CategoryDto addCategoryExpense(NewCategoryDto categoryDto, String email) {
        User user = findUserByEmail(email);
        CategoryExpense category = CategoryMapper.INSTANCE.toCategoryExpense(categoryDto, user);
        categoryExpenseRepository.save(category);
        log.debug("Added new category of expense: {} by user: {}", category, user);
        return CategoryMapper.INSTANCE.toCategoryDto(category);
    }

    public CategoryDto updateCategoryIncome(CategoryDto categoryDto, String email) {
        User user = findUserByEmail(email);
        CategoryIncome category = categoryIncomeRepository.getById(categoryDto.getId());
        if (!user.getId().equals(category.getUser().getId())) {
            throw new ForbiddenException("This user can't update this category");
        }
        category.setName(categoryDto.getName());
        CategoryDto categoryDtoResponse = CategoryMapper.INSTANCE.toCategoryDto(categoryIncomeRepository.save(category));
        log.debug("Category of income: {} was updated", category.getId());
        return categoryDtoResponse;
    }

    public CategoryDto updateCategoryExpense(CategoryDto categoryDto, String email) {
        User user = findUserByEmail(email);
        CategoryExpense category = categoryExpenseRepository.getById(categoryDto.getId());
        if (!user.getId().equals(category.getUser().getId())) {
            throw new ForbiddenException("This user can't update this category");
        }
        category.setName(categoryDto.getName());
        CategoryDto categoryDtoResponse = CategoryMapper.INSTANCE.toCategoryDto(categoryExpenseRepository.save(category));
        log.debug("Category of income: {} was updated", category.getId());
        return categoryDtoResponse;
    }

    public void deleteCategoryIncomeById(long id, String email) {
        User user = findUserByEmail(email);
        CategoryIncome category = categoryIncomeRepository.getById(id);
        if (!user.getId().equals(category.getUser().getId())) {
            throw new ForbiddenException("This user can't delete this category");
        }
        categoryIncomeRepository.deleteById(id);
        log.debug("Category of income with id ({}) was deleted", id);
    }

    public void deleteCategoryExpenseById(long id, String email) {
        User user = findUserByEmail(email);
        CategoryExpense category = categoryExpenseRepository.getById(id);
        if (!user.getId().equals(category.getUser().getId())) {
            throw new ForbiddenException("This user can't delete this category");
        }
        categoryExpenseRepository.deleteById(id);
        log.debug("Category of expense with id ({}) was deleted", id);
    }

    private User findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        log.debug("finding user by email: {}", email);
        if (user == null) {
            throw new EntityNotFoundException("User named " + email + " not found");
        }
        log.debug("user found");
        return user;
    }
}
