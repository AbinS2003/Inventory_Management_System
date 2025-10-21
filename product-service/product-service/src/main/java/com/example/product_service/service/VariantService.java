package com.example.product_service.service;

import com.example.product_service.dto.ApiResponse;
import com.example.product_service.dto.VariantResponse;
import com.example.product_service.exception.InsufficentStockException;
import com.example.product_service.exception.VariantNotFoundException;
import com.example.product_service.model.Variant;
import com.example.product_service.repository.VariantRepository;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VariantService {

    private final VariantRepository variantRepository;

    public VariantService(VariantRepository variantRepository) {
        this.variantRepository = variantRepository;
    }

    public ResponseEntity<ApiResponse<VariantResponse>> getVariantById(String id) {

        Variant variant = variantRepository.findById(id)
                .orElseThrow(() -> new VariantNotFoundException(id));

        VariantResponse variantResponse = new VariantResponse();
        variantResponse.setVariantId(variant.getId());
        variantResponse.setProductId(variant.getProductId());
        variantResponse.setAttributes(variant.getAttributes());
        variantResponse.setPrice(variant.getPrice());
        variantResponse.setQuantity(variant.getQuantity());

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Variant fetched",
                variantResponse
        ));
    }

    public ResponseEntity<ApiResponse<VariantResponse>> updateQuantity(String id, @Min(value = 1, message = "Quantity must be at least 1")
                                                                        int quantity) {

        Variant variant = variantRepository.findById(id)
                .orElseThrow(() -> new VariantNotFoundException(id));

        if (variant.getQuantity() < quantity) {
            throw new InsufficentStockException(variant.getId());
        }

        variant.setQuantity(variant.getQuantity() - quantity);
        Variant savedVariant = variantRepository.save(variant);

        VariantResponse response = new VariantResponse();
        response.setVariantId(savedVariant.getId());
        response.setProductId(savedVariant.getProductId());
        response.setAttributes(savedVariant.getAttributes());
        response.setPrice(savedVariant.getPrice());
        response.setQuantity(savedVariant.getQuantity());

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Variant quantity updated successfully",
                response
        ));

    }


}
