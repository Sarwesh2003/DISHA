package com.example.disha.ViewDetails.data;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.disha.R;
import com.example.disha.Reviews.dataModel.LocationAdapter;

public class ReviewVH extends RecyclerView.ViewHolder {
    TextView userName, description, date;
    RatingBar ratingBar;
    public ReviewVH(@NonNull View itemView) {
        super(itemView);
        userName = itemView.findViewById(R.id.username);
        description = itemView.findViewById(R.id.description);
        date = itemView.findViewById(R.id.dateVisited);
        ratingBar = itemView.findViewById(R.id.rating);
    }
}
