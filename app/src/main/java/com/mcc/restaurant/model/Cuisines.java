package com.mcc.restaurant.model;


import com.google.gson.annotations.SerializedName;

public class Cuisines {
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("description")
    public String image;
    @SerializedName("count")
    public int itemQuantity;

    public Cuisines(int id, String name, String image, int itemQuantity) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.itemQuantity = itemQuantity;
    }
}
