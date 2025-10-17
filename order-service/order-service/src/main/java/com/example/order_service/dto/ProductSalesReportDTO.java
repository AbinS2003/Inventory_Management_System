package com.example.order_service.dto;

public class ProductSalesReportDTO {

    private String productId;
    private String productName;
    private String category;
    private int totalQuantity;
    private double totalAmount;


    public ProductSalesReportDTO() {
    }

    public ProductSalesReportDTO(String productId, String productName, String category, int totalQuantity, double totalAmount) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
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

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
