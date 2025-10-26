package com.example.order_service.exception;

public class CustomerNotFoundException extends RuntimeException{

    public CustomerNotFoundException(String id){
        super("Customer does not exist with id " + id);
    }
}
