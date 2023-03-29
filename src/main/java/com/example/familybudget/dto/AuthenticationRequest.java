package com.example.familybudget.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@ApiModel(description = "Request for authentication")
public class AuthenticationRequest {

    @Email
    @NotBlank
    @ApiModelProperty(notes = "User email", example = "test@mail.com", required = true)
    private String email;
    @NotBlank
    @ApiModelProperty(notes = "password", example = "12qwaszx!@QWASZX", required = true)
    private String password;
}

