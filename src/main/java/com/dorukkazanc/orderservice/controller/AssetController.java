package com.dorukkazanc.orderservice.controller;


import com.dorukkazanc.orderservice.dto.AssetResponseDTO;
import com.dorukkazanc.orderservice.dto.BaseResponse;
import com.dorukkazanc.orderservice.dto.DynamicRequestDTO;
import com.dorukkazanc.orderservice.entity.Customer;
import com.dorukkazanc.orderservice.service.AssetService;
import com.dorukkazanc.orderservice.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {
    private final AssetService assetService;
    private final ResponseService responseService;

    @PostMapping("/search")
    public ResponseEntity<BaseResponse<List<AssetResponseDTO>>> getAllAssets(@RequestBody DynamicRequestDTO dto, Authentication authentication) {
        try {
            Customer customer = (Customer) authentication.getPrincipal();
            List<AssetResponseDTO> assets = assetService.searchAssetsByCustomerId(customer.getId(), dto).getContent();
            return responseService.success(assets, "Assets retrieved successfully");
        } catch (Exception e) {
            return responseService.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
