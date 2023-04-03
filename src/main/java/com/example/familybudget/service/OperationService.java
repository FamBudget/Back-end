package com.example.familybudget.service;

import com.example.familybudget.dto.OperationDto;
import com.example.familybudget.entity.Account;
import com.example.familybudget.entity.CategoryExpense;
import com.example.familybudget.entity.OperationExpense;
import com.example.familybudget.entity.User;
import com.example.familybudget.exception.ForbiddenException;
import com.example.familybudget.mapper.OperationMapper;
import com.example.familybudget.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

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
        OperationExpense operationExpenseSaved = operationExpenseRepository.save(operationExpense);

        log.debug("Added new operation of expense: {} by user: {}", operationExpense, user);
        return OperationMapper.INSTANCE.toOperationDto(operationExpenseSaved);
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
