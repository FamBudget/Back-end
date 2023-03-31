package com.example.familybudget.repository;

import com.example.familybudget.entity.CategoryExpense;
import com.example.familybudget.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface CategoryExpenseRepository extends JpaRepository<CategoryExpense, Long> {


    default CategoryExpense getById(Long id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Category with id (%d) not found", id))
        );
    }

    List<CategoryExpense> findAllByUser(User user, Pageable pageable);
}
