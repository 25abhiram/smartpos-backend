package com.smartpos.backend.service;

import com.smartpos.backend.dto.CreateOrderRequest;
import com.smartpos.backend.dto.OrderItemDto;
import com.smartpos.backend.entity.*;
import com.smartpos.backend.repository.BranchRepository;
import com.smartpos.backend.repository.OrderRepository;
import com.smartpos.backend.repository.ProductRepository;
import com.smartpos.backend.repository.UserRepository;
import com.smartpos.backend.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails=(UserDetailsImpl) authentication.getPrincipal();

        User cashier=userDetails.getUser();
        order.setCashier(cashier);

        Branch branch=cashier.getBranch();
        order.setBranch(branch);

        return orderRepository.save(order);
    }

    public Order getOrder(String referenceId){
        return orderRepository.findByReferenceId(referenceId)
                .orElseThrow(()->new RuntimeException("No order found with referenceId "+referenceId));
    }
}
