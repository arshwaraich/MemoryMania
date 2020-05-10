package com.example.memorymania.data;

import android.content.Context;

import androidx.preference.PreferenceManager;

import com.example.memorymania.R;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Products {
    @SerializedName("products")
    private List<Product> products;

    private List<Product> replicateProducts(Context context) {
        int totalMatches = Integer.parseInt(
                PreferenceManager
                        .getDefaultSharedPreferences(context)
                        .getString("total_match_size", "10"));
        int matchSize = Integer.parseInt(
                PreferenceManager
                        .getDefaultSharedPreferences(context)
                        .getString("match_size", "2"));
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
