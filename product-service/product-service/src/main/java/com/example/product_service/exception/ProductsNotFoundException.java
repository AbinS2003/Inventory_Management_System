package com.example.product_service.exception;

public class ProductsNotFoundException extends RuntimeException{

    public ProductsNotFoundException(ErrorMessage errorMessage){
        super(errorMessage.getMessage());
    }
}
