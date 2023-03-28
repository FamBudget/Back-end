package com.example.familybudget.exception;

public class CurrencyNotValidException extends RuntimeException {
    public CurrencyNotValidException(String message) {
        super(message);
    }
}