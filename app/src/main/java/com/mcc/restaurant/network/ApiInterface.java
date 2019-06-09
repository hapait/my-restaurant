package com.mcc.restaurant.network;

import com.mcc.restaurant.model.Coupons;
import com.mcc.restaurant.model.Cuisines;
import com.mcc.restaurant.model.FoodItem;
import com.mcc.restaurant.model.Reviews;
import com.mcc.restaurant.model.WpFeaturedMedia;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface ApiInterface {

    @GET(HttpParams.API_CATEGORIES)
    Call<List<Cuisines>> getCuisines();

    @GET(HttpParams.API_FOOD_ITEMS_BY_CATEGORY)
    Call<List<FoodItem>>getFoodsByCategory(@Query(HttpParams.API_TEXT_CATEGORIES) int id);

    @GET(HttpParams.API_FEATURED_FOODS)
    Call<List<FoodItem>>getFeaturedFoods();

    @GET
    Call<WpFeaturedMedia>getImageString(@Url String id);

    @GET
    Call<FoodItem>getSingleFoodItem(@Url String id);

    /*To get coupons data*/
    @GET(HttpParams.API_COUPON)
    Call<List<Coupons>>getCoupons();

    /*To get offers data*/
    @GET(HttpParams.API_OFFERS)
    Call<List<FoodItem>>getOffers();

    @GET(HttpParams.API_REVIEWS)
    Call<List<Reviews>>getReviews(@Query(HttpParams.API_TEXT_POST)int id);

    @FormUrlEncoded
    @POST(HttpParams.API_POST_A_REVIEW)
    Call<String> postReview  (@Field(HttpParams.REVIEW_AUTHOR_NAME) String name,
                              @Field(HttpParams.REVIEW_AUTHOR_EMAIL) String email,
                              @Field(HttpParams.REVIEW_CONTENT) String content,
                              @Query(HttpParams.API_TEXT_POST) int postID);
}
