package com.example.order_service.service;

import com.example.order_service.dto.ApiResponse;
import com.example.order_service.dto.CustomerRequestDTO;
import com.example.order_service.dto.CustomerResponseDTO;
import com.example.order_service.exception.CustomerNotFoundException;
import com.example.order_service.model.Customer;
import com.example.order_service.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Autowired
    PasswordEncoder passwordEncoder;


    public ResponseEntity<ApiResponse<CustomerResponseDTO>> createCustomer(@Valid CustomerRequestDTO customerRequest) {

        Customer customer = new Customer();

        customer.setName(customerRequest.getName());
        customer.setEmail(customerRequest.getEmail());
        customer.setPassword(passwordEncoder.encode(customerRequest.getPassword()));

        Customer savedCustomer = customerRepository.save(customer);

        CustomerResponseDTO customerResponse = new CustomerResponseDTO(

                savedCustomer.getId(),
                savedCustomer.getName(),
                savedCustomer.getEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Customer Created",
                customerResponse
        ));
    }


    public ResponseEntity<ApiResponse<CustomerResponseDTO>> getCustomerById(String id) {

        Optional<Customer> customerOpt = customerRepository.findById(id);

        if (customerOpt.isEmpty()){
            throw new CustomerNotFoundException(id);
        }

        Customer customer = customerOpt.get();

        CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail()
        );

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Customer fetched",
                customerResponseDTO
        ));
    }
}
