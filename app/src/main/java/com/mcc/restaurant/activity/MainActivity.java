package com.mcc.restaurant.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mcc.restaurant.R;
import com.mcc.restaurant.adapter.CuisineAdapter;
import com.mcc.restaurant.adapter.FoodAdapterHorizontal;
import com.mcc.restaurant.adapter.ImageSliderAdapter;
import com.mcc.restaurant.data.constant.AppConstants;
import com.mcc.restaurant.listener.OnItemClickListener;
import com.mcc.restaurant.model.Cuisines;
import com.mcc.restaurant.model.FoodItem;
import com.mcc.restaurant.model.WpFeaturedMedia;
import com.mcc.restaurant.network.ApiClient;
import com.mcc.restaurant.network.HttpParams;
import com.mcc.restaurant.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private ArrayList<FoodItem> foodItemList, offerItemList;
    private ArrayList<Cuisines> cuisinesList;
    private TextView tvOfferTitle, tvCategoryTitle;
    private RecyclerView cuisinesRecyclerView;
    private FoodAdapterHorizontal offerAdapter;
    private CuisineAdapter cuisineAdapter;
    private ImageSliderAdapter imageSliderAdapter;
    private Context mContext;
    private Activity mActivity;
    private ArrayList<String> featuredImageList;
    private ViewPager mPager;
    private ProgressBar cuisineLoader, offerLoader;
    private RelativeLayout cuisinesParent, offersParent;

    // constants
    private static final int TIMER_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVar();
        initViews();
        loadData();
        initListeners();
    }

    private void initVar() {
        mContext = getApplicationContext();
        mActivity = MainActivity.this;
        offerItemList = new ArrayList<>();
        cuisinesList = new ArrayList<>();
        foodItemList=new ArrayList<>();

        featuredImageList = new ArrayList<>();
        imageSliderAdapter = new ImageSliderAdapter(mContext, featuredImageList);
    }

    private void initViews() {
        setContentView(R.layout.activity_main);
        initToolbar();
        initDrawer();


        tvOfferTitle=findViewById(R.id.tv_list_title);
        RelativeLayout offerCategory = findViewById(R.id.lyt_offer_list);
        RecyclerView offerRecyclerView = offerCategory.findViewById(R.id.rv_home);
        offerAdapter=new FoodAdapterHorizontal(mContext, offerItemList);
        offerLoader=offerCategory.findViewById(R.id.section_progress);
        offersParent=offerCategory.findViewById(R.id.parent_panel);
        offerRecyclerView.setLayoutManager( new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        offerRecyclerView.setAdapter(offerAdapter);



        RelativeLayout cuisinesPart = findViewById(R.id.lyt_cuisines);
        cuisinesRecyclerView = cuisinesPart.findViewById(R.id.rv_home);
        tvCategoryTitle = cuisinesPart.findViewById(R.id.tv_list_title);
        cuisineLoader = cuisinesPart.findViewById(R.id.section_progress);
        cuisineAdapter = new CuisineAdapter(mContext, cuisinesList);
        cuisinesParent=cuisinesPart.findViewById(R.id.parent_panel);


    }

    private void loadData(){
        // Load featured foods
        loadFeaturedSlider();
        // Load cuisines data by categories
        loadCuisinesByCategories();
        // Load food offers
        loadOffersData();
        // Load featured food images
        loadFeaturedMedias();
    }

    private void initListeners() {

        offerAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemListener(View view, int position) {
                ActivityUtils.getInstance().invokeSingleOfferActivity(mActivity, offerItemList.get(position).id);
            }
        });
        cuisineAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemListener(View view, int position) {
                ActivityUtils.getInstance().invokeCategorizedFoodsActivity(mActivity, cuisinesList.get(position).id, cuisinesList.get(position).name);
            }
        });

        imageSliderAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemListener(View view, int position) {
                ActivityUtils.getInstance().invokeSingleFoodActivity(mActivity, foodItemList.get(position).id);
            }
        });
    }

    private void loadCuisinesByCategories() {

        ApiClient.getClient().getCuisines().enqueue(new Callback<List<Cuisines>>() {
            @Override
            public void onResponse(Call<List<Cuisines>> call, Response<List<Cuisines>> response) {

                cuisinesList.clear();
                cuisinesList.addAll(response.body());
                filterCategories(cuisinesList);

                if (!cuisinesList.isEmpty()) {
                    cuisinesParent.setVisibility(View.VISIBLE);

                    tvCategoryTitle.setText(getString(R.string.our_cuisines) + getString(R.string.start_brace) + cuisinesList.size() + getString(R.string.end_brace));
                    cuisineLoader.setVisibility(View.GONE);
                    cuisinesRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));
                    cuisinesRecyclerView.setAdapter(cuisineAdapter);
                    cuisinesRecyclerView.setNestedScrollingEnabled(false);
                    cuisineAdapter.notifyDataSetChanged();
                }
                else {
                    cuisineLoader.setVisibility(View.GONE);
                    hideLoader();
                }
            }

            @Override
            public void onFailure(Call<List<Cuisines>> call, Throwable t) {
                System.out.print(t.toString());
            }
        });
    }


    private void loadFeaturedSlider() {

        mPager = findViewById(R.id.vp_image_slider);
        mPager.setAdapter(imageSliderAdapter);
        CircleIndicator indicator = findViewById(R.id.slider_indicator);
        indicator.setViewPager(mPager);
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                int setPosition = mPager.getCurrentItem() + 1;
                if (setPosition == featuredImageList.size()) {
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

    }

    private void loadFeaturedMedias() {
        ApiClient.getClient().getFeaturedFoods().enqueue(new Callback<List<FoodItem>>() {
            @Override
            public void onResponse(Call<List<FoodItem>> call, Response<List<FoodItem>> response) {
                    foodItemList.addAll(response.body());
                for (int i = 0; i < response.body().size(); i++) {

                    //Retrofit does not allow using GET with Parameters with Url for dynamic Urls
                    ApiClient.getClient().getImageString(HttpParams.BASE_URL + HttpParams.API_FEATURED_IMAGE
                            + response.body().get(i).media).enqueue(new Callback<WpFeaturedMedia>() {
                        @Override
                        public void onResponse(Call<WpFeaturedMedia> call, Response<WpFeaturedMedia> response) {
                            featuredImageList.add(response.body().wpMedia.imageUrl);
                            // update image slider adapter data
                            imageSliderAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<WpFeaturedMedia> call, Throwable t) {
                            Toast.makeText(mActivity, getString(R.string.could_not_load_data), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<FoodItem>> call, Throwable t) {
                System.out.print(t.toString());
            }
        });
    }

    private void filterCategories(ArrayList<Cuisines> foodItemList) {
        for (int i = 0; i < foodItemList.size(); i++) {
            if (foodItemList.get(i).name.equalsIgnoreCase(AppConstants.COUPON_TITLE) ||
                    foodItemList.get(i).name.equalsIgnoreCase(AppConstants.OFFER_TITLE)
                    || foodItemList.get(i).name.equalsIgnoreCase(AppConstants.UNCATEGORIZED)) {
                foodItemList.remove(i);
            }
        }
    }

    private void loadOffersData(){
        ApiClient.getClient().getOffers().enqueue(new Callback<List<FoodItem>>() {
            @Override
            public void onResponse(Call<List<FoodItem>> call, Response<List<FoodItem>> response) {
                offerItemList.clear();
                offerItemList.addAll(response.body());

                if (!offerItemList.isEmpty()) {
                    offersParent.setVisibility(View.VISIBLE);
                    tvOfferTitle.setText(getString(R.string.our_offers) + offerItemList.size() + getString(R.string.end_brace));
                    offerLoader.setVisibility(View.GONE);
                    for (int i = 0; i < response.body().size(); i++) {

                        loadOfferImages(i, HttpParams.BASE_URL + HttpParams.API_FEATURED_IMAGE
                                + response.body().get(i).media);

                    }
                    offerAdapter.notifyDataSetChanged();
                }else{
                    offerLoader.setVisibility(View.GONE);
                    hideLoader();
                }
            }

            @Override
            public void onFailure(Call<List<FoodItem>> call, Throwable t) {

            }
        });
    }
    private void loadOfferImages(final int position, String url){
        ApiClient.getClient().getImageString(url).enqueue(new Callback<WpFeaturedMedia>() {
            @Override
            public void onResponse(Call<WpFeaturedMedia> call, Response<WpFeaturedMedia> response) {


                offerItemList.get(position).setImageUrl(response.body().wpMedia.imageUrl);
                offerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<WpFeaturedMedia> call, Throwable t) {
                Toast.makeText(mActivity, getString(R.string.could_not_load_data), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

