package com.mcc.restaurant.model;

import com.google.gson.annotations.SerializedName;


public class WpFeaturedMedia {
    @SerializedName("guid")
    public WpMedia wpMedia;

    public WpFeaturedMedia(WpMedia wpMedia) {
        this.wpMedia = wpMedia;
    }

    public static class WpMedia{
        @SerializedName("rendered")
        public String imageUrl;

        public WpMedia(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
