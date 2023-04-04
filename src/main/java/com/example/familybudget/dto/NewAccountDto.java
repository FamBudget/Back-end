package com.example.familybudget.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
public class NewAccountDto {
    @NotBlank
    private String name;
    @NotNull
    @PositiveOrZero
    private Double amount;
    private String currency;
    private LocalDateTime createdOn;
}
