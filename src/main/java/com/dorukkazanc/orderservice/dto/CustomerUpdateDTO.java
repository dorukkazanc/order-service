package com.dorukkazanc.orderservice.dto;

import com.dorukkazanc.orderservice.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerUpdateDTO {
    
    private String username;
    private Boolean active;
    private UserRole role;
} 