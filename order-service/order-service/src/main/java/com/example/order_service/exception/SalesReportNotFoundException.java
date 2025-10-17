package com.example.order_service.exception;

public class SalesReportNotFoundException extends RuntimeException{

    public SalesReportNotFoundException(String message){
        super(message);
    }
}
