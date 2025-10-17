package com.example.order_service.model;

public class OrderItem {

    private String productId;
    private String productName;
    private int qty;
    private double price;
    private String category;
    private double totalPrice;

    public OrderItem() {
    }

    public OrderItem(String productId, String productName, int qty, double price, String category, double totalPrice) {
        this.productId = productId;
        this.productName = productName;
        this.qty = qty;
        this.price = price;
        this.category = category;
        this.totalPrice = totalPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public void setTotalAmount(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
