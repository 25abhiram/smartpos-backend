package com.smartpos.backend.dto;

import lombok.Data;

import java.util.Map;

@Data
public class SalesReportDto {
    private Long totalOrders;
    private Double totalRevenue;
    private Map<String,Double> revenueByBranch;
    private Map<String,Long> ordersByBranch;
}
