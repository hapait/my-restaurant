package com.mcc.restaurant.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mcc.restaurant.R;
import com.mcc.restaurant.adapter.FavoritesAdapter;
import com.mcc.restaurant.data.constant.AppConstants;
import com.mcc.restaurant.data.sqlite.FavouriteDbController;
import com.mcc.restaurant.listener.OnItemClickListener;
import com.mcc.restaurant.model.FoodItem;
import com.mcc.restaurant.utils.ActivityUtils;

import java.util.ArrayList;

public class FavoritesActivity extends BaseActivity {
    private Context mContext;
    private Activity mActivity;
    private ArrayList<FoodItem> favoriteFoodList;
    private FavoritesAdapter favoritesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVars();
        initViews();
        initListeners();

    }

    public void initViews() {
        // set parent view

        setContentView(R.layout.activity_favorites);

        initToolbar();
        initLoader();
        hideLoader();
        enableBackButton();
        RecyclerView rvFavoriteList = findViewById(R.id.rv_favorite_list);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(AppConstants.FAVORITE_TITLE);
        rvFavoriteList.setLayoutManager(new LinearLayoutManager(mActivity));
        if (favoriteFoodList.size()==0){
            showEmptyView();

        }
        favoritesAdapter = new FavoritesAdapter(mContext, favoriteFoodList);
        rvFavoriteList.setAdapter(favoritesAdapter);
    }


    public void initVars() {

        mContext = getApplicationContext();

        FavouriteDbController favouriteDbController = new FavouriteDbController(mContext);
        favoriteFoodList = new ArrayList<>();

        favoriteFoodList = favouriteDbController.getAllData();


        mActivity = FavoritesActivity.this;



    }

    public void initListeners() {
        favoritesAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemListener(View view, int position) {
                ActivityUtils.getInstance().invokeActivity(mActivity, FoodDetailsActivity.class, false);
            }
        });
        ;
    }
}
