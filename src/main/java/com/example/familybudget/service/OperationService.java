package com.example.familybudget.service;

import com.example.familybudget.dto.OperationDto;
import com.example.familybudget.dto.OperationMovingDto;
import com.example.familybudget.dto.ResponseOperation;
import com.example.familybudget.dto.ResponseOperationMoving;
import com.example.familybudget.entity.*;
import com.example.familybudget.exception.ForbiddenException;
import com.example.familybudget.mapper.OperationMapper;
import com.example.familybudget.mapper.OperationMovingMapper;
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
    private final OperationMovingRepository operationMovingRepository;
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

    public List<ResponseOperationMoving> getOperationsMoving(String email,
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
        } else if (sort == SortParameter.AMOUNT) {
            sortParameter = "amount";
        } else if (sort == SortParameter.ACCOUNT_FROM) {
            sortParameter = "accountFrom.name";
        } else if (sort == SortParameter.ACCOUNT_TO) {
            sortParameter = "accountTo.name";
        }
        Pageable page = PageRequest.of(from / size, size,
                sortDesc ? Sort.by(sortParameter).descending() : Sort.by(sortParameter).ascending());

        LocalDateTime start = startDate == null ? LocalDateTime.now().minusYears(999) : startDate;
        LocalDateTime end = endDate == null ? LocalDateTime.now() : endDate;
        List<ResponseOperationMoving> operationsDto = operationMovingRepository
                .getAllByUserAndCreatedOn_MinAndCreatedOn_Max(user, start, end, page)
                .stream().map(OperationMovingMapper.INSTANCE::toOperationDto).collect(Collectors.toList());
        log.debug("Got operations expense by user: {}. Operations counts: {}", user, operationsDto.size());
        return operationsDto;
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

    public ResponseOperationMoving getOperationMovingById(long id, String email) {
        findUserByEmail(email);
        OperationMoving operation = operationMovingRepository.getById(id);
        if (!email.equals(operation.getUser().getEmail())) {
            throw new ForbiddenException("This user can't get this operation");
        }
        ResponseOperationMoving operationDto = OperationMovingMapper.INSTANCE.toOperationDto(operation);

        log.debug("Got operation moving {} by id", operation);
        return operationDto;
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

    @Transactional
    public ResponseOperationMoving addOperationMoving(OperationMovingDto newOperationDto, String email) {
        User user = findUserByEmail(email);

        Account accountFrom = accountRepository.getById(newOperationDto.getAccountFromId());
        String emailAccountFrom = accountFrom.getUser().getEmail();
        if (!email.equals(emailAccountFrom)) {
            throw new ForbiddenException("This account does not apply to this user");
        }

        Account accountTo = accountRepository.getById(newOperationDto.getAccountToId());
        String emailAccountTo = accountTo.getUser().getEmail();
        if (!email.equals(emailAccountTo)) {
            throw new ForbiddenException("This account does not apply to this user");
        }

        double newAmount = accountFrom.getAmount() - newOperationDto.getAmount();
        if (newAmount < 0) {
            throw new ForbiddenException("Not enough money in this account");
        }

        accountFrom.setAmount(newAmount);
        accountRepository.save(accountFrom);

        newAmount = accountTo.getAmount() + newOperationDto.getAmount();

        accountTo.setAmount(newAmount);
        accountRepository.save(accountTo);

        OperationMoving operationMoving = OperationMovingMapper.INSTANCE
                .toOperation(newOperationDto, accountFrom, accountTo, user);
        if (operationMoving.getCreatedOn() == null) {
            operationMoving.setCreatedOn(LocalDateTime.now());
        }
        OperationMoving operationSaved = operationMovingRepository.save(operationMoving);

        log.debug("Added new operation of moving: {} by user: {}", operationMoving, user);
        return OperationMovingMapper.INSTANCE.toOperationDto(operationSaved);
    }

    @Transactional
    public ResponseOperation updateOperationExpense(OperationDto operationDto, String email) {
        findUserByEmail(email);
        OperationExpense operation = operationExpenseRepository.getById(operationDto.getId());
        if (!email.equals(operation.getUser().getEmail())) {
            throw new ForbiddenException("This user can't update this operation");
        }

        Account oldAccount = operation.getAccount();
        Account account = accountRepository.getById(operationDto.getAccountId());
        String emailFromAccount = account.getUser().getEmail();
        if (!email.equals(emailFromAccount)) {
            throw new ForbiddenException("This account does not apply to this user");
        }

        CategoryExpense category = categoryExpenseRepository.getById(operationDto.getCategoryId());
        String emailFromCategory = category.getUser().getEmail();
        if (!email.equals(emailFromCategory)) {
            throw new ForbiddenException("This category does not apply to this user");
        }

        oldAccount.setAmount(oldAccount.getAmount() + operation.getAmount());

        double newAmount = account.getAmount() - operationDto.getAmount();
        if (newAmount < 0) {
            throw new ForbiddenException("Not enough money in this account");
        }

        account.setAmount(newAmount);

        operation.setAmount(operationDto.getAmount());

        if (!oldAccount.equals(account)) {
            accountRepository.save(oldAccount);
        }
        accountRepository.save(account);

        operation.setAccount(account);
        operation.setCategory(category);

        if (operationDto.getCreatedOn() != null) {
            operation.setCreatedOn(operationDto.getCreatedOn());
        }

        if (operationDto.getDescription() != null) {
            operation.setDescription(operationDto.getDescription());
        }

        ResponseOperation responseOperation = OperationMapper.INSTANCE
                .toOperationDto(operationExpenseRepository.save(operation));
        log.debug("Operation: {} was updated", operation.getId());
        return responseOperation;
    }

    @Transactional
    public ResponseOperation updateOperationIncome(OperationDto operationDto, String email) {
        findUserByEmail(email);
        OperationIncome operation = operationIncomeRepository.getById(operationDto.getId());
        if (!email.equals(operation.getUser().getEmail())) {
            throw new ForbiddenException("This user can't update this operation");
        }

        Account oldAccount = operation.getAccount();
        Account account = accountRepository.getById(operationDto.getAccountId());
        String emailFromAccount = account.getUser().getEmail();
        if (!email.equals(emailFromAccount)) {
            throw new ForbiddenException("This account does not apply to this user");
        }

        CategoryIncome category = categoryIncomeRepository.getById(operationDto.getCategoryId());
        String emailFromCategory = category.getUser().getEmail();
        if (!email.equals(emailFromCategory)) {
            throw new ForbiddenException("This category does not apply to this user");
        }

        account.setAmount(account.getAmount() + operationDto.getAmount());

        double newAmountForOldAccount = oldAccount.getAmount() - operation.getAmount();
        if (newAmountForOldAccount < 0) {
            throw new ForbiddenException("Not enough money in this account");
        }
        oldAccount.setAmount(newAmountForOldAccount);

        operation.setAmount(operationDto.getAmount());

        if (!oldAccount.equals(account)) {
            accountRepository.save(oldAccount);
        }
        accountRepository.save(account);

        operation.setAccount(account);
        operation.setCategory(category);

        if (operationDto.getCreatedOn() != null) {
            operation.setCreatedOn(operationDto.getCreatedOn());
        }

        if (operationDto.getDescription() != null) {
            operation.setDescription(operationDto.getDescription());
        }

        ResponseOperation responseOperation = OperationMapper.INSTANCE
                .toOperationDto(operationIncomeRepository.save(operation));
        log.debug("Operation: {} was updated", operation.getId());
        return responseOperation;
    }

    @Transactional
    public void deleteOperationExpenseById(long id, String email) {
        findUserByEmail(email);
        OperationExpense operation = operationExpenseRepository.getById(id);
        if (!email.equals(operation.getUser().getEmail())) {
            throw new ForbiddenException("This user can't delete this operation");
        }

        Account account = operation.getAccount();

        account.setAmount(account.getAmount() + operation.getAmount());
        operationExpenseRepository.deleteById(id);
        log.debug("Operation of expense with id {} was deleted", id);
        accountRepository.save(account);
        log.debug("An amount on the account: {} was updated", account.getId());
    }

    @Transactional
    public void deleteOperationIncomeById(long id, String email) {
        findUserByEmail(email);
        OperationIncome operation = operationIncomeRepository.getById(id);
        if (!email.equals(operation.getUser().getEmail())) {
            throw new ForbiddenException("This user can't delete this operation");
        }

        Account account = operation.getAccount();

        double newAmount = account.getAmount() - operation.getAmount();
        if (newAmount < 0) {
            throw new ForbiddenException("Not enough money in this account");
        }
        account.setAmount(newAmount);
        operationIncomeRepository.deleteById(id);
        log.debug("Operation of expense with id {} was deleted", id);
        accountRepository.save(account);
        log.debug("An amount on the account: {} was updated", account.getId());
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
