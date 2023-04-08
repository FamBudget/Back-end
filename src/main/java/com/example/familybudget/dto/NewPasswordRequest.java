package com.example.familybudget.dto;

import com.example.familybudget.entity.annotation.PasswordValueMatch;
import com.example.familybudget.entity.annotation.ValidPassword;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@PasswordValueMatch.List({
        @PasswordValueMatch(
                field = "password",
                fieldMatch = "confirmPassword",
                message = "Passwords do not match!"
        )
})
@Data
@ApiModel(description = "Request for new password")
public class NewPasswordRequest {
    @ValidPassword
    @NotBlank
    @ApiModelProperty(notes = "password", example = "12qwaszx!@QWASZX", required = true)
    private String password;
    @NotBlank
    @ApiModelProperty(notes = "confirmPassword", example = "12qwaszx!@QWASZX", required = true)
    private String confirmPassword;
}