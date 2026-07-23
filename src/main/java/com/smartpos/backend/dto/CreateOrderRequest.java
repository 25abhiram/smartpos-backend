package com.smartpos.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private String paymentMethod;
    private List<OrderItemDto> orderItems;
}
