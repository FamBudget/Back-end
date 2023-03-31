package com.example.familybudget.repository;

import com.example.familybudget.entity.CategoryIncome;
import com.example.familybudget.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface CategoryIncomeRepository extends JpaRepository<CategoryIncome, Long> {


    default CategoryIncome getById(Long id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Category with id (%d) not found", id))
        );
    }

    List<CategoryIncome> findAllByUser(User user, Pageable pageable);
}
