package com.example.product_service.model;

import org.springframework.data.annotation.Id;

import java.util.Map;

public class Variant {

    @Id
    private String id;
    private String productId;
    private Map<String, String> attributes;
    private double price;
    private int quantity;

    public Variant() {
    }

    public Variant(String id, String productId, Map<String, String> variant, double price, int quantity) {
        this.id = id;
        this.productId = productId;
        this.attributes = variant;
        this.price = price;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

}
