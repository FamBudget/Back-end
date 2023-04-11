package com.example.familybudget.repository;

import com.example.familybudget.entity.OperationMoving;
import com.example.familybudget.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

public interface OperationMovingRepository extends JpaRepository<OperationMoving, Long> {

    default OperationMoving getById(Long id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Operation moving with id (%d) not found", id))
        );
    }

    @Query("SELECT op FROM OperationMoving op JOIN FETCH op.accountFrom JOIN FETCH op.accountTo JOIN FETCH op.user WHERE " +
            "(op.createdOn >= :startDate) " +
            "AND (op.createdOn <= :endDate) " +
            "AND op.user = :user"
    )
    List<OperationMoving> getAllByUserAndCreatedOn_MinAndCreatedOn_Max(User user,
                                                                       LocalDateTime startDate,
                                                                       LocalDateTime endDate,
                                                                       Pageable pageable);
}
