package com.example.familybudget.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "Request for new password")
public class ResponseResetPassword {
    String status;
    String email;
}
