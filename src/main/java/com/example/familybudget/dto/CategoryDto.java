package com.example.familybudget.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategoryDto {
    private Long id;
    private String name;
}
