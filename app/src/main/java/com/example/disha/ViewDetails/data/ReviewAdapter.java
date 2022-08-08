package com.example.disha.ViewDetails.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.disha.R;
import com.example.disha.Reviews.dataModel.Locations;
import com.example.disha.Reviews.dataModel.Review;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    ArrayList<Review> members = new ArrayList<>();
    Context context;

    public ReviewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.review_list_design,parent,false);
        return new ReviewVH(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ReviewVH vh = (ReviewVH) holder;
        Review mem = members.get(position);
        String name = mem.getUsername();
        vh.userName.setText(name);
        vh.description.setText(mem.getDescription());
        vh.date.setText(mem.getDate());
        vh.ratingBar.setRating(Float.parseFloat(mem.getRatings()));
    }

    public void setItems(ArrayList<Review> mem){
        members.addAll(mem);
    }

    @Override
    public int getItemCount() {
        return members.size();
    }
}
