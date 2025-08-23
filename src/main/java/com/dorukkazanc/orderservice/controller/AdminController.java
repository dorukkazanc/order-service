package com.dorukkazanc.orderservice.controller;

import com.dorukkazanc.orderservice.dto.*;
import com.dorukkazanc.orderservice.service.AdminService;
import com.dorukkazanc.orderservice.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final ResponseService responseService;

    @GetMapping("/customers")
    public ResponseEntity<BaseResponse<List<CustomerResponseDTO>>> getAllCustomers() {
        try {
            List<CustomerResponseDTO> customers = adminService.getAllCustomers();
            return responseService.success(customers, "All customers retrieved successfully");
        } catch (Exception e) {
            return responseService.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<BaseResponse<CustomerResponseDTO>> getCustomerById(@PathVariable Long customerId) {
        try {
            CustomerResponseDTO customer = adminService.getCustomerById(customerId);
            return responseService.success(customer, "Customer retrieved successfully");
        } catch (Exception e) {
            return responseService.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/customers/{customerId}")
    public ResponseEntity<BaseResponse<CustomerResponseDTO>> updateCustomer(
            @PathVariable Long customerId,
            @RequestBody CustomerUpdateDTO customerUpdateDTO) {
        try {
            CustomerResponseDTO updatedCustomer = adminService.updateCustomer(customerId, customerUpdateDTO);
            return responseService.success(updatedCustomer, "Customer updated successfully");
        } catch (Exception e) {
            return responseService.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/customers/{customerId}")
    public ResponseEntity<BaseResponse<Void>> deleteCustomer(@PathVariable Long customerId) {
        try {
            adminService.deleteCustomer(customerId);
            return responseService.successDelete("Customer deleted successfully");
        } catch (Exception e) {
            return responseService.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/assets")
    public ResponseEntity<BaseResponse<List<AssetResponseDTO>>> getAllAssets() {
        try {
            List<AssetResponseDTO> assets = adminService.getAllAssets();
            return responseService.success(assets, "All assets retrieved successfully");
        } catch (Exception e) {
            return responseService.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/assets/{assetId}")
    public ResponseEntity<BaseResponse<AssetResponseDTO>> getAssetById(@PathVariable Long assetId) {
        try {
            AssetResponseDTO asset = adminService.getAssetById(assetId);
            return responseService.success(asset, "Asset retrieved successfully");
        } catch (Exception e) {
            return responseService.error(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/assets/{assetId}")
    public ResponseEntity<BaseResponse<AssetResponseDTO>> updateAsset(
            @PathVariable Long assetId,
            @RequestBody AssetUpdateDTO assetUpdateDTO) {
        try {
            AssetResponseDTO updatedAsset = adminService.updateAsset(assetId, assetUpdateDTO);
            return responseService.success(updatedAsset, "Asset updated successfully");
        } catch (Exception e) {
            return responseService.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/assets/{assetId}")
    public ResponseEntity<BaseResponse<Void>> deleteAsset(@PathVariable Long assetId) {
        try {
            adminService.deleteAsset(assetId);
            return responseService.successDelete("Asset deleted successfully");
        } catch (Exception e) {
            return responseService.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<BaseResponse<List<OrderResponseDTO>>> getAllOrders() {
        try {
            List<OrderResponseDTO> orders = adminService.getAllOrders();
            return responseService.success(orders, "All orders retrieved successfully");
        } catch (Exception e) {
            return responseService.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<BaseResponse<OrderResponseDTO>> getOrderById(@PathVariable Long orderId) {
        try {
            OrderResponseDTO order = adminService.getOrderById(orderId);
            return responseService.success(order, "Order retrieved successfully");
        } catch (Exception e) {
            return responseService.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/orders/{orderId}")
    public ResponseEntity<BaseResponse<OrderResponseDTO>> updateOrder(
            @PathVariable Long orderId,
            @RequestBody OrderUpdateDTO orderUpdateDTO) {
        try {
            OrderResponseDTO updatedOrder = adminService.updateOrder(orderId, orderUpdateDTO);
            return responseService.success(updatedOrder, "Order updated successfully");
        } catch (Exception e) {
            return responseService.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<BaseResponse<Void>> deleteOrder(@PathVariable Long orderId) {
        try {
            adminService.deleteOrder(orderId);
            return responseService.successDelete("Order deleted successfully");
        } catch (Exception e) {
            return responseService.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/orders/search")
    public ResponseEntity<BaseResponse<List<OrderResponseDTO>>> searchOrders(@RequestBody DynamicRequestDTO request) {
        try {
            List<OrderResponseDTO> orders = adminService.searchOrders(request).getContent();
            return responseService.success(orders, "Orders searched successfully");
        } catch (Exception e) {
            return responseService.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/assets/search")
    public ResponseEntity<BaseResponse<List<AssetResponseDTO>>> searchAssets(@RequestBody DynamicRequestDTO request) {
        try {
            List<AssetResponseDTO> assets = adminService.searchAssets(request).getContent();
            return responseService.success(assets, "Assets searched successfully");
        } catch (Exception e) {
            return responseService.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/customers/search")
    public ResponseEntity<BaseResponse<List<CustomerResponseDTO>>> searchCustomers(@RequestBody DynamicRequestDTO request) {
        try {
            List<CustomerResponseDTO> customers = adminService.searchCustomers(request).getContent();
            return responseService.success(customers, "Customers searched successfully");
        } catch (Exception e) {
            return responseService.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/orders/match/{orderId}")
    public ResponseEntity<BaseResponse<OrderMatchResponseDTO>> matchOrder(@PathVariable Long orderId) {
        try {
            OrderMatchResponseDTO approvedOrder = adminService.matchOrder(orderId);
            return responseService.success(approvedOrder, "Order approved successfully");
        } catch (Exception e) {
            return responseService.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}