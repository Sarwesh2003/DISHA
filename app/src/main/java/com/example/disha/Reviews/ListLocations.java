package com.example.disha.Reviews;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.disha.AddPlace.data.DAOPlaceData;
import com.example.disha.AddPlace.data.PlaceData;
import com.example.disha.R;
import com.example.disha.Reviews.ReviewFragment1;
import com.example.disha.Reviews.dataModel.LocationAdapter;
import com.example.disha.Reviews.dataModel.Locations;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListLocations extends Fragment implements LocationAdapter.OnLocationClickListener {
    View root;
    DAOPlaceData dao;
    LocationAdapter adapter;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    ArrayList<Locations> members;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_list_locations, container, false);
        progressBar = root.findViewById(R.id.progress);
        adapter = new LocationAdapter(getContext(), this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView = root.findViewById(R.id.location_list);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        dao = new DAOPlaceData();
        members = new ArrayList<>();
        loadData();
        return root;
    }

    private void loadData() {

        dao.get().addValueEventListener(new ValueEventListener() {
            ArrayList<Locations> arr = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    PlaceData mem = data.getValue(PlaceData.class);
                    if(mem != null){
                        arr.add(new Locations(mem.getPlaceName(),mem.getAddress()));
                    }
                    else{
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        if(progressBar != null){
                            progressBar.setVisibility(android.view.View.GONE);
                        }
                    }
                }
                adapter.setItems(arr);
                members.addAll(arr);
                adapter.notifyDataSetChanged();

                if(progressBar != null){
                    progressBar.setVisibility(android.view.View.GONE);
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

    @Override
    public void onLocationClick(int position) {
        Bundle b = new Bundle();
        b.putString("PlaceName", members.get(position).getLocationName());
        ReviewFragment1 review = new ReviewFragment1();
        review.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_review_container,
                review).setCustomAnimations(R.anim.slide_up,R.anim.slide_down).addToBackStack(null).commit();
    }
}