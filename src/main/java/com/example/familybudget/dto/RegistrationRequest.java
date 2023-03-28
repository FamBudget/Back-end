package com.example.familybudget.dto;

import com.example.familybudget.entity.annotation.PasswordValueMatch;
import com.example.familybudget.entity.annotation.ValidPassword;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

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
    private String email;
    private String firstName;
    private String lastName;
    @ValidPassword
    @NotEmpty
    private String password;
    private String confirmPassword;
}