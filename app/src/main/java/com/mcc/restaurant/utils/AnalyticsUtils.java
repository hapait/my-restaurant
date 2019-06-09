package com.mcc.restaurant.utils;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;


public class AnalyticsUtils {

    private FirebaseAnalytics mFirebaseAnalytics;
    private static AnalyticsUtils analyticsUtils;

    public static AnalyticsUtils getAnalyticsUtils(Context context) {
        if (analyticsUtils == null) {
            analyticsUtils = new AnalyticsUtils(context);
        }
        return analyticsUtils;
    }

    private AnalyticsUtils(Context context) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public void trackActivity(String activityName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, activityName);
        mFirebaseAnalytics.logEvent("PAGE_VISIT", bundle);
    }

    public void trackFood(String foodName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, foodName);
        mFirebaseAnalytics.logEvent("FAVORITE_FOOD", bundle);
    }
}