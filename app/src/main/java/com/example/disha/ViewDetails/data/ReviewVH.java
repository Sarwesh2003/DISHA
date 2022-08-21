package com.example.disha.ViewDetails.data;

import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.disha.R;
import com.example.disha.Reviews.dataModel.LocationAdapter;
import com.google.android.material.card.MaterialCardView;

public class ReviewVH extends RecyclerView.ViewHolder {
    TextView userName, description, date, ramp, handrail, toilets, lifts, wheelchair, braille;
    RatingBar ratingBar;
    ImageButton arrow_btn;
    RelativeLayout layout;
    MaterialCardView cv;
    public ReviewVH(@NonNull View itemView) {
        super(itemView);
        userName = itemView.findViewById(R.id.username);
        description = itemView.findViewById(R.id.description);
        date = itemView.findViewById(R.id.dateVisited);
        ratingBar = itemView.findViewById(R.id.rating);
        arrow_btn = itemView.findViewById(R.id.arrow_btn);
        layout = itemView.findViewById(R.id.hiddenView);
        cv = itemView.findViewById(R.id.base_cv);
        ramp = itemView.findViewById(R.id.avlRamps);
        handrail = itemView.findViewById(R.id.avlHandrail);
        toilets = itemView.findViewById(R.id.avlToilet);
        lifts = itemView.findViewById(R.id.avlLifts);
        wheelchair = itemView.findViewById(R.id.avlWheelchair);
        braille = itemView.findViewById(R.id.avlBraille);
    }
}
