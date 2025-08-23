package com.dorukkazanc.orderservice.enums;

public enum FilterOperator {
    EQUALS("eq"),
    NOT_EQUALS("ne"),
    GREATER_THAN("gt"),
    GREATER_THAN_EQUALS("gte"),
    LESS_THAN("lt"),
    LESS_THAN_EQUALS("lte"),
    CONTAINS("contains"),
    NOT_CONTAINS("not_contains"),
    STARTS_WITH("starts_with"),
    ENDS_WITH("ends_with"),
    IN("in"),
    NOT_IN("not_in"),
    IS_NULL("is_null"),
    IS_NOT_NULL("is_not_null"),
    BETWEEN("between");
    
    private final String value;
    
    FilterOperator(String value) {
        this.value = value;
    }
    
    public static FilterOperator fromValue(String value) {
        for (FilterOperator operator : values()) {
            if (operator.value.equals(value)) {
                return operator;
            }
        }
        throw new IllegalArgumentException("Unknown operator: " + value);
    }
} 