package com.example.familybudget.repository;

import com.example.familybudget.entity.OperationExpense;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityNotFoundException;

public interface OperationExpenseRepository extends JpaRepository<OperationExpense, Long> {

    default OperationExpense getById(Long id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Operation expense with id (%d) not found", id))
        );
    }
}
