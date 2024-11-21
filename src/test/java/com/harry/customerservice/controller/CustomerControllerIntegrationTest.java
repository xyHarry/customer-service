package com.harry.customerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harry.customerservice.model.Customer;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldCreateCustomerAndVerifyDataUsingGet() throws Exception {
        Customer customer = new Customer();
        customer.setFirstName("Harry");
        customer.setLastName("Xu");
        customer.setEmailAddress("hxu@example.com");
        customer.setPhoneNumber("123-456-7890");

        // Step 1: Create the customer using POST and retrieve the ID
        MvcResult postResult = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists()) // Ensure that ID is returned
                .andReturn();

        // Extract the generated customer ID from the POST response
        String customerId = JsonPath.parse(postResult.getResponse().getContentAsString()).read("$.id");

        // Step 2: Retrieve the customer using GET and verify the data
        mockMvc.perform(get("/api/customers/{id}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Harry"))
                .andExpect(jsonPath("$.lastName").value("Xu"))
                .andExpect(jsonPath("$.emailAddress").value("hxu@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("123-456-7890"));
    }

}
