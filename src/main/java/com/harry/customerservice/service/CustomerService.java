package com.harry.customerservice.service;

import com.harry.customerservice.model.Customer;
import com.harry.customerservice.repository.CustomerRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;
    private final Counter customerCreateCounter;
    private final Counter customerReadCounter;
    private final Counter customerUpdateCounter;
    private final Counter customerDeleteCounter;
    private final Counter customerErrorCounter;
    private final Timer customerCreateTimer;
    private final Timer customerGetAllTimer;
    private final Timer customerReadTimer;
    private final Timer customerUpdateTimer;
    private final Timer customerDeleteTimer;

    public CustomerService(CustomerRepository customerRepository, MeterRegistry meterRegistry) {
        this.customerRepository = customerRepository;

        // Initializing counters for each operation
        this.customerCreateCounter = meterRegistry.counter("customer.create.count");
        this.customerReadCounter = meterRegistry.counter("customer.read.count");
        this.customerUpdateCounter = meterRegistry.counter("customer.update.count");
        this.customerDeleteCounter = meterRegistry.counter("customer.delete.count");
        this.customerErrorCounter = meterRegistry.counter("customer.error.count");

        // Initializing timers for each operation
        this.customerCreateTimer = meterRegistry.timer("customer.create.time");
        this.customerGetAllTimer = meterRegistry.timer("customer.getAll.time");
        this.customerReadTimer = meterRegistry.timer("customer.read.time");
        this.customerUpdateTimer = meterRegistry.timer("customer.update.time");
        this.customerDeleteTimer = meterRegistry.timer("customer.delete.time");
    }

    // CREATE
    @Transactional
    public Customer createCustomer(Customer customer) {
        return customerCreateTimer.record(() -> {
            try {
                Customer savedCustomer = customerRepository.save(customer);
                customerCreateCounter.increment();
                return savedCustomer;
            } catch (Exception e) {
                customerErrorCounter.increment();
                logger.error("Error creating customer", e);
                throw e;
            }
        });
    }

    // READ all customers
    public List<Customer> getAllCustomers() {
        return customerGetAllTimer.record(() -> {
            try {
                List<Customer> customers = customerRepository.findAll();
                return customers;
            } catch (Exception e) {
                customerErrorCounter.increment();
                logger.error("Error retrieving all customers", e);
                throw e;
            }
        });
    }

    // READ
    public Optional<Customer> getCustomerById(UUID customerId) {
        return customerReadTimer.record(() -> {
            try {
                Optional<Customer> customer = customerRepository.findById(customerId);
                customerReadCounter.increment();
                return customer;
            } catch (Exception e) {
                customerErrorCounter.increment();
                logger.error("Error retrieving customer by ID", e);
                throw e;
            }
        });
    }

    // UPDATE
    @Transactional
    public Customer updateCustomer(UUID customerId, Customer updatedCustomer) {
        return customerUpdateTimer.record(() -> {
            try {
                Optional<Customer> existingCustomerOpt = customerRepository.findById(customerId);
                if (existingCustomerOpt.isEmpty()) {
                    throw new RuntimeException("Customer not found");
                }

                Customer existingCustomer = existingCustomerOpt.get();
                existingCustomer.setFirstName(updatedCustomer.getFirstName());
                existingCustomer.setLastName(updatedCustomer.getLastName());
                existingCustomer.setEmailAddress(updatedCustomer.getEmailAddress());
                existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());

                Customer savedCustomer = customerRepository.save(existingCustomer);
                customerUpdateCounter.increment();
                return savedCustomer;
            } catch (Exception e) {
                customerErrorCounter.increment();
                logger.error("Error updating customer", e);
                throw e;
            }
        });
    }

    // DELETE
    @Transactional
    public void deleteCustomer(UUID customerId) {
        customerDeleteTimer.record(() -> {
            try {
                if (customerRepository.existsById(customerId)) {
                    customerRepository.deleteById(customerId);
                    customerDeleteCounter.increment();
                } else {
                    throw new RuntimeException("Customer not found");
                }
            } catch (Exception e) {
                customerErrorCounter.increment();
                logger.error("Error deleting customer", e);
                throw e;
            }
        });
    }
}

