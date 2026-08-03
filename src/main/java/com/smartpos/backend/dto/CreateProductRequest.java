package com.smartpos.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class CreateProductRequest {
    @NotBlank(message = "Product name cannot be empty")
    private String name;

    @NotNull(message = "Unit price is required")
    @PositiveOrZero(message = "Unit Price cannot be negative")
    private Double unitPrice;

    @NotNull(message = "Stock Quantity is required")
    @PositiveOrZero(message = "Stock Quantity  cannot be negative")
    private Double stockQuantity;

    @NotNull(message = "Low stock threshold is required")
    @PositiveOrZero(message = "Low Stock Threshold cannot be negative")
    private Double lowStockThreshold;
}
