package com.mcc.restaurant.network;

import com.mcc.restaurant.data.constant.AppConstants;


public class HttpParams {

    // replace by your site url
    public static final String BASE_URL = "https://mccltd.info/projects/restaurent-single/";

    public static final String API_TEXT_POST="post";

    public static final String API_CATEGORIES = "wp-json/wp/v2/categories";

    public static final String API_FOOD_ITEMS_BY_CATEGORY="wp-json/wp/v2/posts?";

    public static final String API_TEXT_CATEGORIES = "categories";

    public static final String API_FEATURED_FOODS="wp-json/wp/v2/posts?sticky=true";

    public static final String API_FEATURED_IMAGE="wp-json/wp/v2/media/";

    public static final String API_SINGLE_FOOD="wp-json/wp/v2/posts/";
    public static final String API_SINGLE_OFFER="wp-json/wp/v2/posts/";

    //Replace your coupon category id from AppConstants.java
    public static final String API_COUPON="wp-json/wp/v2/posts?categories="+ AppConstants.CATEGORY_ID_COUPONS;

    //Replace your coupon category id from AppConstants.java
    public static final String API_OFFERS="wp-json/wp/v2/posts?categories="+ AppConstants.CATEGORY_ID_OFFERS;


    public static final String API_REVIEWS="wp-json/wp/v2/comments?";
    public static final String API_POST_A_REVIEW = "wp-json/wp/v2/comments?";


    public static final String REVIEW_AUTHOR_NAME = "author_name";
    public static final String REVIEW_AUTHOR_EMAIL = "author_email";
    public static final String REVIEW_CONTENT = "content";
}
