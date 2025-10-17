package com.example.order_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CreateOrderRequest {

    @NotBlank(message = "Customer name is required")
    private String customerName;
    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemsRequest> items;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(String customerName, List<OrderItemsRequest> items) {
        this.customerName = customerName;
        this.items = items;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<OrderItemsRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemsRequest> items) {
        this.items = items;
    }
}
