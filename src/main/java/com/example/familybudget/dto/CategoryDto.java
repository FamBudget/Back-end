package com.example.familybudget.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(description = "Short information about category")
@Data
public class CategoryDto {
    @ApiModelProperty(notes = "Category id", example = "1", required = true)
    @NotNull
    private Long id;
    @ApiModelProperty(notes = "Category name", example = "Category1", required = true)
    @NotBlank
    private String name;
}
