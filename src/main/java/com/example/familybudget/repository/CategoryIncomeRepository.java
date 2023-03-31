package com.example.familybudget.repository;

import com.example.familybudget.entity.CategoryIncome;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityNotFoundException;

public interface CategoryIncomeRepository extends JpaRepository<CategoryIncome, Long> {


    default CategoryIncome getById(Long id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Category of income with id (" + id + ") not found"))
        );
    }
}
