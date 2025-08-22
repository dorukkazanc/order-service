package com.dorukkazanc.orderservice.dto;

import com.dorukkazanc.orderservice.enums.OrderStatus;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateDTO {
    
    @Positive(message = "Size must be positive")
    private Long size;
    
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    
    private OrderStatus status;
}