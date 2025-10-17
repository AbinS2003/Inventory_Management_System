package com.example.product_service.exception;

public enum ErrorMessage {

    PRODUCT_NOT_FOUND("No products found"),
    NO_RESULTS_FOUND("No results found");

    private final String message;


    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
