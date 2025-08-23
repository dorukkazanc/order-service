/**
 * Author: doruk.kazanc
 * Date: 22.08.2025
 */

package com.dorukkazanc.orderservice.controller;

import com.dorukkazanc.orderservice.dto.BaseResponse;
import com.dorukkazanc.orderservice.dto.CustomerResponseDTO;
import com.dorukkazanc.orderservice.dto.CustomerUpdateDTO;
import com.dorukkazanc.orderservice.dto.LoginRequestDTO;
import com.dorukkazanc.orderservice.dto.LoginResponseDTO;
import com.dorukkazanc.orderservice.service.AuthService;
import com.dorukkazanc.orderservice.service.ResponseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ResponseService responseService;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO,
            HttpServletRequest request) {
        try {
            LoginResponseDTO response = authService.login(loginRequestDTO, request);
            return responseService.success(response, "Login successful");
        } catch (AuthenticationException e) {
            return responseService.unauthorized("Invalid username or password");
        }
    }
    
    @PostMapping("/create-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<CustomerResponseDTO>> createAdmin(
            @Valid @RequestBody CustomerUpdateDTO customerUpdateDTO) {
        try {
            CustomerResponseDTO admin = authService.createAdmin(customerUpdateDTO);
            return responseService.success(admin, "Admin user created successfully");
        } catch (Exception e) {
            return responseService.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}