package com.smartpos.backend.repository;

import com.smartpos.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<Order> findByReferenceId(String referenceId);
}
