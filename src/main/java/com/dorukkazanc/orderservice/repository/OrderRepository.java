package com.dorukkazanc.orderservice.repository;

import com.dorukkazanc.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(String customerId);
    List<Order> findByCustomerIdAndCreatedDateBetween(String customerId, LocalDateTime createdDateAfter, LocalDateTime createdDateBefore);
}
