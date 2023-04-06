package com.example.familybudget.repository;

import com.example.familybudget.entity.OperationIncome;
import com.example.familybudget.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

public interface OperationIncomeRepository extends JpaRepository<OperationIncome, Long> {

    default OperationIncome getById(Long id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Operation income with id (%d) not found", id))
        );
    }

    @Query("SELECT op FROM OperationExpense op JOIN FETCH op.category JOIN FETCH op.account JOIN FETCH op.user WHERE " +
            "(op.createdOn >= :startDate) " +
            "AND (op.createdOn <= :endDate) " +
            "AND op.user = :user"
    )
    List<OperationIncome> getAllByUserAndCreatedOn_MinAndCreatedOn_Max(User user,
                                                                        LocalDateTime startDate,
                                                                        LocalDateTime endDate,
                                                                        Pageable pageable);
}
