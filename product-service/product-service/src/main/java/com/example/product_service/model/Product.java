package com.example.product_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "products")
public class Product {

    @Id
    private String id;
    @TextIndexed
    private String name;
    @Indexed
    private String category;
    private List<String> addonProductIds;



    public Product() {
    }

    public Product(String name, String category, List<String> addonProductIds) {
        this.name = name;
        this.category = category;
        this.addonProductIds = addonProductIds;
    }

    public Product(String name, String category) {
        this.name = name;
        this.category = category;
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

    public List<String> getAddonProductIds() {
        return addonProductIds;
    }

    public void setAddonProductIds(List<String> addonProductIds) {
        this.addonProductIds = addonProductIds;
    }
}
