package com.dorukkazanc.orderservice.repository;

import com.dorukkazanc.orderservice.entity.Order;
import com.dorukkazanc.orderservice.enums.OrderSide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Optional<Order> findOrderByIdAndCustomerId(Long id, String customerId);

    List<Order> findOrdersByOrderSide(OrderSide orderSide);
}
