package com.example.familybudget.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class AuthenticationRequest {

    @Email
    private String email;
    @NotBlank
    private String password;
}

