package com.mcc.restaurant.utils;

import android.app.Activity;
import android.content.Intent;

import com.mcc.restaurant.activity.FoodDetailsActivity;
import com.mcc.restaurant.activity.FoodListActivity;
import com.mcc.restaurant.activity.LargeImageActivity;
import com.mcc.restaurant.activity.OfferDetailsActivity;
import com.mcc.restaurant.data.constant.AppConstants;

public class ActivityUtils {

    private static ActivityUtils sActivityUtils = null;

    public static ActivityUtils getInstance() {
        if (sActivityUtils == null) {
            sActivityUtils = new ActivityUtils();
        }
        return sActivityUtils;
    }

    public void invokeActivity(Activity activity, Class<?> tClass, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }

    public void invokeCategorizedFoodsActivity(Activity activity, int categoryId, String categoryName){
        Intent intent=new Intent(activity, FoodListActivity.class);
        intent.putExtra(AppConstants.CATEGORY_ID, categoryId);
        intent.putExtra(AppConstants.CATEGORY_NAME, categoryName);
        activity.startActivity(intent);

    }

    public void invokeSingleFoodActivity(Activity activity, int foodId){
        Intent intent=new Intent(activity, FoodDetailsActivity.class);
        intent.putExtra(AppConstants.FOOD_ID, foodId);
        activity.startActivity(intent);
    }
    public void invokeSingleOfferActivity(Activity activity, int offerId){
        Intent intent=new Intent(activity, OfferDetailsActivity.class);
        intent.putExtra(AppConstants.OFFER_ID, offerId);
        activity.startActivity(intent);
    }


    public void invokeLargeFoodImage(Activity activity, String url){
        Intent intent  = new Intent(activity, LargeImageActivity.class);
        intent.putExtra(AppConstants.KEY_IMAGE_URL, url);
        activity.startActivity(intent);
    }

//    public void invokeProducts(Activity activity, String pageTitle, int pageType, int categoryId) {
//        Intent intent = new Intent(activity, ProductListActivity.class);
//        intent.putExtra(AppConstants.PAGE_TITLE, pageTitle);
//        intent.putExtra(AppConstants.PAGE_TYPE, pageType);
//        intent.putExtra(AppConstants.CATEGORY_ID, categoryId);
//        activity.startActivity(intent);
//    }
//
//    public void invokeImage(Activity activity, String imageUrl) {
//        Intent intent = new Intent(activity, LargeImageViewActivity.class);
//        intent.putExtra(AppConstants.KEY_IMAGE_URL, imageUrl);
//        activity.startActivity(intent);
//    }
//
//    public void invokeProductDetails(Activity activity, String productId) {
//        Intent intent = new Intent(activity, ProductDetailsActivity.class);
//        intent.putExtra(AppConstants.PRODUCT_ID, productId);
//        activity.startActivity(intent);
//    }
//
//    public void invokeWebPageActivity(Activity activity, String pageTitle, String url) {
//        Intent intent = new Intent(activity, WebPageActivity.class);
//        intent.putExtra(AppConstants.BUNDLE_KEY_TITLE, pageTitle);
//        intent.putExtra(AppConstants.BUNDLE_KEY_URL, url);
//        activity.startActivity(intent);
//    }
//
//    public void invokeNotifyContentActivity(Activity activity, String title, String message) {
//        Intent intent = new Intent(activity, NotificationContentActivity.class);
//        intent.putExtra(AppConstants.BUNDLE_KEY_TITLE, title);
//        intent.putExtra(AppConstants.BUNDLE_KEY_MESSAGE, message);
//        activity.startActivity(intent);
//    }
//
//    public void invokeSearchActivity(Activity activity, String searchKey) {
//        Intent intent = new Intent(activity, SearchActivity.class);
//        intent.putExtra(AppConstants.SEARCH_KEY, searchKey);
//        activity.startActivity(intent);
//    }

}
