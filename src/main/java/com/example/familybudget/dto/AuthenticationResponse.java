package com.example.familybudget.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Response after successful authentication")
public class AuthenticationResponse {
    @ApiModelProperty(notes = "token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcnRlbWV2MTk4NkBnbWFpbC5jb20iLCJle" +
            "HAiOjE2ODAwMzcxNjB9.e127HkzxqERU9kRjJW-PsNb9jxEoSrZOLk34-m-_P9s")
    private String token;
}