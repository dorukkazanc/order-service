package com.dorukkazanc.orderservice.repository;

import com.dorukkazanc.orderservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
