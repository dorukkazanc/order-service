package com.dorukkazanc.orderservice.repository;

import com.dorukkazanc.orderservice.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long>, JpaSpecificationExecutor<Asset> {
    Optional<Asset> findByCustomerIdAndAssetName(String customerId, String assetName);
}
