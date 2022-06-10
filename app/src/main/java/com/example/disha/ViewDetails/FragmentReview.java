package com.example.disha.ViewDetails;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.disha.AddPlace.data.PlaceData;
import com.example.disha.MainActivity;
import com.example.disha.R;
import com.example.disha.Reviews.dataModel.DAOReview;
import com.example.disha.Reviews.dataModel.LocationAdapter;
import com.example.disha.Reviews.dataModel.Locations;
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
    String[] data;
    DAOReview dao;
    RecyclerView recyclerView;
    TextView message;
    ProgressBar progressBar;
    ArrayList<ReviewData> list;
    private ReviewAdapter adapter;

    public FragmentReview(String[] data) {
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
        message = root.findViewById(R.id.msg);
        adapter = new ReviewAdapter(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        loadData();
        return root;
    }
    private void loadData(){
        Query retquery = dao.getReference().orderByChild("placeName").equalTo(data[0]);
        retquery.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<ReviewData> arr = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot s : snapshot.getChildren()) {
                        arr.add(new ReviewData(s.child("username").getValue(String.class),
                                s.child("date").getValue(String.class),
                                s.child("description").getValue(String.class),
                                s.child("ratings").getValue(String.class)));
                    }
                    adapter.setItems(arr);
                    list.addAll(arr);
                    adapter.notifyDataSetChanged();

                    if(progressBar != null){
                        progressBar.setVisibility(android.view.View.GONE);
                    }
                }else{
                    message.setVisibility(View.VISIBLE);
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