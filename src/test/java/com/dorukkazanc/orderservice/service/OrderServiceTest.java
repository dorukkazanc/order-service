package com.dorukkazanc.orderservice.service;

import com.dorukkazanc.orderservice.dto.OrderRequestDTO;
import com.dorukkazanc.orderservice.dto.OrderResponseDTO;
import com.dorukkazanc.orderservice.dto.OrderUpdateDTO;
import com.dorukkazanc.orderservice.entity.Asset;
import com.dorukkazanc.orderservice.entity.Order;
import com.dorukkazanc.orderservice.enums.OrderSide;
import com.dorukkazanc.orderservice.enums.OrderStatus;
import com.dorukkazanc.orderservice.exception.InsufficientAssetException;
import com.dorukkazanc.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AssetService assetService;

    @InjectMocks
    private OrderService orderService;

    private Order testOrder;
    private Asset testAsset;
    private OrderRequestDTO orderRequestDTO;

    @BeforeEach
    void setUp() {
        testOrder = Order.builder()
                .id(1L)
                .customerId("123")
                .assetName("BTC")
                .orderSide(OrderSide.SELL)
                .size(10L)
                .price(BigDecimal.valueOf(50000))
                .status(OrderStatus.PENDING)
                .build();

        testAsset = Asset.builder()
                .id(1L)
                .customerId("123")
                .assetName("BTC")
                .size(100L)
                .usableSize(100L)
                .build();

        orderRequestDTO = OrderRequestDTO.builder()
                .assetName("BTC")
                .orderSide(OrderSide.SELL)
                .size(10L)
                .price(BigDecimal.valueOf(50000))
                .build();
    }

    @Test
    void createOrder_ShouldCreateOrderSuccessfully() {
        when(assetService.getAssetByCustomerIdAndName(123L, "BTC")).thenReturn(testAsset);
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        doNothing().when(assetService).updateAssetForOrder(any(Order.class), any(Asset.class));

        OrderResponseDTO result = orderService.createOrder(123L, orderRequestDTO);

        assertNotNull(result);
        assertEquals("BTC", result.getAssetName());
        assertEquals(OrderSide.SELL, result.getOrderSide());
        verify(orderRepository).save(any(Order.class));
        verify(assetService).updateAssetForOrder(any(Order.class), eq(testAsset));
    }

    @Test
    void createOrder_ShouldThrowException_WhenAssetInsufficient() {
        Asset insufficientAsset = Asset.builder()
                .id(1L)
                .customerId("123")
                .assetName("BTC")
                .size(5L)
                .usableSize(5L)
                .build();

        when(assetService.getAssetByCustomerIdAndName(123L, "BTC")).thenReturn(insufficientAsset);

        assertThrows(InsufficientAssetException.class, () -> {
            orderService.createOrder(123L, orderRequestDTO);
        });

        verify(orderRepository, never()).save(any());
        verify(assetService, never()).updateAssetForOrder(any(), any());
    }

    @Test
    void getAllOrders_ShouldReturnAllOrders() {
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findAll()).thenReturn(orders);

        List<OrderResponseDTO> result = orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("BTC", result.get(0).getAssetName());
        verify(orderRepository).findAll();
    }

    @Test
    void getOrderById_ShouldReturnOrder_WhenExists() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        Optional<OrderResponseDTO> result = orderService.getOrderById(1L);

        assertTrue(result.isPresent());
        assertEquals("BTC", result.get().getAssetName());
        verify(orderRepository).findById(1L);
    }

    @Test
    void getOrderById_ShouldReturnEmpty_WhenOrderNotFound() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<OrderResponseDTO> result = orderService.getOrderById(999L);

        assertFalse(result.isPresent());
        verify(orderRepository).findById(999L);
    }

    @Test
    void updateOrder_ShouldUpdateOrderSuccessfully() {
        OrderUpdateDTO updateDTO = OrderUpdateDTO.builder()
                .size(20L)
                .price(BigDecimal.valueOf(60000))
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Optional<OrderResponseDTO> result = orderService.updateOrder(1L, updateDTO);

        assertTrue(result.isPresent());
        verify(orderRepository).save(testOrder);
    }

    @Test
    void deleteOrder_ShouldReturnTrue_WhenOrderExistsAndPending() {
        when(orderRepository.findOrderByIdAndCustomerId(1L, "123")).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        boolean result = orderService.deleteOrder(1L, 123L);

        assertTrue(result);
        assertEquals(OrderStatus.CANCELED, testOrder.getStatus());
        verify(orderRepository).save(testOrder);
    }

    @Test
    void deleteOrder_ShouldReturnFalse_WhenOrderNotExists() {
        when(orderRepository.findOrderByIdAndCustomerId(999L, "123")).thenReturn(Optional.empty());

        boolean result = orderService.deleteOrder(999L, 123L);

        assertFalse(result);
        verify(orderRepository, never()).save(any());
    }
} 