package com.example.familybudget.service;

import com.example.familybudget.dto.OperationDto;
import com.example.familybudget.dto.ResponseOperation;
import com.example.familybudget.entity.*;
import com.example.familybudget.exception.ForbiddenException;
import com.example.familybudget.mapper.OperationMapper;
import com.example.familybudget.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<ResponseOperation> getOperationsExpense(String email,
                                                        LocalDateTime startDate,
                                                        LocalDateTime endDate,
                                                        SortParameter sort,
                                                        boolean sortDesc,
                                                        int from,
                                                        int size) {
        User user = findUserByEmail(email);

        String sortParameter = "";
        if (sort == SortParameter.DATE) {
            sortParameter = "createdOn";
        } else if (sort == SortParameter.CATEGORY) {
            sortParameter = "category.name";
        } else if (sort == SortParameter.AMOUNT) {
            sortParameter = "amount";
        } else if (sort == SortParameter.ACCOUNT) {
            sortParameter = "account.name";
        }
        Pageable page = PageRequest.of(from / size, size,
                sortDesc ? Sort.by(sortParameter).descending() :  Sort.by(sortParameter).ascending());

        LocalDateTime start = startDate == null ? LocalDateTime.now().minusYears(999) : startDate;
        LocalDateTime end = endDate == null ? LocalDateTime.now() : endDate;
        List<ResponseOperation> operationsDto = operationExpenseRepository
                .getAllByUserAndCreatedOn_MinAndCreatedOn_Max(user, start, end, page)
                .stream().map(OperationMapper.INSTANCE::toOperationDto).collect(Collectors.toList());
        log.debug("Got operations expense by user: {}. Operations counts: {}", user, operationsDto.size());
        return operationsDto;
    }

    public List<ResponseOperation> getOperationsIncome(String email,
                                                        LocalDateTime startDate,
                                                        LocalDateTime endDate,
                                                        SortParameter sort,
                                                        boolean sortDesc,
                                                        int from,
                                                        int size) {
        User user = findUserByEmail(email);

        String sortParameter = "";
        if (sort == SortParameter.DATE) {
            sortParameter = "createdOn";
        } else if (sort == SortParameter.CATEGORY) {
            sortParameter = "category.name";
        } else if (sort == SortParameter.AMOUNT) {
            sortParameter = "amount";
        } else if (sort == SortParameter.ACCOUNT) {
            sortParameter = "account.name";
        }
        Pageable page = PageRequest.of(from / size, size,
                sortDesc ? Sort.by(sortParameter).descending() :  Sort.by(sortParameter).ascending());

        LocalDateTime start = startDate == null ? LocalDateTime.now().minusYears(999) : startDate;
        LocalDateTime end = endDate == null ? LocalDateTime.now() : endDate;
        List<ResponseOperation> operationsDto = operationIncomeRepository
                .getAllByUserAndCreatedOn_MinAndCreatedOn_Max(user, start, end, page)
                .stream().map(OperationMapper.INSTANCE::toOperationDto).collect(Collectors.toList());
        log.debug("Got operations expense by user: {}. Operations counts: {}", user, operationsDto.size());
        return operationsDto;
    }

    @Transactional
    public ResponseOperation addOperationExpense(OperationDto newOperationDto, String email) {
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
        OperationExpense operationSaved = operationExpenseRepository.save(operationExpense);

        log.debug("Added new operation of expense: {} by user: {}", operationExpense, user);
        return OperationMapper.INSTANCE.toOperationDto(operationSaved);
    }

    public ResponseOperation getOperationExpenseById(long id, String email) {
        findUserByEmail(email);
        OperationExpense operation = operationExpenseRepository.getById(id);
        if (!email.equals(operation.getUser().getEmail())) {
            throw new ForbiddenException("This user can't get this operation");
        }
        ResponseOperation operationDto = OperationMapper.INSTANCE.toOperationDto(operation);

        log.debug("Got operation income {} by id", operation);
        return operationDto;
    }

    public ResponseOperation getOperationIncomeById(long id, String email) {
        findUserByEmail(email);
        OperationIncome operation = operationIncomeRepository.getById(id);
        if (!email.equals(operation.getUser().getEmail())) {
            throw new ForbiddenException("This user can't get this operation");
        }
        ResponseOperation operationDto = OperationMapper.INSTANCE.toOperationDto(operation);

        log.debug("Got operation income {} by id", operation);
        return operationDto;
    }

    @Transactional
    public ResponseOperation addOperationIncome(OperationDto newOperationDto, String email) {
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
        OperationIncome operationSaved = operationIncomeRepository.save(operationIncome);

        log.debug("Added new operation of income: {} by user: {}", operationIncome, user);
        return OperationMapper.INSTANCE.toOperationDto(operationSaved);
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
