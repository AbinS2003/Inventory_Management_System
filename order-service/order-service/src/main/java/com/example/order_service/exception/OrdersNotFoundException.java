package com.example.order_service.exception;

public class OrdersNotFoundException extends RuntimeException{

    public OrdersNotFoundException(ErrorMessage errorMessage){
        super(errorMessage.getMessages());
    }
}
