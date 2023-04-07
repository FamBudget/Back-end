package com.example.familybudget.service;

import com.example.familybudget.dto.AccountDto;
import com.example.familybudget.dto.NewAccountDto;
import com.example.familybudget.entity.Account;
import com.example.familybudget.entity.User;
import com.example.familybudget.exception.ForbiddenException;
import com.example.familybudget.mapper.AccountMapper;
import com.example.familybudget.repository.AccountRepository;
import com.example.familybudget.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public List<AccountDto> getAccountsByEmail(String email, int from, int size) {
        User user = findUserByEmail(email);
        Pageable page = PageRequest.of(from / size, size);
        List<AccountDto> accountDtoList = accountRepository.findAllByUser(user, page)
                .stream().map(AccountMapper.INSTANCE::toAccountDto).collect(Collectors.toList());

        log.debug("Got accounts by user: {}. Accounts counts: {}", user, accountDtoList.size());
        return accountDtoList;
    }

    public AccountDto addAccount(NewAccountDto newAccountDto, String email) {
        User user = findUserByEmail(email);

        Account account = AccountMapper.INSTANCE.toAccount(newAccountDto, user);
        if (account.getCreatedOn() == null) {
            account.setCreatedOn(LocalDateTime.now());
        }
        account.setCurrency(user.getCurrency());
        AccountDto responseAccountDto = AccountMapper
                .INSTANCE.toAccountDto(accountRepository.save(account));
        log.debug("Added new account: {} by user: {}", account, user);
        return responseAccountDto;
    }

    public AccountDto getAccountById(long id, String email) {
        findUserByEmail(email);
        Account account = accountRepository.getById(id);
        if (!email.equals(account.getUser().getEmail())) {
            throw new ForbiddenException("This user can't get this account");
        }
        AccountDto accountDto = AccountMapper.INSTANCE.toAccountDto(account);

        log.debug("Got account {} by id", account);
        return accountDto;
    }

    public AccountDto updateAccount(AccountDto accountDto, String email) {
        findUserByEmail(email);
        Account account = accountRepository.getById(accountDto.getId());
        if (!email.equals(account.getUser().getEmail())) {
            throw new ForbiddenException("This user can't update this account");
        }
        account.setName(accountDto.getName());
        account.setAmount(accountDto.getAmount());
        AccountDto updatedAccountDto = AccountMapper.INSTANCE.toAccountDto(accountRepository.save(account));
        log.debug("Account: {} was updated", account.getId());
        return updatedAccountDto;
    }

    public void deleteAccountById(long id, String email) {
        findUserByEmail(email);
        Account account = accountRepository.getById(id);
        if (!email.equals(account.getUser().getEmail())) {
            throw new ForbiddenException("This user can't delete this account");
        }
        accountRepository.deleteById(id);
        log.debug("Account with id {} was deleted", id);
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
