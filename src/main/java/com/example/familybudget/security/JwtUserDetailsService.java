package com.example.familybudget.security;

import com.example.familybudget.entity.Role;
import com.example.familybudget.entity.User;
import com.example.familybudget.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public JwtUserDetails loadUserByUsername(String email) {
        log.debug("finding user by email: {}", email);
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new EntityNotFoundException("User named " + email + " not found");
        }
        log.debug("user found");
        user.setRole(Role.USER);
        return JwtUserDetails.fromUserToJwtUserDetails(user);
    }
}