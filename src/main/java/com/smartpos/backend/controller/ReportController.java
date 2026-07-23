package com.smartpos.backend.controller;

import com.smartpos.backend.dto.SalesReportDto;
import com.smartpos.backend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/sales")
    public ResponseEntity<SalesReportDto> getSalesReport(){
        SalesReportDto report=reportService.generateAdminSalesReport();
        return ResponseEntity.ok(report);
    }
}
