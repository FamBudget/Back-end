package com.example.familybudget.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class AccountDtoShort {
    private Long id;
    private String name;
    private String currency;
    private Long iconNumber;
}
