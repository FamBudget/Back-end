package com.example.familybudget.exception;

import lombok.Data;

@Data
public class ApiError {
    private String message;
    private String status;
    private String timestamp;
}