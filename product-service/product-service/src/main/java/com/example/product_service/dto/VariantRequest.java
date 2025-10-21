package com.example.product_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

@Schema(description = "Represents a product variant (used for both create and update)")
public class VariantRequest {

    @Schema(
            description = "Variant ID — only required when updating an existing variant. Omit this when creating a new variant.",
            example = "64f8a1c3d2f1e234abcd1234",
            required = false
    )    private String id;

    @NotNull(message = "Price is required")
    private Double price;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotEmpty(message = "Attributes are required")
    private Map<String, String> attributes;

    public VariantRequest() {
    }

    public VariantRequest(Double price, Integer quantity, Map<String, String> attributes) {
        this.price = price;
        this.quantity = quantity;
        this.attributes = attributes;
    }



    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
