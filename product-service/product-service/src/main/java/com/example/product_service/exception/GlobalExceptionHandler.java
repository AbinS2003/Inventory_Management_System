package com.example.product_service.exception;

import com.example.product_service.dto.ApiResponse;
import jakarta.validation.ConstraintViolationException;
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
    public ResponseEntity<ApiResponse<Object>> handleNotFound(ProductNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>
                        (HttpStatus.NOT_FOUND.value(),
                        ex.getMessage(),
                        null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationErrors(MethodArgumentNotValidException ex){

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
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


    @ExceptionHandler(InsufficentStockException.class)
    public ResponseEntity<ApiResponse<Object>> handleInsufficentStock(InsufficentStockException ex){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                null
        ));
    }

    @ExceptionHandler(ProductsNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleProductsNotFound(ProductsNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleConstraintViolation(ConstraintViolationException ex){

        Map<String, String> response = new HashMap<>();

        String errorMessage = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getMessage())
                .findFirst()
                .orElse("Validation failed");
        response.put("error", errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                null
        ));

    }
}
