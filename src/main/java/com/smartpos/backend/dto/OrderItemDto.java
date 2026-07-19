package com.smartpos.backend.dto;

import lombok.Data;

@Data
public class OrderItemDto {
    private String name;
    private Double quantity;
    private Long productId;
}
