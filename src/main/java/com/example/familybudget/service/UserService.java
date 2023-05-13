package com.example.familybudget.service;

import com.example.familybudget.dto.NewPasswordRequest;
import com.example.familybudget.dto.ResponseUserSecurityStatus;
import com.example.familybudget.dto.UpdateUserRequest;
import com.example.familybudget.dto.UserDto;
import com.example.familybudget.entity.*;
import com.example.familybudget.exception.ForbiddenException;
import com.example.familybudget.mapper.UserMapper;
import com.example.familybudget.repository.AccountRepository;
import com.example.familybudget.repository.CategoryExpenseRepository;
import com.example.familybudget.repository.CategoryIncomeRepository;
import com.example.familybudget.repository.UserRepository;
import com.example.familybudget.service.util.EmailProvider;
import com.example.familybudget.service.util.GmailProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {


    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailProvider emailProvider;
    private final GmailProvider gmailProvider;
    private final CategoryIncomeRepository categoryIncomeRepository;
    private final CategoryExpenseRepository categoryExpenseRepository;
    private final List<String> categoryIncomeList = List.of(
            "Заработная плата",
            "Подработка",
            "Проценты по вкладу");
    private final List<String> categoryExpenseList = List.of(
            "Продукты",
            "Еда вне дома",
            "Дом",
            "Транспорт",
            "Сотовая связь",
            "Одежда",
            "Медицина",
            "Развлечения",
            "Другое");

    public void registerUser(User user) throws Exception {

        user.setRole(Role.USER);
        user.setStatus(Status.NOT_ACTIVE);
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        String serverIP = getIp();

        String activationLink = "http://" + serverIP + ":8080/activate/" + user.getActivationCode();

        String message = String.format(
                "Hello, %s! \n" +
                        "Welcome to Family Budget. Please, visit next link: %s",
                user.getEmail(), activationLink);
        gmailProvider.sendMail(user.getEmail(), "Activation Code", message);
    }

    public User findByEmailAndPassword(String email, String password) {
        User user = findByEmail(email);
        log.debug("finding user by email {} and password {}", email, password);
        if (passwordEncoder.matches(password, user.getPassword())) {
            log.debug("user found");
            return user;
        } else {
            throw new EntityNotFoundException("There is no " + email + " in the database with this password");
        }
    }

    public UserDto getUserByEmail(String email) {
        User user = findByEmail(email);
        log.debug("Got user {} by email", user);
        return UserMapper.INSTANCE.toUserDto(user);
    }

    public void deleteUserByEmail(String email) {
        User user = findByEmail(email);
        userRepository.deleteById(user.getId());
        log.debug("User {} was deleted", email);
    }

    public ResponseUserSecurityStatus activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null) {
            throw new EntityNotFoundException("Activation code not found");
        }
        user.setActivationCode(null);
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);

        log.debug("user {} was activated: ", user);
        List<CategoryIncome> listIncome = new ArrayList<>();
        List<CategoryExpense> listExpense = new ArrayList<>();

        for (String category: categoryIncomeList) {
            listIncome.add(new CategoryIncome(category, user));
        }

        for (String category: categoryExpenseList) {
            listExpense.add(new CategoryExpense(category, user));
        }

        categoryIncomeRepository.saveAll(listIncome);
        log.debug("Added new income categories for user: {}", user);
        categoryExpenseRepository.saveAll(listExpense);
        log.debug("Added new expense categories for user: {}", user);
        Account account = new Account();
        account.setAmount(0.0);
        account.setStartAmount(0.0);
        account.setCurrency(user.getCurrency());
        account.setUser(user);
        account.setName("Наличные");
        account.setCreatedOn(LocalDateTime.now());
        accountRepository.save(account);
        log.debug("Added new account {} for user: {}", account, user);
        ResponseUserSecurityStatus result = new ResponseUserSecurityStatus();
        result.setEmail(user.getEmail());
        result.setStatus("User activated");
        return result;
    }

    public void requestResetPassword(String email) {
        User user = findByEmail(email);
        log.debug("repairing user password by email {}", email);

        user.setActivationCode(UUID.randomUUID().toString());
        userRepository.save(user);

        String serverIP = getIp();

        String resetPasswordLink = "http://" + serverIP + ":8080/reset-password";

        String message = String.format(
                "Hello, %s! \n" +
                        "Follow the link to reset your password. If you did not make this request, then simply ignore this letter: %s/%s",
                email, resetPasswordLink, user.getActivationCode() + "?email=" + email);
        emailProvider.send(user.getEmail(), "repair password", message);
        log.debug("sending new password typing link");
    }

    public ResponseUserSecurityStatus verifyCode(String email, String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null) {
            throw new EntityNotFoundException("Activation code not found");
        }

        if (!email.equals(user.getEmail())) {
            throw new ForbiddenException("This code does not apply to this user");
        }

        ResponseUserSecurityStatus responseUserSecurityStatus = new ResponseUserSecurityStatus();
        responseUserSecurityStatus.setStatus("success");
        responseUserSecurityStatus.setEmail(user.getEmail());

        log.debug("starting the user password reset process {}: ", user.getEmail());
        return responseUserSecurityStatus;
    }

    public ResponseUserSecurityStatus repairPassword(String email, String code, NewPasswordRequest passwordRequest) {
        User user = userRepository.findByActivationCode(code);
        if (user == null) {
            throw new EntityNotFoundException("Activation code not found");
        }
        if (!email.equals(user.getEmail())) {
            throw new ForbiddenException("This code does not apply to this user");
        }

        user.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
        user.setActivationCode(null);
        userRepository.save(user);

        ResponseUserSecurityStatus responseUserSecurityStatus = new ResponseUserSecurityStatus();
        responseUserSecurityStatus.setStatus("success");
        responseUserSecurityStatus.setEmail(user.getEmail());

        log.debug("User password was changed: ");
        return responseUserSecurityStatus;
    }

    public ResponseUserSecurityStatus changePassword(String email, NewPasswordRequest passwordRequest) {

        User user = findByEmail(email);

        user.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
        userRepository.save(user);

        ResponseUserSecurityStatus responseUserSecurityStatus = new ResponseUserSecurityStatus();
        responseUserSecurityStatus.setStatus("success");
        responseUserSecurityStatus.setEmail(user.getEmail());

        log.debug("User password was changed: ");
        return responseUserSecurityStatus;
    }

    public UserDto updateUser(String email, UpdateUserRequest updateUser) {
        User user = findByEmail(email);
        if (updateUser.getEmail() != null) {
            user.setEmail(updateUser.getEmail());
        }
        if (updateUser.getFirstName() != null) {
            user.setFirstName(updateUser.getFirstName());
        }
        if (updateUser.getLastName() != null) {
            user.setLastName(updateUser.getLastName());
        }
        userRepository.save(user);
        log.debug("Update user {}", user);
        return UserMapper.INSTANCE.toUserDto(user);
    }

    private String getIp() {
        String serverIP;
        try {
            serverIP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            serverIP = "localhost";
        }
        return serverIP;
    }

    private User findByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        log.debug("finding user by email: {}", email);
        if (user == null) {
            throw new EntityNotFoundException("User named " + email + " not found");
        }
        log.debug("user found");
        return user;
    }
}
