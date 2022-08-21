package com.example.disha.ViewDetails.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

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
        vh.arrow_btn.setOnClickListener(v -> {
            if(vh.layout.getVisibility() == View.VISIBLE){
                TransitionManager.beginDelayedTransition(vh.cv,
                        new AutoTransition());
                vh.layout.setVisibility(View.GONE);
                vh.arrow_btn.setImageResource(R.drawable.ic_arrow_down);
            }else{
                TransitionManager.beginDelayedTransition(vh.cv,
                        new AutoTransition());
                vh.layout.setVisibility(View.VISIBLE);
                vh.ramp.setText(mem.getRamp() + "\n" + mem.getRampDescription());
                vh.braille.setText(mem.getBraille() + "\n" + mem.getBraille());
                vh.handrail.setText(mem.getHandrail() + "\n" + mem.getHandrail());
                vh.wheelchair.setText(mem.getWheelchair() + "\n" + mem.getWheelchair());
                vh.lifts.setText(mem.getLifts() + "\n" + mem.getLiftsDescription());
                vh.toilets.setText(mem.getToilet() + "\n" + "No. of Toilets: " + mem.getNtoilet() + "\n" + mem.getToiletDescription());
                vh.arrow_btn.setImageResource(R.drawable.ic_arrow_up);
            }
        });
    }

    public void setItems(ArrayList<Review> mem){
        members.addAll(mem);
    }

    @Override
    public int getItemCount() {
        return members.size();
    }
}
