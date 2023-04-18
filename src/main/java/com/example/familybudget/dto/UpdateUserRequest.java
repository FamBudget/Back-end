package com.example.familybudget.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@ApiModel(description = "Request for update User")
public class UpdateUserRequest {

    @Email
    @ApiModelProperty(notes = "User email", example = "test@mail.com")
    private String email;
    @ApiModelProperty(notes = "first name", example = "Mikhail")
    private String firstName;
    @ApiModelProperty(notes = "Last name", example = "Stone")
    private String lastName;
}