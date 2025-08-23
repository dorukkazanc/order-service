/**
 * Author: doruk.kazanc
 * Date: 22.08.2025
 */

package com.dorukkazanc.orderservice.controller;

import com.dorukkazanc.orderservice.dto.BaseResponse;
import com.dorukkazanc.orderservice.dto.DynamicRequestDTO;
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
    
    @PostMapping("/search")
    public ResponseEntity<BaseResponse<List<OrderResponseDTO>>> searchOrders(
            @RequestBody DynamicRequestDTO request,
            Authentication authentication) {
        try {
            Customer customer = (Customer) authentication.getPrincipal();
            List<OrderResponseDTO> orders = orderService.searchOrdersByCustomerId(customer.getId(), request).getContent();
            return responseService.success(orders, "Orders searched successfully");
        } catch (Exception e) {
            return responseService.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
