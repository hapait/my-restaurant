package com.mcc.restaurant.application;

import android.app.Application;

import com.google.firebase.messaging.FirebaseMessaging;


public class MyApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseMessaging.getInstance().subscribeToTopic("myrestaurant");
    }
}
