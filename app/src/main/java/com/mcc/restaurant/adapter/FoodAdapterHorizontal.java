package com.mcc.restaurant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mcc.restaurant.R;
import com.mcc.restaurant.listener.OnItemClickListener;
import com.mcc.restaurant.model.FoodItem;

import java.util.ArrayList;

public class FoodAdapterHorizontal extends RecyclerView.Adapter<FoodAdapterHorizontal.ViewHolder> {

    private Context mContext;
    private ArrayList<FoodItem> dataList;
    private OnItemClickListener mListener;

    public FoodAdapterHorizontal(Context mContext, ArrayList<FoodItem> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offers_list, parent, false);
        return new ViewHolder(view, viewType, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvFoodName.setText(dataList.get(position).title.rendered);
        Glide.with(mContext)
                .load(dataList.get(position).getImageUrl())
                .placeholder(R.color.imgPlaceholder)
                .centerCrop()
                .into(holder.ivFoodPic);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivFoodPic;
        private TextView tvFoodName;

        public ViewHolder(final View itemView, int viewType, final OnItemClickListener mListener) {
            super(itemView);

            ivFoodPic =  itemView.findViewById(R.id.ivFoodImage);
            tvFoodName =  itemView.findViewById(R.id.tvFoodName);

            // listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onItemListener(view, getLayoutPosition());
                    }
                }
            });

        }
    }

    public void setItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }
}
