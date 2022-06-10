package com.example.disha.Reviews.dataModel;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.disha.R;

public class LocationVH extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView placeName, description;
    LocationAdapter.OnLocationClickListener onLocationClickListener;
    public LocationVH(@NonNull View itemView, LocationAdapter.OnLocationClickListener onLocationClickListener) {
        super(itemView);
        placeName = itemView.findViewById(R.id.place);
        description = itemView.findViewById(R.id.des);
        this.onLocationClickListener = onLocationClickListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onLocationClickListener.onLocationClick(getAdapterPosition());
    }
}
