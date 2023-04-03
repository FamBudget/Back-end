package com.example.familybudget.repository;

import com.example.familybudget.entity.OperationIncome;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityNotFoundException;

public interface OperationIncomeRepository extends JpaRepository<OperationIncome, Long> {

    default OperationIncome getById(Long id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Operation income with id (%d) not found", id))
        );
    }
}
