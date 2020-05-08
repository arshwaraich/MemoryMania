package com.example.memorymania.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Products {
    @SerializedName("products")
    private List<Product> products;

    private List<Product> duplicateProducts() {
        // TODO: Temp size curb
        products = products.subList(0, 10);

        List<Product> copyProducts =  new ArrayList<>(products);
        products.addAll(copyProducts);
        return products;
    }

    public Products(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return duplicateProducts();
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
