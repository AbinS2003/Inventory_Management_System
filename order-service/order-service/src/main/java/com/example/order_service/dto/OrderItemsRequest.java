package com.example.order_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class OrderItemsRequest {

    @NotBlank(message = "Product ID is required")
    private String variantId;
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
    @Valid
    private List<AddonRequest> addons;

    public OrderItemsRequest() {
    }

    public OrderItemsRequest(String variantId, int quantity, List<AddonRequest> addons) {
        this.variantId = variantId;
        this.quantity = quantity;
        this.addons = addons;
    }

    public List<AddonRequest> getAddons() {
        return addons;
    }

    public void setAddons(List<AddonRequest> addons) {
        this.addons = addons;
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
