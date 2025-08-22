/**
 * Author: doruk.kazanc
 * Date: 22.08.2025
 */

package com.dorukkazanc.orderservice.controller;

import com.dorukkazanc.orderservice.dto.BaseResponse;
import com.dorukkazanc.orderservice.dto.OrderRequestDTO;
import com.dorukkazanc.orderservice.dto.OrderResponseDTO;
import com.dorukkazanc.orderservice.dto.OrderUpdateDTO;
import com.dorukkazanc.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<BaseResponse<OrderResponseDTO>> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO createdOrder = orderService.createOrder(orderRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(createdOrder, "Order created successfully"));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<OrderResponseDTO>>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(BaseResponse.success(orders, "Orders retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<OrderResponseDTO>> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(order -> ResponseEntity.ok(BaseResponse.success(order, "Order found")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(BaseResponse.error("Order not found with id: " + id)));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<BaseResponse<List<OrderResponseDTO>>> getOrdersByCustomerId(@PathVariable String customerId) {
        List<OrderResponseDTO> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(BaseResponse.success(orders, "Customer orders retrieved successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<OrderResponseDTO>> updateOrder(@PathVariable Long id, 
                                                       @Valid @RequestBody OrderUpdateDTO orderUpdateDTO) {
        return orderService.updateOrder(id, orderUpdateDTO)
                .map(order -> ResponseEntity.ok(BaseResponse.success(order, "Order updated successfully")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(BaseResponse.error("Order not found with id: " + id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteOrder(@PathVariable Long id) {
        boolean deleted = orderService.deleteOrder(id);
        return deleted 
                ? ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(BaseResponse.<Void>success("Order deleted successfully"))
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(BaseResponse.error("Order not found with id: " + id));
    }
}
