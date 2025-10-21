package com.example.order_service.exception;

public class VariantNotFoundException extends RuntimeException{

    public VariantNotFoundException(String id){
        super("Variant not found with id " + id);
    }
}
