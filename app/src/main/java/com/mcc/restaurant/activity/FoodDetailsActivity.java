package com.mcc.restaurant.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mcc.restaurant.R;
import com.mcc.restaurant.adapter.ImageSliderAdapter;
import com.mcc.restaurant.adapter.ReviewsAdapter;
import com.mcc.restaurant.data.constant.AppConstants;
import com.mcc.restaurant.data.preference.AppPreference;
import com.mcc.restaurant.data.preference.PrefKey;
import com.mcc.restaurant.listener.OnItemClickListener;
import com.mcc.restaurant.model.FoodItem;
import com.mcc.restaurant.model.Reviews;
import com.mcc.restaurant.model.WpFeaturedMedia;
import com.mcc.restaurant.network.ApiClient;
import com.mcc.restaurant.network.HttpParams;
import com.mcc.restaurant.utils.ActivityUtils;
import com.mcc.restaurant.utils.AppUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodDetailsActivity extends BaseActivity {

    private Activity mActivity;
    private Context mContext;
    private ArrayList<String> sliderImages;
    private ArrayList<Reviews> foodReviews;
    private ReviewsAdapter reviewsAdapter;
    private RecyclerView reviewsList;
    private ImageSliderAdapter imageSliderAdapter;
    private ViewPager mPager;
    private TextView tvFoodDesc, tvFoodName, tvFoodPrice;
    private LinearLayout lytEmptyReview, btnMessenger, btnCall, btnReview;
    private RelativeLayout lytSectionLoader;

    private int foodId;
    private EditText edtAuthorName, edtAuthorEmail, edtAuthorReview;

    private String authorName;
    private String authorEmail;
    private String authorComment;
    // constants
    private static final int TIMER_DURATION = 3000;

    private boolean fromPush = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVars();
        initViews();
        loadData();
        initListeners();
    }

    public void initVars() {
        mActivity = FoodDetailsActivity.this;
        mContext = getApplicationContext();
        sliderImages = new ArrayList<>();
        foodReviews = new ArrayList<>();

        Intent intent = getIntent();
        if (intent.hasExtra(AppConstants.FOOD_ID)) {
            foodId = intent.getIntExtra(AppConstants.FOOD_ID, 0);
        }

        if (intent.hasExtra(AppConstants.BUNDLE_FROM_PUSH)) {
            fromPush = intent.getBooleanExtra(AppConstants.BUNDLE_FROM_PUSH, false);
        }

    }

    public void loadData() {
        loadSingleFood();
        loadReviews();
    }

    public void initViews() {
        setContentView(R.layout.activity_food_details);
        initToolbar();
        initLoader();
        setToolbarTitle(null);
        enableBackButton();
        lytEmptyReview = findViewById(R.id.empty_review_view);
        tvFoodDesc = findViewById(R.id.tv_food_details);
        tvFoodName = findViewById(R.id.tv_food_name);
        tvFoodPrice = findViewById(R.id.tv_food_price);
        btnMessenger = findViewById(R.id.messenger_button);
        btnCall = findViewById(R.id.call_button);
        btnReview = findViewById(R.id.write_review);
        lytSectionLoader = findViewById(R.id.section_loader);
        reviewsList = findViewById(R.id.rv_reviews_list);
        loadImageSlider();
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

    public void initListeners() {
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtility.makePhoneCall(mActivity, AppConstants.CALL_NUMBER);

            }
        });

        btnMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtility.invokeMessengerBot(mActivity);
            }
        });

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showReviewDialog();
            }
        });
        lytEmptyReview.findViewById(R.id.write_review).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showReviewDialog();
            }
        });
    }

    public void loadSingleFood() {
        ApiClient.getClient().getSingleFoodItem(HttpParams.BASE_URL + HttpParams.API_SINGLE_FOOD + foodId).enqueue(new Callback<FoodItem>() {
            @Override
            public void onResponse(Call<FoodItem> call, Response<FoodItem> response) {
                hideLoader();
                tvFoodName.setText(response.body().title.rendered);
                String[] croppedDescription = Html.fromHtml(response.body().
                        content.rendered).toString().split(AppConstants.PRICE_EXTRACTOR);
                tvFoodDesc.setText(croppedDescription[0]);
                tvFoodPrice.setText(AppUtility.getPriceFromDesc(response.body().content.rendered).get(0));
                ApiClient.getClient().getImageString(HttpParams.BASE_URL + HttpParams.API_FEATURED_IMAGE + response.body().media).enqueue(new Callback<WpFeaturedMedia>() {
                    @Override
                    public void onResponse(Call<WpFeaturedMedia> call, Response<WpFeaturedMedia> response) {

                        sliderImages.add(response.body().wpMedia.imageUrl);
                        imageSliderAdapter.notifyDataSetChanged();


                    }

                    @Override
                    public void onFailure(Call<WpFeaturedMedia> call, Throwable t) {
                        Toast.makeText(mContext, R.string.could_not_load_data, Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onFailure(Call<FoodItem> call, Throwable t) {

                Toast.makeText(mContext, R.string.could_not_load_data, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void loadReviews() {
        ApiClient.getClient().getReviews(foodId).enqueue(new Callback<List<Reviews>>() {
            @Override
            public void onResponse(Call<List<Reviews>> call, Response<List<Reviews>> response) {
                foodReviews.clear();
                foodReviews.addAll(response.body());
                if (foodReviews.size() == 0) {
                    lytSectionLoader.setVisibility(View.GONE);
                    lytEmptyReview.setVisibility(View.VISIBLE);
                } else {
                    lytSectionLoader.setVisibility(View.GONE);
                    reviewsAdapter = new ReviewsAdapter(foodReviews, mContext);
                    reviewsList.setLayoutManager(new LinearLayoutManager(mActivity));
                    reviewsList.setAdapter(reviewsAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Reviews>> call, Throwable t) {
                Toast.makeText(mActivity, getString(R.string.could_not_load_data), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void showReviewDialog() {

        View rootView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_review, null);

        initDialogViews(rootView);
        initDialogFunctionality();

        AlertDialog alertDialog = new AlertDialog.Builder(mActivity)
                .setView(rootView)
                .setTitle(R.string.write_a_review)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        authorName = edtAuthorName.getText().toString().trim();
                        authorEmail = edtAuthorEmail.getText().toString().trim();
                        authorComment = edtAuthorReview.getText().toString().trim();
                        AppPreference.getInstance(mActivity).setString(PrefKey.KEY_EMAIL, authorEmail);
                        AppPreference.getInstance(mActivity).setString(PrefKey.KEY_NAME, authorName);

                        sendReview(authorName, authorEmail, authorComment);

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (dialogInterface != null) {
                            dialogInterface.dismiss();
                        }

                    }
                })
                .create();
        alertDialog.show();
    }


    public void initDialogViews(View rootView) {
        edtAuthorName = rootView.findViewById(R.id.edt_author_name);
        edtAuthorEmail = rootView.findViewById(R.id.edt_author_email);
        edtAuthorReview = rootView.findViewById(R.id.edt_author_review);
    }

    public void initDialogFunctionality() {
        edtAuthorName.setText(AppPreference.getInstance(mActivity).getString(PrefKey.KEY_NAME));
        edtAuthorEmail.setText(AppPreference.getInstance(mActivity).getString(PrefKey.KEY_EMAIL));
    }

    private void sendReview(String name, String email, String review) {
        ApiClient.getClient().postReview(name, email, review, foodId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    validateReviewView();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                validateReviewView();
            }
        });

    }

    public void validateReviewView() {
        if (foodReviews.size() == 0) {
            lytEmptyReview.setVisibility(View.GONE);
        }
        lytSectionLoader.setVisibility(View.VISIBLE);
        loadReviews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (fromPush) {
            goToHome();
        } else {
            super.onBackPressed();
        }
    }

    private void goToHome() {
        Intent intent = new Intent(FoodDetailsActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}




