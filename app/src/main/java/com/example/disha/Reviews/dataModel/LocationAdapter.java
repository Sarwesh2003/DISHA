package com.example.disha.Reviews.dataModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.disha.R;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Locations> members = new ArrayList<>();
    Context context;
    OnLocationClickListener mOnLocationClickListener;

    public LocationAdapter(Context context, OnLocationClickListener mOnLocationClickListener) {
        this.context = context;
        this.mOnLocationClickListener = mOnLocationClickListener;
    }

    public void setItems(ArrayList<Locations> mem){
        members.addAll(mem);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.location_list_design,parent,false);
        return new LocationVH(root, mOnLocationClickListener);
    }
    public interface OnLocationClickListener{
        void onLocationClick(int position);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LocationVH vh = (LocationVH) holder;
        Locations mem = members.get(position);
        String name = mem.getLocationName();
        vh.placeName.setText(name);
        vh.description.setText(mem.getDescription());
    }

    @Override
    public int getItemCount() {
        return members.size();
    }
}
