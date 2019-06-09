package com.mcc.restaurant.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcc.restaurant.R;
import com.mcc.restaurant.adapter.CouponAdapter;
import com.mcc.restaurant.data.constant.AppConstants;
import com.mcc.restaurant.model.Coupons;
import com.mcc.restaurant.network.ApiClient;
import com.mcc.restaurant.utils.AppUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponsActivity extends BaseActivity {


    private Context mContext;
    private Activity mActivity;
    private ArrayList<Coupons> couponsList;
    private CouponAdapter couponListAdapter;

    private RecyclerView rvCoupons;
    private TextView toolBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVars();
        initView();
        loadCoupons();
        showLoader();
    }

    private void initVars() {
        mContext = getApplicationContext();
        mActivity = CouponsActivity.this;
        couponsList = new ArrayList<>();


    }

    private void initView() {

        // set parent view
        setContentView(R.layout.activity_coupons);
        toolBarTitle = findViewById(R.id.toolbar_title);
        toolBarTitle.setText(AppConstants.COUPON_TITLE);
        initToolbar();
        initLoader();
        enableBackButton();

        rvCoupons = findViewById(R.id.rv_coupons_list);

        // init RecyclerView
        rvCoupons.setHasFixedSize(true);

        rvCoupons.setLayoutManager(new LinearLayoutManager(mActivity));
        couponListAdapter = new CouponAdapter(couponsList);
        rvCoupons.setAdapter(couponListAdapter);
    }

    public void loadCoupons(){
        ApiClient.getClient().getCoupons().enqueue(new Callback<List<Coupons>>() {
            @Override
            public void onResponse(Call<List<Coupons>> call, Response<List<Coupons>> response) {

                ArrayList<Coupons>tempCouponList=new ArrayList<>();
                couponsList.clear();
                couponsList.addAll(response.body());
                tempCouponList.addAll(response.body());

                if (tempCouponList.size()>0){
                    hideLoader();
                    refactorCouponsList(couponsList);
                    tempCouponList.clear();
                    tempCouponList.addAll(couponsList);
                    removeExpiration(couponsList, tempCouponList);
                    couponListAdapter.notifyDataSetChanged();
                }else if(tempCouponList.size()==0){
                    showEmptyView();
                }
            }

            @Override
            public void onFailure(Call<List<Coupons>> call, Throwable t) {
                Toast.makeText(mActivity, getString(R.string.could_not_load_data), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void refactorCouponsList(ArrayList<Coupons> couponsList){
        for (int i=0;i<couponsList.size();i++){

            long remainingTime = System.currentTimeMillis()-AppUtility.getInstance().timestampToMilliseconds(couponsList.get(i).timestamp);
            long thirtyMinus=AppConstants.THIRTY_DAYS-remainingTime;
            long days = TimeUnit.MILLISECONDS.toDays(thirtyMinus);
            String daysString=String.valueOf(days);

            if (daysString.length()==1){

                couponsList.get(i).setExpiration("0"+days + "");
            }else {
                couponsList.get(i).setExpiration(days + "");
            }
        }
    }

    public void removeExpiration(ArrayList<Coupons>couponsList, ArrayList<Coupons>tempCouponsList){
        couponsList.clear();
        for (int i=0; i<tempCouponsList.size(); i++){
            long remainingTime = System.currentTimeMillis()-AppUtility.getInstance().timestampToMilliseconds(tempCouponsList.get(i).timestamp);
            long thirtyMinus=AppConstants.THIRTY_DAYS-remainingTime;
            if (thirtyMinus>0){
                couponsList.add(tempCouponsList.get(i));
            }
        }
    }
}
