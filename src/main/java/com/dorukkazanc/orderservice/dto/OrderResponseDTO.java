package com.dorukkazanc.orderservice.dto;

import com.dorukkazanc.orderservice.enums.OrderSide;
import com.dorukkazanc.orderservice.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {
    
    private Long id;
    private String customerId;
    private String assetName;
    private OrderSide orderSide;
    private Long size;
    private BigDecimal price;
    private OrderStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}