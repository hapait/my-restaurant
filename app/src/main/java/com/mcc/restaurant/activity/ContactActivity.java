package com.mcc.restaurant.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdView;
import com.mcc.restaurant.R;
import com.mcc.restaurant.data.constant.AppConstants;
import com.mcc.restaurant.utils.AdUtils;
import com.mcc.restaurant.utils.AppUtility;

public class ContactActivity extends BaseActivity implements View.OnClickListener{

    private Activity mActivity;
    private Context mContext;
    private RelativeLayout directionButton;
    private ImageView ivMapView;
    private TextView toolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVars();
        initViews();
        initListeners();
    }

    public void initVars(){
        mActivity=ContactActivity.this;
        mContext=getApplicationContext();
    }
    public void initViews(){
        setContentView(R.layout.activity_contact);
        initToolbar();
        enableBackButton();

        directionButton=findViewById(R.id.btn_direction);
        ivMapView=findViewById(R.id.iv_map_image);
        Glide.with(mContext).load(R.drawable.ic_map_view).into(ivMapView);
        toolbarTitle=findViewById(R.id.toolbar_title);
        toolbarTitle.setText(AppConstants.CONTACT_TITLE);
        AdUtils.getInstance(mContext).showBannerAd((AdView) findViewById(R.id.ad_view));
    }

    public void initListeners(){
        directionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        AppUtility.openMap(mActivity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // load banner ad
        AdUtils.getInstance(mContext).showBannerAd((AdView) findViewById(R.id.ad_view));

    }

}
