/**
 * Author: doruk.kazanc
 * Date: 22.08.2025
 */

package com.dorukkazanc.orderservice.service;

import com.dorukkazanc.orderservice.dto.LoginRequestDTO;
import com.dorukkazanc.orderservice.dto.LoginResponseDTO;
import com.dorukkazanc.orderservice.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.channels.FileChannel;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Optional<LoginResponseDTO> login(LoginRequestDTO loginRequestDTO) {
        return customerRepository.findCustomerByUsernameAndPassword(
                loginRequestDTO.getUsername(), loginRequestDTO.getPassword()
        ).map(customer -> new LoginResponseDTO(
                customer.getId().toString(),
                customer.getUsername(),
                customer.getRole(),
                customer.getActive()
        ));
    }
}
