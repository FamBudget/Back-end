package com.example.familybudget.repository;

import com.example.familybudget.entity.CategoryExpense;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityNotFoundException;

public interface CategoryExpenseRepository extends JpaRepository<CategoryExpense, Long> {


    default CategoryExpense getById(Long id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Category of expense with id (" + id + ") not found"))
        );
    }


}
