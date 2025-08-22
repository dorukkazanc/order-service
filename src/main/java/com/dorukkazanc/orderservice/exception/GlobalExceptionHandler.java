package com.dorukkazanc.orderservice.exception;

import com.dorukkazanc.orderservice.dto.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientAssetException.class)
    public ResponseEntity<BaseResponse<Void>> handleInsufficientAssetException(InsufficientAssetException ex) {
        log.warn("Insufficient asset exception: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Illegal argument exception: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("Validation exception occurred");
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.error("Validation failed", errors));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BaseResponse<Void>> handleRuntimeException(RuntimeException ex) {
        log.error("Unexpected runtime exception: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.error("An unexpected error occurred"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleGenericException(Exception ex) {
        log.error("Unexpected exception: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.error("An unexpected error occurred"));
    }
}