package com.smartpos.backend.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class UpdateProductRequest {
    private String name;

    @PositiveOrZero(message = "Unit Price cannot be negative")
    private Double unitPrice;

    @PositiveOrZero(message = "Stock Quantity cannot be negative")
    private Double stockQuantity;

    @PositiveOrZero(message = "Low Stock Threshold cannot be negative")
    private Double lowStockThreshold;
}
