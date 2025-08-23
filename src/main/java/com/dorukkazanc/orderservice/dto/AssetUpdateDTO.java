package com.dorukkazanc.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetUpdateDTO {
    
    private String assetName;
    private Long size;
    private Long usableSize;
} 