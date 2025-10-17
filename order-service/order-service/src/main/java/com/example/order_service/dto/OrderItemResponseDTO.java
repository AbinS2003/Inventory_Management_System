package com.example.order_service.dto;

public class OrderItemResponseDTO {

    private String productId;
    private String productName;
    private int qty;
    private double price;
    private String category;
    private double totalPrice;

    public OrderItemResponseDTO() {
    }

    public OrderItemResponseDTO(String productId, String productName, int qty, double price, String category, double totalPrice) {
        this.productId = productId;
        this.productName = productName;
        this.qty = qty;
        this.price = price;
        this.category = category;
        this.totalPrice = totalPrice;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
