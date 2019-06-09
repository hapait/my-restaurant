package com.mcc.restaurant.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.mcc.restaurant.R;
import com.mcc.restaurant.adapter.ImageSliderAdapter;
import com.mcc.restaurant.data.constant.AppConstants;
import com.mcc.restaurant.listener.OnItemClickListener;
import com.mcc.restaurant.model.FoodItem;
import com.mcc.restaurant.model.WpFeaturedMedia;
import com.mcc.restaurant.network.ApiClient;
import com.mcc.restaurant.network.HttpParams;
import com.mcc.restaurant.utils.ActivityUtils;
import com.mcc.restaurant.utils.AppUtility;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferDetailsActivity extends  BaseActivity {
    private Context mContext;
    private Activity mActivity;
    private TextView tvOfferTitle,tvOfferDetails;
    private FloatingActionButton fabCall, fabMessenger;
    private int offerId;

    private ArrayList<String> sliderImages;
    private ImageSliderAdapter imageSliderAdapter;
    private ViewPager mPager;
    private static final int TIMER_DURATION = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVars();
        initViews();
        loadSingleOffer();
        initListeners();
    }

    private void initVars() {
        mContext = getApplicationContext();
        mActivity = OfferDetailsActivity.this;
        sliderImages = new ArrayList<>();
        Intent intent = getIntent();
        offerId = intent.getIntExtra(AppConstants.OFFER_ID, 0);
    }

    public void initViews(){

        setContentView(R.layout.activity_offer_details);
        initToolbar();
        enableBackButton();
        initLoader();
        setToolbarTitle(null);
        tvOfferTitle=findViewById(R.id.tv_offer_title);
        tvOfferDetails=findViewById(R.id.tv_offer_details);
        TextView tvContactNumber = findViewById(R.id.tv_contact_number);
        fabCall=findViewById(R.id.fab_call);
        fabMessenger=findViewById(R.id.fab_messenger);
        tvContactNumber.setText(AppConstants.CALL_NUMBER);
        loadImageSlider();
        fabCall=findViewById(R.id.fab_call);
        fabMessenger=findViewById(R.id.fab_messenger);
    }
    public void loadImageSlider() {
        imageSliderAdapter = new ImageSliderAdapter(mContext, sliderImages);
        mPager = findViewById(R.id.vp_image_slider);
        mPager.setAdapter(imageSliderAdapter);
        CircleIndicator indicator = findViewById(R.id.slider_indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                int setPosition = mPager.getCurrentItem() + 1;
                if (setPosition == sliderImages.size()) {
                    setPosition = AppConstants.VALUE_ZERO;
                }
                mPager.setCurrentItem(setPosition, true);
            }
        };

        //  Auto animated timer
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, TIMER_DURATION, TIMER_DURATION);


        // view page on item click listener
        imageSliderAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemListener(View view, int position) {
                ActivityUtils.getInstance().invokeLargeFoodImage(mActivity, sliderImages.get(position));
            }
        });
    }
    public void loadSingleOffer() {
        ApiClient.getClient().getSingleFoodItem(HttpParams.BASE_URL + HttpParams.API_SINGLE_OFFER + offerId).enqueue(new Callback<FoodItem>() {
            @Override
            public void onResponse(Call<FoodItem> call, Response<FoodItem> response) {

                hideLoader();

                tvOfferTitle.setText(response.body().title.rendered);
                tvOfferDetails.setText(Html.fromHtml(response.body().content.rendered));
                ApiClient.getClient().getImageString(HttpParams.BASE_URL + HttpParams.API_FEATURED_IMAGE + response.body().media).enqueue(new Callback<WpFeaturedMedia>() {
                    @Override
                    public void onResponse(Call<WpFeaturedMedia> call, Response<WpFeaturedMedia> response) {

                        sliderImages.add(response.body().wpMedia.imageUrl);
                        imageSliderAdapter.notifyDataSetChanged();

                        hideLoader();
                    }

                    @Override
                    public void onFailure(Call<WpFeaturedMedia> call, Throwable t) {

                    }
                });

            }

            @Override
            public void onFailure(Call<FoodItem> call, Throwable t) {

            }
        });
    }

    public void initListeners(){
        fabCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtility.makePhoneCall(mActivity, AppConstants.CALL_NUMBER);
            }
        });

        fabMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtility.invokeMessengerBot(mActivity);
            }
        });
    }
}
