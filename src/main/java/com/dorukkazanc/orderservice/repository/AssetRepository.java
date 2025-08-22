package com.dorukkazanc.orderservice.repository;

import com.dorukkazanc.orderservice.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByCustomerId(String customerId);
    Optional<Asset> findByCustomerIdAndAssetName(String customerId, String assetName);
}
