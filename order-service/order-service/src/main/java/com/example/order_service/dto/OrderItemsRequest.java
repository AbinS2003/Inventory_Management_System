package com.example.order_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class OrderItemsRequest {

    @NotBlank(message = "Product ID is required")
    private String variantId;
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    public OrderItemsRequest() {
    }

    public OrderItemsRequest(String variantId, int quantity) {
        this.variantId = variantId;
        this.quantity = quantity;
    }

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
