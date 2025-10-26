package com.example.order_service.model;

import java.util.Map;

public class AddonItem {

    private String addonProductId;
    private String addonVariantId;
    private String addonName;
    private Map<String, String> attributes;
    private double price;

    public AddonItem() {
    }

    public AddonItem(String addonProductId, String addonVariantId, String addonName, Map<String, String> attributes, double price) {
        this.addonProductId = addonProductId;
        this.addonVariantId = addonVariantId;
        this.addonName = addonName;
        this.attributes = attributes;
        this.price = price;
    }

    public String getAddonProductId() {
        return addonProductId;
    }

    public void setAddonProductId(String addonProductId) {
        this.addonProductId = addonProductId;
    }

    public String getAddonVariantId() {
        return addonVariantId;
    }

    public void setAddonVariantId(String addonVariantId) {
        this.addonVariantId = addonVariantId;
    }

    public String getAddonName() {
        return addonName;
    }

    public void setAddonName(String addonName) {
        this.addonName = addonName;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
