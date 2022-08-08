package com.example.disha.Reviews;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.example.disha.AddPlace.Wheelchair;
import com.example.disha.R;
import com.google.android.material.textfield.TextInputEditText;

public class ReviewLifts extends Fragment {

    AutoCompleteTextView lift;
    TextInputEditText desc_lift;
    AppCompatButton prev, submit;

    public ReviewLifts() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_review_lifts, container, false);
        lift = root.findViewById(R.id.lifts);
        desc_lift = root.findViewById(R.id.descLifts);
        prev = getActivity().findViewById(R.id.prev);
        submit = getActivity().findViewById(R.id.continueBtn);
        initializeAdapters();
        prev.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        submit.setOnClickListener(v -> {
            sendData();
        });
        return root;
    }
    public void initializeAdapters(){
        String[] facility_lift=getResources().getStringArray(R.array.facility_lift);
        ArrayAdapter<String> adapterLift=new ArrayAdapter<>(getContext(),R.layout.dropdown_btype,facility_lift);
        lift.setAdapter(adapterLift);
    }
    private void sendData(){
        Bundle data_bundle = this.getArguments();
        if (data_bundle != null) {
            data_bundle.putString("lifts", lift.getText().toString());
            data_bundle.putString("liftsDescription", desc_lift.getText().toString());
        }
        FragmentManager fragmentManager = this.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down);
        fragmentTransaction.addToBackStack(null);
        ReviewWheelchair wheelchair = new ReviewWheelchair();
        wheelchair.setArguments(data_bundle);
        fragmentTransaction.replace(R.id.fragment_review_container, wheelchair).commit();
    }
}