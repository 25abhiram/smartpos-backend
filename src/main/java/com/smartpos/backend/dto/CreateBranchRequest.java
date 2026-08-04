package com.smartpos.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateBranchRequest {
    @NotBlank(message = "Branch name cannot be empty")
    private String name;

    @NotBlank(message = "Branch address cannot be empty")
    private String address;
}
