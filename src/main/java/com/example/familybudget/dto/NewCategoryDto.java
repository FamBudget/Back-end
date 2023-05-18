package com.example.familybudget.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel(description = "New category")
@Data
public class NewCategoryDto {
    @ApiModelProperty(notes = "Category name", example = "Category1", required = true)
    @NotBlank
    private String name;
    @ApiModelProperty(notes = "Icon number", example = "1")
    private Long iconNumber;
}
