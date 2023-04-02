package com.example.familybudget.repository;

import com.example.familybudget.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityNotFoundException;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByActivationCode(String code);
    User findUserByEmail(String email);

    default User getById(Long id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id (%d) not found", id))
        );
    }
}
