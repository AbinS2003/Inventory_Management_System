package com.example.order_service.model;

import java.util.Map;

public class OrderItem {

    private String productId;
    private String variantId;
    private String productName;
    private String category;
    private Map<String, String> attributes;
    private int qty;
    private double price;
    private double totalPrice;

    public OrderItem() {
    }

    public OrderItem(String productId, String variantId, String productName, String category, Map<String, String> attributes, int qty, double price, double totalPrice) {
        this.productId = productId;
        this.variantId = variantId;
        this.productName = productName;
        this.category = category;
        this.attributes = attributes;
        this.qty = qty;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
