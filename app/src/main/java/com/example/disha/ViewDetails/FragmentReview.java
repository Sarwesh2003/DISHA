package com.example.disha.ViewDetails;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.disha.AddPlace.data.PlaceData;
import com.example.disha.R;
import com.example.disha.Reviews.ActivityReview;
import com.example.disha.Reviews.dataModel.DAOReview;
import com.example.disha.Reviews.dataModel.Review;
import com.example.disha.ViewDetails.data.ReviewAdapter;
import com.example.disha.ViewDetails.data.ReviewData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FragmentReview extends Fragment {

    View root;
    PlaceData data;
    DAOReview dao;
    RecyclerView recyclerView;

    ProgressBar progressBar;
    AppCompatButton btn_review;
    ArrayList<Review> list;
    private ReviewAdapter adapter;

    public FragmentReview(PlaceData data) {
        this.data = data;
        dao = new DAOReview();
        list = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_display_review, container, false);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        progressBar = root.findViewById(R.id.progress);
        recyclerView = root.findViewById(R.id.show_review);
        btn_review = root.findViewById(R.id.btn_review);
        adapter = new ReviewAdapter(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        if(data != null)
            loadData();
        btn_review.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), ActivityReview.class);
            i.putExtra("Place", data.getPlaceName());
            startActivity(i);
        });
        return root;
    }
    private void loadData(){
        Query retquery = dao.getReference().orderByChild("placeName").equalTo(data.getPlaceName());
        retquery.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<Review> arr = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot s : snapshot.getChildren()) {
                        arr.add(new Review(s.child("placeName").getValue(String.class),
                                s.child("username").getValue(String.class),
                                s.child("date").getValue(String.class),
                                s.child("description").getValue(String.class),
                                s.child("ratings").getValue(String.class),
                                s.child("ramp").getValue(String.class),
                                s.child("rampDescription").getValue(String.class),
                                s.child("handrail").getValue(String.class),
                                s.child("handrailDescription").getValue(String.class),
                                s.child("braille").getValue(String.class),
                                s.child("brailleDescription").getValue(String.class),
                                s.child("lifts").getValue(String.class),
                                s.child("liftsDescription").getValue(String.class),
                                s.child("wheelchair").getValue(String.class),
                                s.child("wheelchairDescription").getValue(String.class),
                                s.child("toilet").getValue(String.class),
                                s.child("ntoilet").getValue(String.class),
                                s.child("toiletDescription").getValue(String.class)));
                    }
                    adapter.setItems(arr);
                    list.addAll(arr);
                    adapter.notifyDataSetChanged();

                    if(progressBar != null){
                        progressBar.setVisibility(android.view.View.GONE);
                    }
                }else{
                    btn_review.setVisibility(View.VISIBLE);
                        if(progressBar != null){
                            progressBar.setVisibility(android.view.View.GONE);
                        }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if(progressBar != null){
                    progressBar.setVisibility(android.view.View.GONE);
                }
                Toast.makeText(getContext(),error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}