package com.dorukkazanc.orderservice.service;

import com.dorukkazanc.orderservice.dto.*;
import com.dorukkazanc.orderservice.entity.Asset;
import com.dorukkazanc.orderservice.entity.Customer;
import com.dorukkazanc.orderservice.entity.Order;
import com.dorukkazanc.orderservice.repository.AssetRepository;
import com.dorukkazanc.orderservice.repository.CustomerRepository;
import com.dorukkazanc.orderservice.repository.OrderRepository;
import com.dorukkazanc.orderservice.utils.DynamicQueryBuilder;
import com.dorukkazanc.orderservice.utils.PageableBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final CustomerRepository customerRepository;
    private final AssetRepository assetRepository;
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToCustomerResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CustomerResponseDTO getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        return convertToCustomerResponseDTO(customer);
    }

    public CustomerResponseDTO updateCustomer(Long customerId, CustomerUpdateDTO customerUpdateDTO) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        
        if (customerUpdateDTO.getUsername() != null) {
            customer.setUsername(customerUpdateDTO.getUsername());
        }
        if (customerUpdateDTO.getActive() != null) {
            customer.setActive(customerUpdateDTO.getActive());
        }
        if (customerUpdateDTO.getRole() != null) {
            customer.setRole(customerUpdateDTO.getRole());
        }
        
        Customer savedCustomer = customerRepository.save(customer);
        return convertToCustomerResponseDTO(savedCustomer);
    }

    public void deleteCustomer(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new RuntimeException("Customer not found with id: " + customerId);
        }
        customerRepository.deleteById(customerId);
    }

    @Transactional(readOnly = true)
    public List<AssetResponseDTO> getAllAssets() {
        return assetRepository.findAll().stream()
                .map(this::convertToAssetResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public AssetResponseDTO getAssetById(Long assetId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found with id: " + assetId));
        return convertToAssetResponseDTO(asset);
    }

    public AssetResponseDTO updateAsset(Long assetId, AssetUpdateDTO assetUpdateDTO) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found with id: " + assetId));
        
        if (assetUpdateDTO.getAssetName() != null) {
            asset.setAssetName(assetUpdateDTO.getAssetName());
        }
        if (assetUpdateDTO.getSize() != null) {
            asset.setSize(assetUpdateDTO.getSize());
        }
        if (assetUpdateDTO.getUsableSize() != null) {
            asset.setUsableSize(assetUpdateDTO.getUsableSize());
        }
        
        Asset savedAsset = assetRepository.save(asset);
        return convertToAssetResponseDTO(savedAsset);
    }

    public void deleteAsset(Long assetId) {
        if (!assetRepository.existsById(assetId)) {
            throw new RuntimeException("Asset not found with id: " + assetId);
        }
        assetRepository.deleteById(assetId);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToOrderResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        return convertToOrderResponseDTO(order);
    }

    public OrderResponseDTO updateOrder(Long orderId, OrderUpdateDTO orderUpdateDTO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        if (orderUpdateDTO.getSize() != null) {
            order.setSize(orderUpdateDTO.getSize());
        }
        if (orderUpdateDTO.getPrice() != null) {
            order.setPrice(orderUpdateDTO.getPrice());
        }
        if (orderUpdateDTO.getStatus() != null) {
            order.setStatus(orderUpdateDTO.getStatus());
        }
        
        Order savedOrder = orderRepository.save(order);
        return convertToOrderResponseDTO(savedOrder);
    }

    public void deleteOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new RuntimeException("Order not found with id: " + orderId);
        }
        orderRepository.deleteById(orderId);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponseDTO> searchOrders(DynamicRequestDTO request) {
        Specification<Order> spec = DynamicQueryBuilder.buildSpecification(request);
        Pageable pageable = PageableBuilder.build(request, "createdDate");
        
        if (spec == null) {
            spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        
        Page<Order> orders = orderRepository.findAll(spec, pageable);
        return orders.map(this::convertToOrderResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<AssetResponseDTO> searchAssets(DynamicRequestDTO request) {
        Specification<Asset> spec = DynamicQueryBuilder.buildSpecification(request);
        Pageable pageable = PageableBuilder.build(request, "createdDate");
        
        if (spec == null) {
            spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        
        Page<Asset> assets = assetRepository.findAll(spec, pageable);
        return assets.map(this::convertToAssetResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<CustomerResponseDTO> searchCustomers(DynamicRequestDTO request) {
        Specification<Customer> spec = DynamicQueryBuilder.buildSpecification(request);
        Pageable pageable = PageableBuilder.build(request, "id");
        
        if (spec == null) {
            spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        
        Page<Customer> customers = customerRepository.findAll(spec, pageable);
        return customers.map(this::convertToCustomerResponseDTO);
    }

    // Conversion Methods
    private CustomerResponseDTO convertToCustomerResponseDTO(Customer customer) {
        return CustomerResponseDTO.builder()
                .id(customer.getId())
                .username(customer.getUsername())
                .active(customer.getActive() != null ? customer.getActive() : false)
                .role(customer.getRole())
                .createdDate(customer.getCreatedDate())
                .lastModifiedDate(customer.getLastModifiedDate())
                .build();
    }

    private AssetResponseDTO convertToAssetResponseDTO(Asset asset) {
        return AssetResponseDTO.builder()
                .id(asset.getId())
                .customerId(asset.getCustomerId())
                .assetName(asset.getAssetName())
                .size(asset.getSize())
                .usableSize(asset.getUsableSize())
                .createdDate(asset.getCreatedDate())
                .lastModifiedDate(asset.getLastModifiedDate())
                .build();
    }

    private OrderResponseDTO convertToOrderResponseDTO(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .assetName(order.getAssetName())
                .orderSide(order.getOrderSide())
                .size(order.getSize())
                .price(order.getPrice())
                .status(order.getStatus())
                .createdDate(order.getCreatedDate())
                .lastModifiedDate(order.getLastModifiedDate())
                .build();
    }

    public OrderMatchResponseDTO matchOrder(Long orderId) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        return null;
    }
}