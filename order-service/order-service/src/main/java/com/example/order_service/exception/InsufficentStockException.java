package com.example.order_service.exception;

public class InsufficentStockException extends RuntimeException{

    public InsufficentStockException(String id){
        super("Insufficient stock for product " + id);
    }
}
