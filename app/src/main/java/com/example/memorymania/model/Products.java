package com.example.memorymania.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Products {
    @SerializedName("products")
    private List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
