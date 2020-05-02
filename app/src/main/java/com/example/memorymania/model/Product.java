package com.example.memorymania.model;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("id")
    private long id;

    @SerializedName("image")
    private RetroPhoto image;

    public Product(long id, RetroPhoto image) {
        this.id = id;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RetroPhoto getImage() {
        return image;
    }

    public void setImage(RetroPhoto image) {
        this.image = image;
    }
}
