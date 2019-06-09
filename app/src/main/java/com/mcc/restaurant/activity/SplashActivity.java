package com.mcc.restaurant.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.mcc.restaurant.R;
import com.mcc.restaurant.data.constant.AppConstants;
import com.mcc.restaurant.utils.ActivityUtils;
import com.mcc.restaurant.utils.AppUtility;


public class SplashActivity extends BaseActivity {


    private Context mContext;
    private Activity mActivity;
    private static final int SPLASH_DURATION = 2500;
    private LinearLayout lytSplashBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initVars();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFunctionality();
    }

    private void initVars() {
        mActivity = SplashActivity.this;
        mContext = mActivity.getApplicationContext();
    }

    private void initView() {
        setContentView(R.layout.activity_splash);
        lytSplashBody = findViewById(R.id.lyt_splash_body);
    }

    private void initFunctionality() {

        if (AppUtility.isNetworkAvailable(mContext)) {
            lytSplashBody.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActivityUtils.getInstance().invokeActivity(mActivity, MainActivity.class, true);
                }
            }, SPLASH_DURATION);

        } else {
            AppUtility.noInternetWarning(lytSplashBody, mContext);
        }
    }
}
