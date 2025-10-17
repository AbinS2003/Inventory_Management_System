package com.example.order_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class OrderItemsRequest {

    @NotBlank(message = "Product ID is required")
    private String productId;
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    public OrderItemsRequest() {
    }

    public OrderItemsRequest(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
