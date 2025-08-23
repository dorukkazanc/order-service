/**
 * Author: doruk.kazanc
 * Date: 22.08.2025
 */

package com.dorukkazanc.orderservice.service;


import com.dorukkazanc.orderservice.dto.AssetResponseDTO;
import com.dorukkazanc.orderservice.dto.DynamicRequestDTO;
import com.dorukkazanc.orderservice.entity.Asset;
import com.dorukkazanc.orderservice.entity.Order;
import com.dorukkazanc.orderservice.enums.OrderSide;
import com.dorukkazanc.orderservice.exception.InsufficientAssetException;
import com.dorukkazanc.orderservice.repository.AssetRepository;
import com.dorukkazanc.orderservice.utils.DynamicQueryBuilder;
import com.dorukkazanc.orderservice.utils.PageableBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AssetService {
    private final AssetRepository assetRepository;


    @Transactional(readOnly = true)
    public Asset getAssetByCustomerIdAndName(Long id, String assetName) {
        return assetRepository.findByCustomerIdAndAssetName(id.toString(), assetName).orElse(null);
    }
    
    @Transactional(readOnly = true)
    public Page<AssetResponseDTO> searchAssetsByCustomerId(Long customerId, DynamicRequestDTO request) {
        Specification<Asset> spec = DynamicQueryBuilder.buildSpecification(request);

        Pageable pageable = PageableBuilder.build(request, "createdDate");

        spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("customerId"), customerId.toString()));

        Page<Asset> assets = assetRepository.findAll(spec, pageable);
        return assets.map(this::convertToResponseDTO);
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

    private AssetResponseDTO convertToResponseDTO(Asset asset) {
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
}
