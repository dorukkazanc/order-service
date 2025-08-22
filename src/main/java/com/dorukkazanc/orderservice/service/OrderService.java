package com.dorukkazanc.orderservice.service;

import com.dorukkazanc.orderservice.dto.OrderRequestDTO;
import com.dorukkazanc.orderservice.dto.OrderResponseDTO;
import com.dorukkazanc.orderservice.dto.OrderUpdateDTO;
import com.dorukkazanc.orderservice.entity.Order;
import com.dorukkazanc.orderservice.enums.OrderStatus;
import com.dorukkazanc.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        Order order = Order.builder()
                .customerId(orderRequestDTO.getCustomerId())
                .assetName(orderRequestDTO.getAssetName())
                .orderSide(orderRequestDTO.getOrderSide())
                .size(orderRequestDTO.getSize())
                .price(orderRequestDTO.getPrice())
                .status(OrderStatus.PENDING)
                .build();
        
        Order savedOrder = orderRepository.save(order);
        return convertToResponseDTO(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<OrderResponseDTO> getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::convertToResponseDTO);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByCustomerId(String customerId) {
        return orderRepository.findByCustomerId(customerId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<OrderResponseDTO> updateOrder(Long id, OrderUpdateDTO orderUpdateDTO) {
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    if (orderUpdateDTO.getSize() != null) {
                        existingOrder.setSize(orderUpdateDTO.getSize());
                    }
                    if (orderUpdateDTO.getPrice() != null) {
                        existingOrder.setPrice(orderUpdateDTO.getPrice());
                    }
                    if (orderUpdateDTO.getStatus() != null) {
                        existingOrder.setStatus(orderUpdateDTO.getStatus());
                    }
                    Order savedOrder = orderRepository.save(existingOrder);
                    return convertToResponseDTO(savedOrder);
                });
    }

    public boolean deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private OrderResponseDTO convertToResponseDTO(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getCustomerId(),
                order.getAssetName(),
                order.getOrderSide(),
                order.getSize(),
                order.getPrice(),
                order.getStatus(),
                order.getCreatedDate(),
                order.getLastModifiedDate()
        );
    }
}