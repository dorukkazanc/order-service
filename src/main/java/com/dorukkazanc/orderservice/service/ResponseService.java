package com.dorukkazanc.orderservice.service;

import com.dorukkazanc.orderservice.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResponseService {

    public <T> ResponseEntity<BaseResponse<T>> success(T data, String message) {
        return ResponseEntity.ok(BaseResponse.success(data, message));
    }

    public <T> ResponseEntity<BaseResponse<T>> created(T data, String message) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(data, message));
    }

    public <T> ResponseEntity<BaseResponse<T>> error(String message, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(BaseResponse.error(message));
    }

    public <T> ResponseEntity<BaseResponse<T>> notFound(String message) {
        return error(message, HttpStatus.NOT_FOUND);
    }

    public <T> ResponseEntity<BaseResponse<T>> unauthorized(String message) {
        return error(message, HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<BaseResponse<Void>> noContent(String message) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(BaseResponse.<Void>success(message));
    }

    public <T> ResponseEntity<BaseResponse<T>> fromOptional(Optional<T> optional, String successMessage, String notFoundMessage) {
        return optional
                .map(data -> success(data, successMessage))
                .orElse(notFound(notFoundMessage));
    }

    public ResponseEntity<BaseResponse<Void>> successDelete(String message) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.success(null, message));
    }
}