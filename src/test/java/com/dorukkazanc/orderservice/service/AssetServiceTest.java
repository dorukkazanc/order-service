package com.dorukkazanc.orderservice.service;

import com.dorukkazanc.orderservice.entity.Asset;
import com.dorukkazanc.orderservice.entity.Order;
import com.dorukkazanc.orderservice.enums.OrderSide;
import com.dorukkazanc.orderservice.exception.InsufficientAssetException;
import com.dorukkazanc.orderservice.repository.AssetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private AssetService assetService;

    private Asset testAsset;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        testAsset = Asset.builder()
                .id(1L)
                .customerId("123")
                .assetName("BTC")
                .size(100L)
                .usableSize(100L)
                .build();

        testOrder = Order.builder()
                .id(1L)
                .customerId("123")
                .assetName("BTC")
                .orderSide(OrderSide.SELL)
                .size(10L)
                .price(BigDecimal.valueOf(50000))
                .build();
    }

    @Test
    void getAssetByCustomerIdAndName_ShouldReturnAsset() {
        when(assetRepository.findByCustomerIdAndAssetName("123", "BTC"))
                .thenReturn(Optional.of(testAsset));

        Asset result = assetService.getAssetByCustomerIdAndName(123L, "BTC");

        assertNotNull(result);
        assertEquals("BTC", result.getAssetName());
        assertEquals("123", result.getCustomerId());
        verify(assetRepository).findByCustomerIdAndAssetName("123", "BTC");
    }

    @Test
    void getAssetByCustomerIdAndName_ShouldReturnNull_WhenAssetNotFound() {
        when(assetRepository.findByCustomerIdAndAssetName("123", "BTC"))
                .thenReturn(Optional.empty());

        Asset result = assetService.getAssetByCustomerIdAndName(123L, "BTC");

        assertNull(result);
        verify(assetRepository).findByCustomerIdAndAssetName("123", "BTC");
    }

    @Test
    void updateAssetForOrder_ShouldUpdateAssetForSellOrder() {
        when(assetRepository.save(any(Asset.class))).thenReturn(testAsset);

        assetService.updateAssetForOrder(testOrder, testAsset);

        assertEquals(90L, testAsset.getUsableSize());
        verify(assetRepository).save(testAsset);
    }

    @Test
    void updateAssetForOrder_ShouldThrowException_WhenAssetIsNull() {
        assertThrows(InsufficientAssetException.class, () -> {
            assetService.updateAssetForOrder(testOrder, null);
        });

        verify(assetRepository, never()).save(any());
    }
} 