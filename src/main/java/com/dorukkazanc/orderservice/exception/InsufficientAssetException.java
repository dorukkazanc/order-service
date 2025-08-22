package com.dorukkazanc.orderservice.exception;

public class InsufficientAssetException extends RuntimeException {
    
    public InsufficientAssetException(String message) {
        super(message);
    }
    
    public InsufficientAssetException(String message, Throwable cause) {
        super(message, cause);
    }
}