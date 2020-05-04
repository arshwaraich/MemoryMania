package com.example.memorymania.data;

import com.google.gson.annotations.SerializedName;

public class Product {

    enum MatchState {
        MATCHED,
        HIDDEN,
        SHOWN
    }

    @SerializedName("id")
    private long id;

    @SerializedName("image")
    private RetroPhoto image;

    private MatchState matchState;

    public Product(long id, RetroPhoto image) {
        this.id = id;
        this.image = image;
        this.matchState = MatchState.HIDDEN;
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

    public MatchState getMatchState() {
        return matchState;
    }

    public void setMatchState(MatchState matchState) {
        this.matchState = matchState;
    }
}
