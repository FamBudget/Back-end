package com.example.familybudget.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
public class OperationMovingDto {
    @NotNull
    @Positive
    private Double amount;
    private String description;
    @NotNull
    private Long accountFromId;
    @NotNull
    private Long accountToId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent
    private LocalDateTime createdOn;
}
