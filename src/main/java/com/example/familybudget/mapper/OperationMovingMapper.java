package com.example.familybudget.mapper;

import com.example.familybudget.dto.OperationMovingDto;
import com.example.familybudget.dto.ResponseOperationMoving;
import com.example.familybudget.entity.Account;
import com.example.familybudget.entity.OperationMoving;
import com.example.familybudget.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OperationMovingMapper {

    OperationMovingMapper INSTANCE = Mappers.getMapper(OperationMovingMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "accountFrom", target = "accountFrom")
    @Mapping(source = "accountTo", target = "accountTo")
    @Mapping(source = "createdOn", target = "createdOn")
    ResponseOperationMoving toOperationDto(OperationMoving operation);


    @Mapping(source = "operationDto.amount", target = "amount")
    @Mapping(source = "operationDto.description", target = "description")
    @Mapping(source = "accountFrom", target = "accountFrom")
    @Mapping(source = "accountTo", target = "accountTo")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "operationDto.createdOn", target = "createdOn")
    OperationMoving toOperation(OperationMovingDto operationDto, Account accountFrom, Account accountTo, User user);
}