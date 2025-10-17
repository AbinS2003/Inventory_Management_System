package com.example.order_service.dto;

import java.util.List;

public class OrderResponse {

    private String id;
    private String customerName;
    private List<OrderItemResponseDTO> items;
    private double totalAmount;

    public OrderResponse() {
    }

    public OrderResponse(String id, String customerName, List<OrderItemResponseDTO> items, double totalAmount) {
        this.id = id;
        this.customerName = customerName;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<OrderItemResponseDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponseDTO> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
