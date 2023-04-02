package com.example.familybudget.mapper;

import com.example.familybudget.dto.AccountDto;
import com.example.familybudget.dto.NewAccountDto;
import com.example.familybudget.entity.Account;
import com.example.familybudget.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "currency", target = "currency")
    @Mapping(source = "createdOn", target = "createdOn")
    AccountDto toAccountDto(Account account);

    @Mapping(source = "accountDto.amount", target = "amount")
    @Mapping(source = "accountDto.name", target = "name")
    @Mapping(source = "accountDto.currency", target = "currency")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "accountDto.createdOn", target = "createdOn")
    Account toAccount(NewAccountDto accountDto, User user);
}