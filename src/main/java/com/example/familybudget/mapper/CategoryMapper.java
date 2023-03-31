package com.example.familybudget.mapper;

import com.example.familybudget.dto.CategoryDto;
import com.example.familybudget.dto.NewCategoryDto;
import com.example.familybudget.entity.CategoryExpense;
import com.example.familybudget.entity.CategoryIncome;
import com.example.familybudget.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    CategoryDto toCategoryDto(CategoryIncome category);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    CategoryDto toCategoryDto(CategoryExpense category);

    @Mapping(source = "newCategoryDto.name", target = "name")
    @Mapping(source = "user", target = "user")
    CategoryIncome toCategoryIncome(NewCategoryDto newCategoryDto, User user);

    @Mapping(source = "newCategoryDto.name", target = "name")
    @Mapping(source = "user", target = "user")
    CategoryExpense toCategoryExpense(NewCategoryDto newCategoryDto, User user);
}