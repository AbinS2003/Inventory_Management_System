package com.example.product_service.exception;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(String id) {
        super("Product not found with id: " + id);
    }

    public ProductNotFoundException(ErrorMessage errorMessage, String details) {
        super(errorMessage.getMessage() + details);
    }

}

