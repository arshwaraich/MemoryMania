package com.example.memorymania.data;

import com.google.gson.annotations.SerializedName;

public class RetroPhoto {

    @SerializedName("id")
    private long id;
    @SerializedName("albumId")
    private long product_id;
    @SerializedName("src")
    private String src;

    public RetroPhoto(long id, long product_id, String src) {
        this.id = id;
        this.product_id = product_id;
        this.src = src;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}