package com.example.familybudget.mapper;

import com.example.familybudget.dto.OperationDto;
import com.example.familybudget.dto.ResponseOperation;
import com.example.familybudget.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OperationMapper {

    OperationMapper INSTANCE = Mappers.getMapper(OperationMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "account", target = "account")
    @Mapping(source = "createdOn", target = "createdOn")
    ResponseOperation toOperationDto(OperationIncome operation);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "account", target = "account")
    @Mapping(source = "createdOn", target = "createdOn")
    ResponseOperation toOperationDto(OperationExpense operation);


    @Mapping(source = "operationDto.amount", target = "amount")
    @Mapping(source = "operationDto.description", target = "description")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "account", target = "account")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "operationDto.createdOn", target = "createdOn")
    OperationIncome toOperationIncome(OperationDto operationDto, CategoryIncome category, Account account, User user);

    @Mapping(source = "operationDto.amount", target = "amount")
    @Mapping(source = "operationDto.description", target = "description")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "account", target = "account")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "operationDto.createdOn", target = "createdOn")
    OperationExpense toOperationExpense(OperationDto operationDto, CategoryExpense category, Account account, User user);
}