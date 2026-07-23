package com.smartpos.backend.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String username;
    private String password;
    private Long branchId;
    private String role;
}
