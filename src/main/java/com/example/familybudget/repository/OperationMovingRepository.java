package com.example.familybudget.repository;

import com.example.familybudget.entity.OperationMoving;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityNotFoundException;

public interface OperationMovingRepository extends JpaRepository<OperationMoving, Long> {

    default OperationMoving getById(Long id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Operation moving with id (%d) not found", id))
        );
    }
}
