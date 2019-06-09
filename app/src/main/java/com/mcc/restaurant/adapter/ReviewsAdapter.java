package com.mcc.restaurant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mcc.restaurant.R;
import com.mcc.restaurant.model.Reviews;

import java.util.ArrayList;


public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder>{

    private ArrayList<Reviews>dataList;
    private Context context;

    public ReviewsAdapter(ArrayList<Reviews> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_reviews_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.reviewAuthor.setText(dataList.get(position).author);
        holder.reviewContent.setText(Html.fromHtml(dataList.get(position).review.rendered));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView reviewAuthor, reviewContent;

        public ViewHolder(View itemView) {
            super(itemView);
            reviewAuthor=itemView.findViewById(R.id.review_author_name);
            reviewContent=itemView.findViewById(R.id.review_details);
        }
    }
}
