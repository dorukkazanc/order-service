package com.dorukkazanc.orderservice.service;

import com.dorukkazanc.orderservice.dto.CustomerResponseDTO;
import com.dorukkazanc.orderservice.dto.CustomerUpdateDTO;
import com.dorukkazanc.orderservice.dto.LoginRequestDTO;
import com.dorukkazanc.orderservice.dto.LoginResponseDTO;
import com.dorukkazanc.orderservice.entity.Customer;
import com.dorukkazanc.orderservice.enums.UserRole;
import com.dorukkazanc.orderservice.repository.CustomerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO, HttpServletRequest request) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        HttpSession session = request.getSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());

        Customer customer = (Customer) authentication.getPrincipal();
        return new LoginResponseDTO(
                customer.getId().toString(),
                customer.getUsername(),
                customer.getRole(),
                customer.getActive()
        );
    }
    
    public CustomerResponseDTO createAdmin(CustomerUpdateDTO customerUpdateDTO) {
        if (customerUpdateDTO.getUsername() == null) {
            throw new RuntimeException("Username is required");
        }
        
        if (customerRepository.findCustomerByUsername(customerUpdateDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        
        Customer admin = Customer.builder()
                .username(customerUpdateDTO.getUsername())
                .password(passwordEncoder.encode("admin123")) // Default password for admin
                .active(true)
                .role(UserRole.ADMIN)
                .build();
        
        Customer savedAdmin = customerRepository.save(admin);
        
        return CustomerResponseDTO.builder()
                .id(savedAdmin.getId())
                .username(savedAdmin.getUsername())
                .active(savedAdmin.getActive())
                .role(savedAdmin.getRole())
                .createdDate(savedAdmin.getCreatedDate())
                .lastModifiedDate(savedAdmin.getLastModifiedDate())
                .build();
    }
}