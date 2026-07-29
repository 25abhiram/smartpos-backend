package com.smartpos.backend.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UpdateUserRequest {
    private String username;
    private String password;
    private Long branchId;
    private Set<Long> roleIds;
}
