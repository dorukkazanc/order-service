package com.dorukkazanc.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DynamicRequestDTO {
    
    private List<FilterCriteria> filters;
    private String sortBy;
    private String sortDirection;
    private Integer page;
    private Integer size;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FilterCriteria {
        private String field;
        private String operator;
        private Object value;
        private String valueType;
    }
} 