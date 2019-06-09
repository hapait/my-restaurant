package com.mcc.restaurant.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mcc.restaurant.R;
import com.mcc.restaurant.adapter.FoodListAdapter;
import com.mcc.restaurant.data.constant.AppConstants;
import com.mcc.restaurant.data.sqlite.FavouriteDbController;
import com.mcc.restaurant.listener.OnItemClickListener;
import com.mcc.restaurant.model.FoodItem;
import com.mcc.restaurant.model.WpFeaturedMedia;
import com.mcc.restaurant.network.ApiClient;
import com.mcc.restaurant.network.HttpParams;
import com.mcc.restaurant.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodListActivity extends BaseActivity {
    private Context mContext;
    private Activity mActivity;
    private ArrayList<FoodItem> foodList;
    private FoodListAdapter foodListAdapter;

    private FavouriteDbController favouriteDbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVars();
        initViews();

    }




    public void initViews() {
        // set parent view
        setContentView(R.layout.activity_food_list);

        initToolbar();
        initLoader();
        enableBackButton();

        showLoader();
        RecyclerView rvFoodList = findViewById(R.id.rv_food_list);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);

        rvFoodList.setLayoutManager(new LinearLayoutManager(mActivity));

        Intent intent = getIntent();
        if (intent != null) {
            int categoryId = intent.getIntExtra(AppConstants.CATEGORY_ID, 0);
            String categoryName = intent.getStringExtra(AppConstants.CATEGORY_NAME);
            toolbarTitle.setText(categoryName);
            loadFoodsByCategory(categoryId);
        }

        foodListAdapter = new FoodListAdapter(mContext, foodList);
        rvFoodList.setAdapter(foodListAdapter);

    }

    public void initVars() {

        mContext = getApplicationContext();

        foodList = new ArrayList<>();

        mActivity = FoodListActivity.this;

        favouriteDbController = new FavouriteDbController(mContext);


    }

    public void loadFoodsByCategory(int categoryId) {
        ApiClient.getClient().getFoodsByCategory(categoryId).enqueue(new Callback<List<FoodItem>>() {
            @Override
            public void onResponse(Call<List<FoodItem>> call, final Response<List<FoodItem>> response) {
                foodList.clear();
                foodList.addAll(response.body());
                syncListWithDB();
                foodListAdapter.notifyDataSetChanged();
                hideLoader();

                for (int i=0; i<response.body().size(); i++){


                    loadFoodImages(i,HttpParams.BASE_URL+HttpParams.API_FEATURED_IMAGE
                            +response.body().get(i).media );

                }
                foodListAdapter.notifyDataSetChanged();
                foodListAdapter.setItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemListener(View view, int position) {
                        ActivityUtils.getInstance().invokeSingleFoodActivity(mActivity, response.body().get(position).id);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<FoodItem>> call, Throwable t) {
                Toast.makeText(mActivity, getString(R.string.could_not_load_data), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*
   Synchronizes server data with saved data in SQLite
     */
    public void syncListWithDB() {
        ArrayList<FoodItem> favoriteFoodList;


        favoriteFoodList = favouriteDbController.getAllData();
        for (int i = 0; i < foodList.size(); i++) {
            for (int j = 0; j < favoriteFoodList.size(); j++) {
                if (foodList.get(i).title.rendered.equalsIgnoreCase(favoriteFoodList.get(j).title.rendered)) {
                    foodList.get(i).isFavorite = true;
                }
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public void loadFoodImages(final int position, String url){
        ApiClient.getClient().getImageString(url).enqueue(new Callback<WpFeaturedMedia>() {
            @Override
            public void onResponse(Call<WpFeaturedMedia> call, Response<WpFeaturedMedia> response) {


                foodList.get(position).setImageUrl(response.body().wpMedia.imageUrl);
                foodListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<WpFeaturedMedia> call, Throwable t) {
                Toast.makeText(mActivity, getString(R.string.could_not_load_data), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

