package com.dorukkazanc.orderservice.service;

import com.dorukkazanc.orderservice.dto.MatchExecutionResult;
import com.dorukkazanc.orderservice.dto.OrderResponseDTO;
import com.dorukkazanc.orderservice.dto.OrderUpdateDTO;
import com.dorukkazanc.orderservice.enums.OrderSide;
import com.dorukkazanc.orderservice.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchService {
    private final OrderService orderService;
    private final AssetService assetService;

    public void matchOrder(Long orderId) {
        OrderResponseDTO order = orderService.getOrderById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Order is not in a matchable state: " + order.getStatus());
        }

        List<MatchExecutionResult> executionResults = new ArrayList<>();
        Long remainingSize = order.getSize();

        if (order.getOrderSide().equals(OrderSide.BUY)) {
            remainingSize = matchBuyOrder(order, executionResults, remainingSize);
        } else {
            remainingSize = matchSellOrder(order, executionResults, remainingSize);
        }
        OrderStatus newStatus = remainingSize <= 0 ? OrderStatus.MATCHED : OrderStatus.PENDING;

        orderService.updateOrder(orderId, OrderUpdateDTO.builder()
                .size(remainingSize)
                .status(newStatus)
                .build());
                
        log.info("Order {} matching completed. Status: {}, Remaining size: {}, Executions: {}", 
                orderId, newStatus, remainingSize, executionResults.size());
    }

    private Long matchBuyOrder(OrderResponseDTO buyOrder, List<MatchExecutionResult> executionResults, Long remainingSize) {
        List<OrderResponseDTO> orders = orderService.getOrdersByOrderSide(OrderSide.SELL)
                .stream()
                .filter(sellOrder -> sellOrder.getStatus() == OrderStatus.PENDING
                        && sellOrder.getAssetName().equals(buyOrder.getAssetName())
                        && !Objects.equals(buyOrder.getCustomerId(), sellOrder.getCustomerId())
                        && sellOrder.getPrice().compareTo(buyOrder.getPrice()) == 0)
                .sorted(Comparator.comparing(OrderResponseDTO::getCreatedDate,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();

        for (OrderResponseDTO sellOrder : orders) {
            if( remainingSize <= 0) {
                break;
            }

            Long matchedSize = Math.min(remainingSize, sellOrder.getSize());
            BigDecimal matchedPrice = sellOrder.getPrice();

            var execution = executeMatches(buyOrder, sellOrder, matchedSize, matchedPrice);
            executionResults.add(execution);

            remainingSize -= matchedSize;

            Long newSize = sellOrder.getSize() - matchedSize;
            OrderStatus newStatus = newSize <= 0 ? OrderStatus.MATCHED : sellOrder.getStatus();

            orderService.updateOrder(sellOrder.getId(), OrderUpdateDTO.builder()
                    .size(newSize)
                    .status(newStatus)
                    .build());

            log.info("BUY order {} matched {} shares with BUY order {} at price {}",
                    buyOrder.getId(), matchedSize, sellOrder.getId(), matchedPrice);        }

        return remainingSize;
    }

    private Long matchSellOrder(OrderResponseDTO sellOrder, List<MatchExecutionResult> executionResults, Long remainingSize) {
        List<OrderResponseDTO> orders = orderService.getOrdersByOrderSide(OrderSide.BUY)
                .stream()
                .filter(buyOrder -> buyOrder.getStatus() == OrderStatus.PENDING
                        && buyOrder.getAssetName().equals(sellOrder.getAssetName())
                        && !Objects.equals(sellOrder.getCustomerId(), buyOrder.getCustomerId())
                        && buyOrder.getPrice().compareTo(sellOrder.getPrice()) == 0)
                .sorted(Comparator.comparing(OrderResponseDTO::getCreatedDate,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();

        for (OrderResponseDTO buyOrder : orders) {
            if( remainingSize <= 0) {
                break;
            }

            Long matchedSize = Math.min(remainingSize, buyOrder.getSize());
            BigDecimal matchedPrice = buyOrder.getPrice();

            var execution = executeMatches(buyOrder, sellOrder, matchedSize, matchedPrice);
            executionResults.add(execution);

            remainingSize -= matchedSize;

            Long newSize = buyOrder.getSize() - matchedSize;
            OrderStatus newStatus = newSize <= 0 ? OrderStatus.MATCHED : buyOrder.getStatus();

            orderService.updateOrder(buyOrder.getId(), OrderUpdateDTO.builder()
                    .size(newSize)
                    .status(newStatus)
                    .build());
            log.info("SELL order {} matched {} shares with BUY order {} at price {}",
                    sellOrder.getId(), matchedSize, buyOrder.getId(), matchedPrice);
        }

        return remainingSize;
    }

    private MatchExecutionResult executeMatches(OrderResponseDTO buyOrder, OrderResponseDTO sellOrder, Long matchedSize, BigDecimal matchedPrice) {
        BigDecimal totalCost = matchedPrice.multiply(BigDecimal.valueOf(matchedSize));
        assetService.transferAssetsBetweenCustomers(buyOrder.getCustomerId(), sellOrder.getCustomerId(), buyOrder.getAssetName(), matchedSize, totalCost);
        
        return MatchExecutionResult.builder()
                .matchedOrderId(sellOrder.getId())
                .matchedSize(matchedSize)
                .executionPrice(matchedPrice)
                .totalValue(totalCost)
                .matchTime(LocalDateTime.now())
                .build();
    }
}
