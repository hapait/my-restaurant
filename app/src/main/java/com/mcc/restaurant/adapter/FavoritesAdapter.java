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
import com.mcc.restaurant.activity.BaseActivity;
import com.mcc.restaurant.data.sqlite.FavouriteDbController;
import com.mcc.restaurant.listener.OnItemClickListener;
import com.mcc.restaurant.model.FoodItem;
import com.mcc.restaurant.utils.AppUtility;

import java.util.ArrayList;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<FoodItem> dataList;
    private OnItemClickListener mListener;
    private FavouriteDbController favouriteDbController;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_list, parent, false);
        return new FavoritesAdapter.ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvFoodName.setText(dataList.get(position).title.rendered);
        holder.tvFoodPrice.setText("Price " + AppUtility.getPriceFromDesc(dataList.get(position).content.rendered).get(0));
        Glide.with(mContext).load(dataList.get(position).media).into(holder.ivFoodPic);
        holder.ivFoodFav.setImageResource(R.drawable.delete);
        holder.ivFoodFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, R.string.favorite_remove, Toast.LENGTH_SHORT).show();
                favouriteDbController = new FavouriteDbController(mContext);
                favouriteDbController.deleteFav(dataList.get(position).id);
                dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, dataList.size());
                if (dataList.size()==0){
                    BaseActivity.showEmptyView();
                }


            }
        });

    }

    public FavoritesAdapter(Context mContext, ArrayList<FoodItem> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

         ImageView ivFoodPic, ivFoodFav;
         TextView tvFoodName, tvFoodPrice;
         ViewHolder(View itemView, final OnItemClickListener mListener) {
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
