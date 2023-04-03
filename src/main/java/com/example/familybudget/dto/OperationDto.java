package com.example.familybudget.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class OperationDto {
    private Long id;
    @NotEmpty
    private Double amount;
    private String description;
    @NotEmpty
    private Long categoryId;
    @NotEmpty
    private Long accountId;
    private LocalDateTime createdOn;
}
