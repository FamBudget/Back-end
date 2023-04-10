package com.example.familybudget.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ResponseOperationMoving {
    private Long id;
    private Double amount;
    private String description;
    private AccountDtoShort accountFrom;
    private AccountDtoShort accountTo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
}
