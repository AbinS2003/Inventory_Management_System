package com.example.product_service.controller;

import com.example.product_service.dto.ApiResponse;
import com.example.product_service.dto.VariantResponse;
import com.example.product_service.repository.VariantRepository;
import com.example.product_service.service.VariantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/variants")
@Tag(
        name = "variant controller",
        description = "Handles CRUD operations for variant"
)
public class VariantController {

    private final VariantService variantService;

    public VariantController(VariantService variantService) {
        this.variantService = variantService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VariantResponse>> getVariantById(@Parameter(description = "Unique ID of the variant to fetch") @PathVariable String id){

        return variantService.getVariantById(id);

    }


    @PutMapping("/reduce/{id}")
    @Operation(
            summary = "Reduce product stock ",
            description = "Reduces the quantity of a specific product by the given amount."
    ) @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Stock reduced successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error - Failed to fetch products"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Insufficent stock")
    })
    public ResponseEntity<ApiResponse<VariantResponse>> updateQuantity(@Parameter(description = "Unique ID of the variant to update")@PathVariable String id,
                                                                       @RequestParam @Min(value = 1, message = "Quantity must be at least 1")
                                                                       int quantity){

        return variantService.updateQuantity(id, quantity);
    }

}
