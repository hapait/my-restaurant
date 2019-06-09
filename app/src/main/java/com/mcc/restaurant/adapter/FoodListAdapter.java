package com.mcc.restaurant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mcc.restaurant.R;
import com.mcc.restaurant.data.sqlite.FavouriteDbController;
import com.mcc.restaurant.listener.OnItemClickListener;
import com.mcc.restaurant.model.FoodItem;
import com.mcc.restaurant.utils.AnalyticsUtils;
import com.mcc.restaurant.utils.AppUtility;

import java.util.ArrayList;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<FoodItem>dataList;
    private OnItemClickListener mListener;
    private AnalyticsUtils mFirebaseAnalytics;

    private FavouriteDbController favouriteDbController;
    public FoodListAdapter(Context mContext, ArrayList<FoodItem> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
        mFirebaseAnalytics=AnalyticsUtils.getAnalyticsUtils(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_list, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvFoodName.setText(dataList.get(position).title.rendered);

        holder.tvFoodPrice.setText("Price " + AppUtility.getPriceFromDesc(dataList.get(position).content.rendered).get(0));

            Glide.with(mContext).load(dataList.get(position).getImageUrl()).into(holder.ivFoodPic);

        if (dataList.get(position).isFavorite) {
            holder.ivFoodFav.setImageResource(R.drawable.thumb_up);
        }else {
            holder.ivFoodFav.setImageResource(R.drawable.thumb_up_outline);
        }
        holder.ivFoodFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dataList.get(position).isFavorite) {
                    Toast.makeText(mContext, R.string.favorite_added, Toast.LENGTH_SHORT).show();

                    //Tracking Which food gets favoritted the most
                    mFirebaseAnalytics.trackFood(dataList.get(position).title.rendered);

                    favouriteDbController = new FavouriteDbController(mContext);
                    favouriteDbController.insertData(dataList.get(position).id, dataList.get(position).title.rendered, dataList.get(position).content.rendered,
                            dataList.get(position).getImageUrl());
                    holder.ivFoodFav.setImageResource(R.drawable.thumb_up);
                    dataList.get(position).isFavorite=true;

                }else{
                    favouriteDbController = new FavouriteDbController(mContext);
                    favouriteDbController.deleteFav(dataList.get(position).id);
                    holder.ivFoodFav.setImageResource(R.drawable.thumb_up_outline);
                    Toast.makeText(mContext, R.string.favorite_remove, Toast.LENGTH_SHORT).show();
                    dataList.get(position).isFavorite=false;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView ivFoodPic, ivFoodFav;
        public TextView tvFoodName, tvFoodPrice;
        public ViewHolder(View itemView, final OnItemClickListener mListener) {
            super(itemView);
            ivFoodPic=itemView.findViewById(R.id.iv_food_pic);
            ivFoodFav=itemView.findViewById(R.id.iv_food_fav);
            tvFoodName=itemView.findViewById(R.id.tv_food_name);
            tvFoodPrice=itemView.findViewById(R.id.tv_food_price);

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
