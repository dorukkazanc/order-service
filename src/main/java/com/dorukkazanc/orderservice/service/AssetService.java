/**
 * Author: doruk.kazanc
 * Date: 22.08.2025
 */

package com.dorukkazanc.orderservice.service;


import com.dorukkazanc.orderservice.entity.Asset;
import com.dorukkazanc.orderservice.entity.Order;
import com.dorukkazanc.orderservice.enums.OrderSide;
import com.dorukkazanc.orderservice.exception.InsufficientAssetException;
import com.dorukkazanc.orderservice.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AssetService {
    private final AssetRepository assetRepository;


    @Transactional(readOnly = true)
    Asset getAssetByCustomerIdAndName(Long id, String assetName) {
        return assetRepository.findByCustomerIdAndAssetName(id.toString(), assetName).orElse(null);
    }
    public void updateAssetForOrder(Order order, Asset asset) {
        if (asset == null) {
            throw new InsufficientAssetException("Asset not found for the customer");
        }

        if (order.getOrderSide().equals(OrderSide.BUY)) {
            BigDecimal totalCost = order.getPrice().multiply(BigDecimal.valueOf(order.getSize()));
            asset.setUsableSize(asset.getUsableSize() - totalCost.longValue());

        } else if (order.getOrderSide().equals(OrderSide.SELL)) {
            asset.setUsableSize(asset.getUsableSize() - order.getSize());
        }

        assetRepository.save(asset);
    }
}
