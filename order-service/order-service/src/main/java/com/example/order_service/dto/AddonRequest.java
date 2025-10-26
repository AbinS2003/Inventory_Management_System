package com.example.order_service.dto;


public class AddonRequest {

    private String variantId;

    public AddonRequest(String variantId) {
        this.variantId = variantId;
    }

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }
}
