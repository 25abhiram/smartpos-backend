package com.smartpos.backend.dto;

import lombok.Data;

@Data
public class UpdateBranchRequest {
    private String name;
    private String address;
}
