package com.example.order_service.exception;

public enum ErrorMessage {

    NO_ORDERS("No Orders found");

    private final String messages;

    ErrorMessage(String messages) {
        this.messages = messages;
    }

    public String getMessages() {
        return messages;
    }
}
