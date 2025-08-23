package com.dorukkazanc.orderservice.service;

import com.dorukkazanc.orderservice.dto.OrderResponseDTO;
import com.dorukkazanc.orderservice.entity.Order;
import com.dorukkazanc.orderservice.enums.OrderSide;
import com.dorukkazanc.orderservice.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final OrderService orderService;
    private final AssetService assetService;


    public void matchOrder(Long orderId) {
        OrderResponseDTO order = orderService.getOrderById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        if(order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Order is not in a matchable state: " + order.getStatus());
        }

        if(order.getOrderSide().equals(OrderSide.BUY)) {
            orderService.getOrdersByOrderSide(OrderSide.SELL)
                    .stream()
                    .filter(sellOrder -> sellOrder.getAssetName().equals(order.getAssetName())
                            && !Objects.equals(order.getCustomerId(), sellOrder.getCustomerId()))
                    .findFirst()
                    .ifPresent(sellOrder -> {
                        // Execute match logic
                    });
        } else {
            orderService.getOrdersByOrderSide(OrderSide.BUY)
                    .stream()
                    .filter(buyOrder -> buyOrder.getAssetName().equals(order.getAssetName())
                            && !Objects.equals(order.getCustomerId(), buyOrder.getCustomerId()))
                    .findFirst()
                    .ifPresent(buyOrder -> {
                        // Execute match logic
                    });
            }
    }
}
