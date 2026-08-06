package com.smartpos.backend.controller;

import com.smartpos.backend.dto.CreateOrderRequest;
import com.smartpos.backend.entity.Order;
import com.smartpos.backend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody CreateOrderRequest orderRequest){
        Order order=orderService.createOrder(orderRequest);
        return ResponseEntity.ok().body(order);
    }

    @GetMapping("/{referenceId}")
    public ResponseEntity<?> getOrder(@PathVariable String referenceId){
        Order order=orderService.getOrder(referenceId);
        return ResponseEntity.ok().body(order);
    }
}
