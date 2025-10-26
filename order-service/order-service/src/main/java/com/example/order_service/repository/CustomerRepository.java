package com.example.order_service.repository;

import com.example.order_service.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {

    //for sample data loading
    Optional<Customer> findByName(String name);
}
