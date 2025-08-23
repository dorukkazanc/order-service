package com.dorukkazanc.orderservice.dto;

import com.dorukkazanc.orderservice.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponseDTO {
    
    private Long id;
    private String username;
    private Boolean active;
    private UserRole role;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
} 