package com.dorukkazanc.orderservice.dto;

import com.dorukkazanc.orderservice.enums.OrderSide;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    
    @NotBlank(message = "Customer ID cannot be blank")
    private String customerId;
    
    @NotBlank(message = "Asset name cannot be blank")
    private String assetName;
    
    @NotNull(message = "Order side cannot be null")
    private OrderSide orderSide;
    
    @NotNull(message = "Size cannot be null")
    @Positive(message = "Size must be positive")
    private Long size;
    
    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
}