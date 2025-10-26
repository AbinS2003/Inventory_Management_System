package com.example.order_service.dto;

import java.util.Map;

public class VariantResponseDTO {

    private String variantId;
    private String productId;
    private Map<String, String> attributes;
    private double price;
    private int quantity;

    public VariantResponseDTO() {
    }

    public VariantResponseDTO(String variantId, String productId, Map<String, String> attributes, double price, int quantity) {
        this.variantId = variantId;
        this.productId = productId;
        this.attributes = attributes;
        this.price = price;
        this.quantity = quantity;
    }

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
