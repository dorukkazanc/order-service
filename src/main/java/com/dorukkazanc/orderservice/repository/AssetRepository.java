package com.dorukkazanc.orderservice.repository;

import com.dorukkazanc.orderservice.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByCustomerId(String customerId);
}
