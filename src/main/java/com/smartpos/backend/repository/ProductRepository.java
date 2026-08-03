package com.smartpos.backend.repository;

import com.smartpos.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("SELECT p FROM Product p WHERE p.stockQuantity <= p.lowStockThreshold")
    List<Product> findLowStockProducts();

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}
