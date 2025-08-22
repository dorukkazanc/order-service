package com.dorukkazanc.orderservice.repository;

import com.dorukkazanc.orderservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findCustomerByUsernameAndPassword(String username, String password);
    Optional<Customer> findCustomerByUsername(String username);

}
