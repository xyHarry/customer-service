package com.harry.customerservice.repository;

import com.harry.customerservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    // Additional query methods (if needed) can be added here
}
