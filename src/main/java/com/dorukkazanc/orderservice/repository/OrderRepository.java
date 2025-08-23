package com.dorukkazanc.orderservice.repository;

import com.dorukkazanc.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(String customerId);
    List<Order> findByCustomerIdAndCreatedDateBetween(String customerId, LocalDateTime createdDateAfter, LocalDateTime createdDateBefore);
    Optional<Order> findOrderByIdAndCustomerId(Long id, String customerId);
}
