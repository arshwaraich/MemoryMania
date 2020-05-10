package com.example.memorymania.data;

import android.content.Context;

import com.example.memorymania.R;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Products {
    @SerializedName("products")
    private List<Product> products;

    private List<Product> replicateProducts(Context context) {
        Integer totalMatches = context.getResources().getInteger(R.integer.total_matches);
        Integer matchSize = context.getResources().getInteger(R.integer.match_size);
        products = products.subList(0, totalMatches);

        List<Product> replicatedProd = new ArrayList<>();
        List<Product> copyProducts =  new ArrayList<>();
        for(int i = 0; i < matchSize; ++i) {
            copyProducts.clear();
            for(Product p: products) {
                copyProducts.add(new Product(p));
            }
            replicatedProd.addAll(copyProducts);
        }
        return replicatedProd;
    }

    public Products(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts(Context context) {
        return replicateProducts(context);
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
