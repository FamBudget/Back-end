package com.example.familybudget.dto;

import com.example.familybudget.entity.annotation.PasswordValueMatch;
import com.example.familybudget.entity.annotation.ValidPassword;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@PasswordValueMatch.List({
        @PasswordValueMatch(
                field = "password",
                fieldMatch = "confirmPassword",
                message = "Passwords do not match!"
        )
})
@Data
public class RegistrationRequest {

    @Email
    @NotBlank
    @ApiModelProperty(notes = "User email", example = "test@mail.com", required = true)
    private String email;
    @ApiModelProperty(notes = "first name", example = "Mikhail", required = false)
    private String firstName;
    @ApiModelProperty(notes = "Last name", example = "Stone", required = false)
    private String lastName;
    @ValidPassword
    @NotBlank
    @ApiModelProperty(notes = "password", example = "12qwaszx!@QWASZX", required = true)
    private String password;
    @NotBlank
    @ApiModelProperty(notes = "confirmPassword", example = "12qwaszx!@QWASZX", required = true)
    private String confirmPassword;
    @NotBlank
    @ApiModelProperty(notes = "currency", example = "RUB_RUS", required = true)
    private String currency;
}