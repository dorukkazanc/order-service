package com.dorukkazanc.orderservice.utils;

import com.dorukkazanc.orderservice.dto.DynamicRequestDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableBuilder {

    public static Pageable build(DynamicRequestDTO request, String defaultSortField) {
        return build(request, defaultSortField, Sort.Direction.DESC);
    }
    
    public static Pageable build(DynamicRequestDTO request, String defaultSortField, Sort.Direction defaultDirection) {
        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 10;
        String sortBy = request.getSortBy() != null ? request.getSortBy() : defaultSortField;
        Sort.Direction direction = request.getSortDirection() != null && 
            request.getSortDirection().equalsIgnoreCase("asc") ? 
            Sort.Direction.ASC : defaultDirection;
        
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }
} 