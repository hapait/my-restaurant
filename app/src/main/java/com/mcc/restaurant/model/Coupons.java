package com.mcc.restaurant.model;

import com.google.gson.annotations.SerializedName;

public class Coupons {

    @SerializedName("title")
    public FoodItem.Title title;

    @SerializedName("id")
    public int id;
    @SerializedName("date")
    public String timestamp;
    public String expiration;


    public Coupons(FoodItem.Title title, int id, String timestamp) {
        this.title = title;
        this.id = id;
        this.timestamp = timestamp;
    }

    public static class Title {
        @SerializedName("rendered")
        public String rendered;

        public Title(String rendered) {
            this.rendered = rendered;
        }

    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }
}
