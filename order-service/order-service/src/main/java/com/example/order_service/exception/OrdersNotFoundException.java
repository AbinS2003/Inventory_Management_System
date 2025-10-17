package com.example.order_service.exception;

public class OrdersNotFoundException extends RuntimeException{

    public OrdersNotFoundException(String message){
        super(message);
    }
}
