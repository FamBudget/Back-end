package com.example.familybudget.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse {

    private int status;
    private String message;

    @Override
    public String toString() {
        return "ApiResponse [statusCode=" + status + ", message=" + message +"]";
    }
}