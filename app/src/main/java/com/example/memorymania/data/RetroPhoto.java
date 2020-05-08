package com.example.memorymania.data;

import com.google.gson.annotations.SerializedName;

public class RetroPhoto {

    @SerializedName("id")
    private long id;
    @SerializedName("src")
    private String src;

    public RetroPhoto(long id, String src) {
        this.id = id;
        this.src = src;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}