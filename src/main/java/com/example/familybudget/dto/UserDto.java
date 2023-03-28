package com.example.familybudget.dto;

import com.example.familybudget.entity.Currency;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserDto {
    @ApiModelProperty(notes = "User ID", example = "1", required = false)
    private Long id;
    @ApiModelProperty(notes = "User email", example = "test@mail.com", required = false)
    private String email;
    @ApiModelProperty(notes = "first name", example = "Mikhail", required = false)
    private String firstName;
    @ApiModelProperty(notes = "Last name", example = "Stone", required = false)
    private String lastName;
    @ApiModelProperty(notes = "RUB_RUS", example = "1", required = false)
    private Currency currency;
}
