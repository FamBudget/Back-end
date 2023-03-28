package com.example.familybudget.service;

import com.example.familybudget.entity.Role;
import com.example.familybudget.entity.Status;
import com.example.familybudget.entity.User;
import com.example.familybudget.repository.UserRepository;
import com.example.familybudget.service.util.EmailProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailProvider emailProvider;

    @Value("${link.to.email}")
    private String link;

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User registerUser(User user) {

        user.setRole(Role.USER);
        user.setStatus(Status.NOT_ACTIVE);
        user.setActivationCode(UUID.randomUUID().toString());
        addUser(user);
        String message = String.format(
                "Hello, %s! \n" +
                        "Welcome to Family Budget. Please, visit next link: %s/%s",
                user.getEmail(), link, user.getActivationCode());
        emailProvider.send(user.getEmail(), "Activation Code", message);
        return user;
    }

    public User findByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        log.debug("finding user by email: {}", email);
        if (user == null) {
            throw new EntityNotFoundException("User named " + email + " not found");
        }
        log.debug("user found");
        return user;
    }

    public User findByEmailAndPassword(String email, String password) {
        User user = findByEmail(email);
        log.debug("finding user by email {} and password {}", email, password);
        if (user != null) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                log.debug("user found");
                return user;
            } else {
                throw new EntityNotFoundException("There is no " + email + " in the database with this password");
            }
        }
        return null;
    }

    public void activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null) {
            throw new EntityNotFoundException("Activation code not found");
        }
        user.setActivationCode(null);
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
    }
}
