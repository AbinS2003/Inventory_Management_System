package com.example.order_service.controller;

import com.example.order_service.dto.ApiResponse;
import com.example.order_service.dto.CustomerRequestDTO;
import com.example.order_service.dto.CustomerResponseDTO;
import com.example.order_service.model.Customer;
import com.example.order_service.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/customers")
@Tag(name = "customer controller",
        description = "Handle crud operations for customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/signup")
    @Operation(summary = "Create a customer",
            description = "Creates a new customer and save to database")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Customer created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request - missing required fields or invalid data format"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> createCustomer(@Valid @RequestBody CustomerRequestDTO customerRequest){

       return customerService.createCustomer(customerRequest);


    }

    @GetMapping("/{id}")
    @Operation(summary = "Fetch a customer by Id",
            description = "Retrieves customer details from the database using the provided customer ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Customer fetched successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Customer not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> getCustomerById(@Parameter(description = "Unique ID of the customer to fetch") @PathVariable String id){

        return customerService.getCustomerById(id);
    }

}
