package com.example.familybudget.dto;

import com.example.familybudget.Created;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
public class OperationDto {
    @NotNull
    private Long id;
    @NotNull(groups = Created.class)
    @Positive(groups = Created.class)
    private Double amount;
    private String description;
    @NotNull(groups = Created.class)
    private Long categoryId;
    @NotNull(groups = Created.class)
    private Long accountId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent(groups = Created.class)
    private LocalDateTime createdOn;
}
