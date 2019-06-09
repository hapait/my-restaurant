package com.mcc.restaurant.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.mcc.restaurant.R;
import com.mcc.restaurant.data.constant.AppConstants;
import com.mcc.restaurant.view.TouchImageView;

public class LargeImageActivity extends AppCompatActivity {
    private Context mContext;
    private TouchImageView touchImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initFunctionality();
    }

    private void initView() {
        setContentView(R.layout.activity_large_image);

        mContext = LargeImageActivity.this;
        touchImageView = findViewById(R.id.large_image_view);
    }

    private void initFunctionality() {
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(AppConstants.KEY_IMAGE_URL);

        Glide.with(mContext).
                load(imageUrl).
                placeholder(R.color.imgPlaceholder).
                into(touchImageView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
