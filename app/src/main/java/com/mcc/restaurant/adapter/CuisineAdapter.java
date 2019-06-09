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
import com.mcc.restaurant.model.Cuisines;

import java.util.ArrayList;

public class CuisineAdapter extends RecyclerView.Adapter<CuisineAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Cuisines> cuisinesList;
    private OnItemClickListener mListener;
    public CuisineAdapter(Context mContext, ArrayList<Cuisines> cuisinesList) {
        this.mContext = mContext;
        this.cuisinesList = cuisinesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cuisine_list, parent, false);
        return new CuisineAdapter.ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvCuisineName.setText(cuisinesList.get(position).name+" ("+ cuisinesList.get(position).itemQuantity+")");

        Glide.with(mContext)
                .load(cuisinesList.get(position).image)
                .placeholder(R.color.imgPlaceholder)
                .centerCrop()
                .into(holder.ivCuisinePic);

    }

    @Override
    public int getItemCount() {
        return cuisinesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivCuisinePic;
        private TextView tvCuisineName;

        public ViewHolder(final View itemView, final OnItemClickListener mListener) {
            super(itemView);

            ivCuisinePic = itemView.findViewById(R.id.ivCuisinePic);
            tvCuisineName = itemView.findViewById(R.id.tvCuisineName);

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
    public void setItemClickListener(OnItemClickListener mListener){
        this.mListener = mListener;
    }
}
