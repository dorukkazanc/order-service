package com.dorukkazanc.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetResponseDTO {
    
    private Long id;
    private String customerId;
    private String assetName;
    private Long size;
    private Long usableSize;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
} 