package com.mcc.restaurant.model;


import com.google.gson.annotations.SerializedName;

public class FoodItem {
    @SerializedName("id")
    public int id;


    @SerializedName("title")
    public  Title title;

    @SerializedName("content")
    public Content content;

    @SerializedName("featured_media")
    public String media;



    public String imageUrl;
    public boolean isFavorite;

    public FoodItem(int id, Title title, Content content, boolean isFavorite) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isFavorite = isFavorite;
    }

    public FoodItem(int id, Title title, Content content, String media) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.media=media;
    }
    public FoodItem(int id, Title title, Content content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
    public static class Content{
        @SerializedName("rendered")
        public String rendered;

        public Content(String rendered) {
            this.rendered = rendered;
        }
    }

    public static class Title{
        @SerializedName("rendered")
        public String rendered;

        public Title(String rendered) {
            this.rendered = rendered;
        }
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
