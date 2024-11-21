package com.harry.customerservice.controller;

import com.harry.customerservice.model.Customer;
import com.harry.customerservice.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        try {
            Customer createdCustomer = customerService.createCustomer(customer);
            return ResponseEntity.created(URI.create("/api/customers/" + createdCustomer.getId())).body(createdCustomer);
        } catch (Exception e) {
            logger.error("Error creating customer", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // READ all customers
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        try {
            List<Customer> customers = customerService.getAllCustomers();
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            logger.error("Error retrieving customers", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // READ by ID
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable UUID id) {
        try {
            Optional<Customer> customer = customerService.getCustomerById(id);
            return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error retrieving customer by ID", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable UUID id, @RequestBody Customer updatedCustomer) {
        try {
            Customer customer = customerService.updateCustomer(id, updatedCustomer);
            return ResponseEntity.ok(customer);
        } catch (RuntimeException e) {
            logger.warn("Customer not found", e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error updating customer", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.warn("Customer not found", e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error deleting customer", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
