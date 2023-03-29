package com.example.familybudget.dto;

import com.example.familybudget.entity.Currency;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Short information about requested user")
public class UserDto {
    @ApiModelProperty(notes = "User ID", example = "1")
    private Long id;
    @ApiModelProperty(notes = "User email", example = "test@mail.com")
    private String email;
    @ApiModelProperty(notes = "first name", example = "Mikhail")
    private String firstName;
    @ApiModelProperty(notes = "Last name", example = "Stone")
    private String lastName;
    @ApiModelProperty(notes = "Currency", example = "RUB_RUS")
    private Currency currency;
}
