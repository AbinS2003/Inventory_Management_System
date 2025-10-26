package com.example.product_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class ProductRequest {

    @NotBlank(message = "product name is required")
    private String name;

    @NotBlank(message = "Category is required")
    private String category;

    @NotEmpty(message = "At least one variant is required")
    @Valid
    private List<VariantRequest> variants;

    private List<String> addonProductIds;

    public ProductRequest() {
    }

    public ProductRequest(String name, String category, List<VariantRequest> variants, List<String> addonProductIds) {
        this.name = name;
        this.category = category;
        this.variants = variants;
        this.addonProductIds = addonProductIds;
    }

    public ProductRequest(String name, String category, List<VariantRequest> variants) {
        this.name = name;
        this.category = category;
        this.variants = variants;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<VariantRequest> getVariants() {
        return variants;
    }

    public void setVariants(List<VariantRequest> variants) {
        this.variants = variants;
    }

    public List<String> getAddonProductIds() {
        return addonProductIds;
    }

    public void setAddonProductIds(List<String> addonProductIds) {
        this.addonProductIds = addonProductIds;
    }
}
