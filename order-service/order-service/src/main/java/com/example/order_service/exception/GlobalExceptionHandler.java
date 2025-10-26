package com.example.order_service.exception;

import com.example.order_service.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleProductNotFound(ProductNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                null
        ));
    }

    @ExceptionHandler(InsufficentStockException.class)
    public ResponseEntity<ApiResponse<Object>> InsufficentStock(InsufficentStockException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                null
        ));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "Validation error",
                errors
        ));

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidFormat(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponse<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Invalid or missing field in request body",
                        ex.getMostSpecificCause().getMessage()
                )
        );
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleOrderNotFound(OrderNotFoundException ex){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                null
        ));
    }

    @ExceptionHandler(OrdersNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleOrdersNotFound(OrdersNotFoundException ex){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                null
        ));
    }

    @ExceptionHandler(SalesReportNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleSalesReportNotFound(SalesReportNotFoundException ex){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                null
        ));
    }

    @ExceptionHandler(VariantNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleVariantNotFound(VariantNotFoundException ex){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                null
        ));
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomerNotFound(CustomerNotFoundException ex){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                null
        ));
    }
}


