package com.example.product_service.dto;

import java.util.List;

public class AddonResponse {

    private String id;
    private String name;
    private String category;
    private List<VariantResponse> variantResponses;

    public AddonResponse() {
    }

    public AddonResponse(String id, String name, String category, List<VariantResponse> variantResponses) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.variantResponses = variantResponses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<VariantResponse> getVariantResponses() {
        return variantResponses;
    }

    public void setVariantResponses(List<VariantResponse> variantResponses) {
        this.variantResponses = variantResponses;
    }
}
