package com.smartpos.backend.service;

import com.smartpos.backend.dto.CreateOrderRequest;
import com.smartpos.backend.dto.OrderItemDto;
import com.smartpos.backend.entity.*;
import com.smartpos.backend.repository.BranchRepository;
import com.smartpos.backend.repository.OrderRepository;
import com.smartpos.backend.repository.ProductRepository;
import com.smartpos.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private UserRepository userRepository;

    public Order createOrder(CreateOrderRequest orderRequest){
        Order order=new Order();
        double totalAmount=0;

        for(OrderItemDto item:orderRequest.getOrderItems()){
            OrderItem orderItem=new OrderItem();
            orderItem.setName(item.getName());
            orderItem.setQuantity(item.getQuantity());

            Product product=productRepository.findById(item.getProductId())
                    .orElseThrow(()->new RuntimeException("Product not found"));

            product.setStockQuantity(product.getStockQuantity()- item.getQuantity());
            productRepository.save(product);

            orderItem.setProduct(product);

            orderItem.setUnitPrice(product.getUnitPrice());

            totalAmount+= product.getUnitPrice()* item.getQuantity();

            order.getOrderItems().add(orderItem);
        }

        order.setTotalAmount(totalAmount);
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setReferenceId(UUID.randomUUID().toString());

        Branch branch=branchRepository.findById(orderRequest.getBranchId())
                .orElseThrow(()->new RuntimeException("Branch not found"));
        order.setBranch(branch);

        User cashier=userRepository.findById(orderRequest.getCashierId())
                .orElseThrow(()->new RuntimeException("Cashier not found"));
        order.setCashier(cashier);

        return orderRepository.save(order);
    }

    public Order getOrder(String referenceId){
        return orderRepository.findByReferenceId(referenceId)
                .orElseThrow(()->new RuntimeException("No order found with referenceId "+referenceId));
    }
}
