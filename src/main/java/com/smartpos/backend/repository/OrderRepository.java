package com.smartpos.backend.repository;

import com.smartpos.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<Order> findByReferenceId(String referenceId);

    @Query("SELECT COUNT(o) FROM Order o")
    Long countTotalOrders();

    @Query("SELECT COALESCE(SUM(o.totalAmount),0) FROM Order o")
    Double calculateTotalRevenue();

    @Query("SELECT o.branch.name, SUM(o.totalAmount), COUNT(o) FROM Order o GROUP BY o.branch.name")
    List<Object[]> getBranchSalesStats();
}
