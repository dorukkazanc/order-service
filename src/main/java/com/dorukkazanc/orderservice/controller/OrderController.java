/**
 * Author: doruk.kazanc
 * Date: 22.08.2025
 */

package com.dorukkazanc.orderservice.controller;

import com.dorukkazanc.orderservice.dto.BaseResponse;
import com.dorukkazanc.orderservice.dto.OrderRequestDTO;
import com.dorukkazanc.orderservice.dto.OrderResponseDTO;
import com.dorukkazanc.orderservice.dto.OrderUpdateDTO;
import com.dorukkazanc.orderservice.entity.Customer;
import com.dorukkazanc.orderservice.service.OrderService;
import com.dorukkazanc.orderservice.service.ResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    private final ResponseService responseService;

    @PostMapping
    public ResponseEntity<BaseResponse<OrderResponseDTO>> createOrder(
            @Valid @RequestBody OrderRequestDTO orderRequestDTO,
            Authentication authentication) {
        try{

            Customer customer = (Customer) authentication.getPrincipal();
            OrderResponseDTO createdOrder = orderService.createOrder(customer.getId(), orderRequestDTO);

            return responseService.created(createdOrder, "Order created successfully");

        }catch(Exception e){
            return responseService.error(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<OrderResponseDTO>>> getAllOrders(
            Authentication auth,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        Customer customer = (Customer) auth.getPrincipal();
        List<OrderResponseDTO> orders;
        
        if (startDate != null && endDate != null) {
            orders = orderService.getOrdersByCustomerIdAndDateRange(customer.getId().toString(), startDate, endDate);
        } else {
            orders = orderService.getOrdersByCustomerId(customer.getId().toString());
        }
        return responseService.success(orders, "Orders retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<OrderResponseDTO>> getOrderById(@PathVariable Long id) {
        return responseService.fromOptional(
                orderService.getOrderById(id),
                "Order found",
                "Order not found with id: " + id
        );
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<BaseResponse<List<OrderResponseDTO>>> getOrdersByCustomerId(
            @PathVariable String customerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        List<OrderResponseDTO> orders;
        if (startDate != null && endDate != null) {
            orders = orderService.getOrdersByCustomerIdAndDateRange(customerId, startDate, endDate);
        } else {
            orders = orderService.getOrdersByCustomerId(customerId);
        }
        return responseService.success(orders, "Customer orders retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<OrderResponseDTO>> updateOrder(@PathVariable Long id, 
                                                       @Valid @RequestBody OrderUpdateDTO orderUpdateDTO) {
        return responseService.fromOptional(
                orderService.updateOrder(id, orderUpdateDTO),
                "Order updated successfully",
                "Order not found with id: " + id
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteOrder(@PathVariable Long id, Authentication authentication) {
        Customer customer = (Customer) authentication.getPrincipal();

        boolean deleted = orderService.deleteOrder(id, customer.getId());
        return deleted 
                ? responseService.successDelete("Order canceled successfully")
                : responseService.error("Only pending orders can be canceled or order not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
