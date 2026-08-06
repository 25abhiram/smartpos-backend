package com.smartpos.backend.service;

import com.smartpos.backend.dto.CreateOrderRequest;
import com.smartpos.backend.dto.OrderItemDto;
import com.smartpos.backend.entity.*;
import com.smartpos.backend.exceptions.InsufficientStockException;
import com.smartpos.backend.exceptions.ResourceNotFoundException;
import com.smartpos.backend.repository.OrderRepository;
import com.smartpos.backend.repository.ProductRepository;
import com.smartpos.backend.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class OrderService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(CreateOrderRequest orderRequest){
        Order order=new Order();
        double totalAmount=0;

        for(OrderItemDto item:orderRequest.getOrderItems()){
            Product product=productRepository.findById(item.getProductId())
                    .orElseThrow(()->new ResourceNotFoundException("Product not found with id "+item.getProductId()));

            if (product.getStockQuantity()< item.getQuantity()){
                throw new InsufficientStockException(
                        "Insufficient stock for product: '"+item.getProductId()+"'. "+"Requested: "+item.getQuantity()+", Available: "+product.getStockQuantity());
            }

            product.setStockQuantity(product.getStockQuantity()- item.getQuantity());
            productRepository.save(product);

            OrderItem orderItem=new OrderItem();
            orderItem.setName(product.getName());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setUnitPrice(product.getUnitPrice());
            orderItem.setProduct(product);

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
        order.setBranch(cashier.getBranch());

        return orderRepository.save(order);
    }

    public Order getOrder(String referenceId){
        return orderRepository.findByReferenceId(referenceId)
                .orElseThrow(()->new ResourceNotFoundException("No order found with referenceId "+referenceId));
    }
}
