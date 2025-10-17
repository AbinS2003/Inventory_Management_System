package com.example.order_service.dto;

public class CategorySalesReportDTO {

    private String categoryName;
    private int totalQuantity;
    private double totalAmount;

    public CategorySalesReportDTO() {
    }

    public CategorySalesReportDTO(String categoryName, int totalQuantity, double totalAmount) {
        this.categoryName = categoryName;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
}
