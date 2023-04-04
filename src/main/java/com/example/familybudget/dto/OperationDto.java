package com.example.familybudget.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
public class OperationDto {
    private Long id;
    @NotNull
    @Positive
    private Double amount;
    private String description;
    @NotNull
    private Long categoryId;
    @NotNull
    private Long accountId;
    private LocalDateTime createdOn;
}
