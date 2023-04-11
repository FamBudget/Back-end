package com.example.familybudget.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "Response to security operations with a user")
public class ResponseUserSecurityStatus {
    String status;
    String email;
}
