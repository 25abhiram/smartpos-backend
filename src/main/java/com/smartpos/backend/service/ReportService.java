package com.smartpos.backend.service;

import com.smartpos.backend.dto.SalesReportDto;
import com.smartpos.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {
    @Autowired
    private OrderRepository orderRepository;

    public SalesReportDto generateAdminSalesReport(){
        SalesReportDto report=new SalesReportDto();

        report.setTotalOrders(orderRepository.countTotalOrders());
        report.setTotalRevenue(orderRepository.calculateTotalRevenue());

        Map<String,Double> revenueData=new HashMap<>();
        Map<String,Long> orderCountData=new HashMap<>();

        List<Object[]> stats=orderRepository.getBranchSalesStats();

        for (Object[] stat:stats){
            String branchName=(String) stat[0];
            Double revenue=(Double) stat[1];
            Long orderCount=(Long) stat[2];
            revenueData.put(branchName,revenue);
            orderCountData.put(branchName,orderCount);
        }

        report.setRevenueByBranch(revenueData);
        report.setOrdersByBranch(orderCountData);

        return report;
    }
}
