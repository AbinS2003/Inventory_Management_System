package com.example.product_service.exception;

public class VariantNotFoundException extends RuntimeException{

    public VariantNotFoundException(String id){
        super("Variant not foundwith id " + id);
    }
}
