package com.example.product_service.exception;

public enum ErrorMessage {

    PRODUCT_NOT_FOUND("No products found"),
    NO_RESULTS_FOUND("No results found"),
    VARIANT_DOES_NOT_BELONG_TO_PRODUCT("Variant does not belong to this product"),
    ADDON_PRODUCT_NOT_FOUND("Addon product(s) not found with id(s): ");

    private final String message;


    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
