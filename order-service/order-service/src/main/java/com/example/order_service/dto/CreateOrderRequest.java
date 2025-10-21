package com.example.order_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CreateOrderRequest {

    @NotBlank(message = "Customer id is required")
    private String customerId;
    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemsRequest> items;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(String customerId, List<OrderItemsRequest> items) {
        this.customerId = customerId;
        this.items = items;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<OrderItemsRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemsRequest> items) {
        this.items = items;
    }
}
