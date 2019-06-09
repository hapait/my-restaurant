package com.mcc.restaurant.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcc.restaurant.R;
import com.mcc.restaurant.model.Coupons;
import com.mcc.restaurant.view.RobotoTextView;

import java.util.ArrayList;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder>{

    private ArrayList<Coupons> dataList;

    public CouponAdapter(ArrayList<Coupons> couponsArrayList) {
        this.dataList = couponsArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
          View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coupon_list, parent, false);
        return new CouponAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        RobotoTextView tvCoupon, tvCouponExpiration;
        public ViewHolder(View itemView) {
            super(itemView);
            tvCoupon=itemView.findViewById(R.id.tv_coupon_text);
            tvCouponExpiration=itemView.findViewById(R.id.tv_coupon_expiration);

        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvCoupon.setText(dataList.get(position).title.rendered);
        holder.tvCouponExpiration.setText(dataList.get(position).expiration);
    }
}
