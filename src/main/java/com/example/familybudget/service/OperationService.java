package com.example.familybudget.service;

import com.example.familybudget.dto.OperationDto;
import com.example.familybudget.entity.*;
import com.example.familybudget.exception.ForbiddenException;
import com.example.familybudget.mapper.OperationMapper;
import com.example.familybudget.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OperationService {

    private final OperationExpenseRepository operationExpenseRepository;
    private final OperationIncomeRepository operationIncomeRepository;
    private final AccountRepository accountRepository;
    private final CategoryIncomeRepository categoryIncomeRepository;
    private final CategoryExpenseRepository categoryExpenseRepository;
    private final UserRepository userRepository;

    @Transactional
    public OperationDto addOperationExpense(OperationDto newOperationDto, String email) {
        User user = findUserByEmail(email);
        CategoryExpense categoryExpense = categoryExpenseRepository.getById(newOperationDto.getCategoryId());
        String emailFromCategory = categoryExpense.getUser().getEmail();
        if (!email.equals(emailFromCategory)) {
            throw new ForbiddenException("This category expense does not apply to this user");
        }
        Account account = accountRepository.getById(newOperationDto.getAccountId());
        String emailFromAccount = account.getUser().getEmail();
        if (!email.equals(emailFromAccount)) {
            throw new ForbiddenException("This account does not apply to this user");
        }

        double newAmount = account.getAmount() - newOperationDto.getAmount();
        if (newAmount < 0) {
            throw new ForbiddenException("Not enough money in this account");
        }
        account.setAmount(newAmount);
        accountRepository.save(account);

        OperationExpense operationExpense = OperationMapper.INSTANCE
                .toOperationExpense(newOperationDto, categoryExpense, account, user);
        if (operationExpense.getCreatedOn() == null) {
            operationExpense.setCreatedOn(LocalDateTime.now());
        }
        OperationExpense operationExpenseSaved = operationExpenseRepository.save(operationExpense);

        log.debug("Added new operation of expense: {} by user: {}", operationExpense, user);
        return OperationMapper.INSTANCE.toOperationDto(operationExpenseSaved);
    }

    @Transactional
    public OperationDto addOperationIncome(OperationDto newOperationDto, String email) {
        User user = findUserByEmail(email);
        CategoryIncome categoryIncome = categoryIncomeRepository.getById(newOperationDto.getCategoryId());
        String emailFromCategory = categoryIncome.getUser().getEmail();
        if (!email.equals(emailFromCategory)) {
            throw new ForbiddenException("This category expense does not apply to this user");
        }
        Account account = accountRepository.getById(newOperationDto.getAccountId());
        String emailFromAccount = account.getUser().getEmail();
        if (!email.equals(emailFromAccount)) {
            throw new ForbiddenException("This account does not apply to this user");
        }

        double newAmount = account.getAmount() + newOperationDto.getAmount();

        account.setAmount(newAmount);
        accountRepository.save(account);

        OperationIncome operationIncome = OperationMapper.INSTANCE
                .toOperationIncome(newOperationDto, categoryIncome, account, user);
        if (operationIncome.getCreatedOn() == null) {
            operationIncome.setCreatedOn(LocalDateTime.now());
        }
        OperationIncome operationIncomeSaved = operationIncomeRepository.save(operationIncome);

        log.debug("Added new operation of income: {} by user: {}", operationIncome, user);
        return OperationMapper.INSTANCE.toOperationDto(operationIncomeSaved);
    }

    private User findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        log.debug("finding user by email: {}", email);
        if (user == null) {
            throw new EntityNotFoundException("User named " + email + " not found");
        }
        log.debug("user found");
        return user;
    }
}
