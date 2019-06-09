package com.mcc.restaurant.model;

import com.google.gson.annotations.SerializedName;

public class Reviews {
    @SerializedName("id")
    public String id;
    @SerializedName("author_name")
    public String author;
    @SerializedName("content")
    public Content review;


    public Reviews(String id, String author, Content review) {
        this.id = id;
        this.author = author;
        this.review = review;
    }

    public static  class Content{
        @SerializedName("rendered")
        public String rendered;

        public Content(String rendered) {
            this.rendered = rendered;
        }
    }
}
