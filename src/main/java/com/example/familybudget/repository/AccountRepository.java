package com.example.familybudget.repository;

import com.example.familybudget.entity.Account;
import com.example.familybudget.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    default Account getById(Long id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Account with id (%d) not found", id))
        );
    }

    List<Account> findAllByUser(User user, Pageable pageable);
}
