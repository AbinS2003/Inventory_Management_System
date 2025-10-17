package com.example.product_service.exception;

public class InsufficentStockException extends RuntimeException{

    public InsufficentStockException(String productId){
        super("Insufficient stock for product: " + productId);

    }
}
