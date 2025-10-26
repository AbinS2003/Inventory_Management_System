package com.example.order_service.dto;

import java.util.List;
import java.util.Map;

public class OrderItemResponseDTO {

    private String productId;
    private String productName;
    private String variantId;
    private Map<String, String> attributes;
    private int qty;
    private double price;
    private String category;
    private double totalPrice;
    private List<AddonItemsResponseDTO> addons;

    public OrderItemResponseDTO() {
    }

    public OrderItemResponseDTO(String productId, String productName, String variantId, Map<String, String> attributes, int qty, double price, String category, double totalPrice, List<AddonItemsResponseDTO> addons) {
        this.productId = productId;
        this.productName = productName;
        this.variantId = variantId;
        this.attributes = attributes;
        this.qty = qty;
        this.price = price;
        this.category = category;
        this.totalPrice = totalPrice;
        this.addons = addons;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<AddonItemsResponseDTO> getAddons() {
        return addons;
    }

    public void setAddons(List<AddonItemsResponseDTO> addons) {
        this.addons = addons;
    }
}
