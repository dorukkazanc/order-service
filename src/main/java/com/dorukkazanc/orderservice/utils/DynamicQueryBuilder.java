package com.dorukkazanc.orderservice.utils;

import com.dorukkazanc.orderservice.dto.DynamicRequestDTO;
import com.dorukkazanc.orderservice.enums.FilterOperator;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class DynamicQueryBuilder {
    
    public static <T> Specification<T> buildSpecification(DynamicRequestDTO request) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (request.getFilters() != null) {
                for (DynamicRequestDTO.FilterCriteria filter : request.getFilters()) {
                    Predicate predicate = buildPredicate(root, criteriaBuilder, filter);
                    if (predicate != null) {
                        predicates.add(predicate);
                    }
                }
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    
    private static <T> Predicate buildPredicate(Root<T> root, CriteriaBuilder criteriaBuilder, 
                                               DynamicRequestDTO.FilterCriteria filter) {
        try {
            FilterOperator operator = FilterOperator.fromValue(filter.getOperator());
            String field = filter.getField();
            Object value = filter.getValue();

            return switch (operator) {
                case EQUALS -> criteriaBuilder.equal(root.get(field), value);
                case NOT_EQUALS -> criteriaBuilder.notEqual(root.get(field), value);
                case GREATER_THAN -> criteriaBuilder.greaterThan(root.get(field), (Comparable) value);
                case GREATER_THAN_EQUALS -> criteriaBuilder.greaterThanOrEqualTo(root.get(field), (Comparable) value);
                case LESS_THAN -> criteriaBuilder.lessThan(root.get(field), (Comparable) value);
                case LESS_THAN_EQUALS -> criteriaBuilder.lessThanOrEqualTo(root.get(field), (Comparable) value);
                case CONTAINS -> criteriaBuilder.like(root.get(field), "%" + value + "%");
                case NOT_CONTAINS -> criteriaBuilder.notLike(root.get(field), "%" + value + "%");
                case STARTS_WITH -> criteriaBuilder.like(root.get(field), value + "%");
                case ENDS_WITH -> criteriaBuilder.like(root.get(field), "%" + value);
                case IN -> {
                    if (value instanceof List) {
                        yield root.get(field).in((List<?>) value);
                    }
                    yield criteriaBuilder.in(root.get(field)).value(value);
                }
                case NOT_IN -> {
                    if (value instanceof List) {
                        yield criteriaBuilder.not(root.get(field).in((List<?>) value));
                    }
                    yield criteriaBuilder.not(criteriaBuilder.in(root.get(field)).value(value));
                }
                case IS_NULL -> criteriaBuilder.isNull(root.get(field));
                case IS_NOT_NULL -> criteriaBuilder.isNotNull(root.get(field));
                case BETWEEN -> {
                    if (value instanceof List && ((List<?>) value).size() == 2) {
                        List<?> values = (List<?>) value;
                        yield criteriaBuilder.between(root.get(field),
                                (Comparable) values.get(0), (Comparable) values.get(1));
                    }
                    yield null;
                }
                default -> null;
            };
        } catch (Exception e) {
            return null;
        }
    }
} 