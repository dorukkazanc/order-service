package com.dorukkazanc.orderservice.service;

import com.dorukkazanc.orderservice.dto.OrderRequestDTO;
import com.dorukkazanc.orderservice.dto.OrderResponseDTO;
import com.dorukkazanc.orderservice.dto.OrderUpdateDTO;
import com.dorukkazanc.orderservice.entity.Asset;
import com.dorukkazanc.orderservice.entity.Order;
import com.dorukkazanc.orderservice.enums.OrderSide;
import com.dorukkazanc.orderservice.enums.OrderStatus;
import com.dorukkazanc.orderservice.exception.InsufficientAssetException;
import com.dorukkazanc.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final AssetService assetService;

    public OrderResponseDTO createOrder(Long customerId, OrderRequestDTO orderRequestDTO) {
        Asset asset;
        if(orderRequestDTO.getOrderSide().equals(OrderSide.BUY)) {
            asset = assetService.getAssetByCustomerIdAndName(customerId, "TRY");
        }else {
            asset = assetService.getAssetByCustomerIdAndName(customerId, orderRequestDTO.getAssetName());
        }

        if(checkIfAssetIsNotSufficient(orderRequestDTO, asset)) {
            throw new InsufficientAssetException("Insufficient asset for the order");
        }

        Order order = Order.builder()
                .customerId(customerId.toString())
                .assetName(orderRequestDTO.getAssetName())
                .orderSide(orderRequestDTO.getOrderSide())
                .size(orderRequestDTO.getSize())
                .price(orderRequestDTO.getPrice())
                .status(OrderStatus.PENDING)
                .build();
        
        Order savedOrder = orderRepository.save(order);
        assetService.updateAssetForOrder(savedOrder, asset);

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

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByCustomerIdAndDateRange(String customerId, LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByCustomerIdAndCreatedDateBetween(customerId, startDate, endDate)
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

    public boolean deleteOrder(Long id, Long customerId) {
        Optional<Order> order = orderRepository.findOrderByIdAndCustomerId(id, customerId.toString());

        if (order.isPresent() && order.get().getStatus() == OrderStatus.PENDING) {
            order.get().setStatus(OrderStatus.CANCELED);
            orderRepository.save(order.get());
            return true;
        }
        return false;
    }

    private OrderResponseDTO convertToResponseDTO(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .assetName(order.getAssetName())
                .orderSide(order.getOrderSide())
                .size(order.getSize())
                .price(order.getPrice())
                .status(order.getStatus())
                .createdDate(order.getCreatedDate())
                .lastModifiedDate(order.getLastModifiedDate())
                .build(
        );
    }

    private boolean checkIfAssetIsNotSufficient(OrderRequestDTO orderRequest, Asset asset) {
        if (asset == null) {
            return true;
        }

        if (orderRequest.getOrderSide().equals(OrderSide.BUY)) {
            BigDecimal totalCost = orderRequest.getPrice().multiply(BigDecimal.valueOf(orderRequest.getSize()));
            return asset.getUsableSize() < totalCost.longValue();

        } else if (orderRequest.getOrderSide().equals(OrderSide.SELL)) {
            return asset.getUsableSize() < orderRequest.getSize();
        }
        
        return false;
    }

    private void validateOrderRequest(Long customerId, OrderRequestDTO request) {
        Asset asset = getRequiredAsset(customerId, request);
        if (checkIfAssetIsNotSufficient(request, asset)) {
            throw new InsufficientAssetException("Insufficient asset for the order");
        }
    }

    private Asset getRequiredAsset(Long customerId, OrderRequestDTO request) {
        return request.getOrderSide().equals(OrderSide.BUY)
                ? assetService.getAssetByCustomerIdAndName(customerId, "TRY")
                : assetService.getAssetByCustomerIdAndName(customerId, request.getAssetName());
    }


}