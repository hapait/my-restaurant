package com.mcc.restaurant.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.mcc.restaurant.R;
import com.mcc.restaurant.data.constant.AppConstants;
import com.mcc.restaurant.utils.ActivityUtils;
import com.mcc.restaurant.utils.AdUtils;
import com.mcc.restaurant.utils.AppUtility;
import com.mcc.restaurant.utils.PermissionUtils;


public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Context mContext;
    private Activity mActivity;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    static LinearLayout loadingView, noDataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();


        AdUtils.getInstance(mContext).loadFullScreenAd(BaseActivity.this);
        // uncomment this line to disable ad
        // AdUtils.getInstance(mContext).disableBannerAd();
        // AdUtils.getInstance(mContext).disableInterstitialAd();
    }

    private void initVariable() {
        mContext = getApplicationContext();
        mActivity = BaseActivity.this;
    }

    public void initToolbar() {
        mToolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    public void enableBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public Toolbar getToolbar() {
        if (mToolbar == null) {
            mToolbar =  findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
        }
        return mToolbar;
    }

    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    public void initDrawer() {

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView mNavigationView = findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.syncState();

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_coupons) {
            //if you don't want to show full screen ad on coupons activity then uncomment this line and comment the next one

            //ActivityUtils.getInstance().invokeActivity(mActivity, CouponsActivity.class, false);
            showAdThenActivity(CouponsActivity.class);
        } else if (id == R.id.action_favourite) {
           ActivityUtils.getInstance().invokeActivity(mActivity, FavoritesActivity.class, false);
        }

                else if (id == R.id.action_call) {
           AppUtility.makePhoneCall(mActivity, AppConstants.CALL_NUMBER);
        }  else if (id == R.id.action_messenger) {
            AppUtility.invokeMessengerBot(mActivity);
        }
        else if (id == R.id.action_direction) {
            ActivityUtils.getInstance().invokeActivity(mActivity, ContactActivity.class, false);
       }

       if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    public void initLoader() {
        loadingView = findViewById(R.id.loadingView);
        noDataView =  findViewById(R.id.noDataView);
    }

    public void showLoader() {
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
        }

        if (noDataView != null) {
            noDataView.setVisibility(View.GONE);
        }
    }

    public void hideLoader() {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
        if (noDataView != null) {
            noDataView.setVisibility(View.GONE);
        }
    }

    public static void showEmptyView() {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
        if (noDataView != null) {
            noDataView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (PermissionUtils.isPermissionResultGranted(grantResults)) {
            if (requestCode == PermissionUtils.REQUEST_CALL) {
                AppUtility.makePhoneCall(mActivity, AppConstants.CALL_NUMBER);
            }
        } else {
            AppUtility.showToast(mActivity, getString(R.string.permission_not_granted));
        }

    }

    public void showAdThenActivity(final Class<?> activity) {
        if (AdUtils.getInstance(mContext).showFullScreenAd()) {
            AdUtils.getInstance(mContext).getInterstitialAd().setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    ActivityUtils.getInstance().invokeActivity(mActivity, activity, false);
                }
            });
        } else {
            ActivityUtils.getInstance().invokeActivity(mActivity, activity, false);
        }
    }


}