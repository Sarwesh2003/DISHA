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

import com.example.disha.AddPlace.Lifts;
import com.example.disha.R;
import com.google.android.material.textfield.TextInputEditText;

public class ReviewToilet extends Fragment {
    AutoCompleteTextView toilet;
    TextInputEditText desc_toilet, ntoilet;
    AppCompatButton prev, submit;
    public ReviewToilet() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_review_toilet, container, false);
        toilet = root.findViewById(R.id.toilet);
        desc_toilet = root.findViewById(R.id.descToilet);
        ntoilet = root.findViewById(R.id.toiletsNo);
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
        String[] facility=getResources().getStringArray(R.array.facilities);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(),R.layout.dropdown_btype,facility);
        toilet.setAdapter(adapter);
    }
    private void sendData(){
        Bundle data_bundle = this.getArguments();
        if (data_bundle != null) {
            data_bundle.putString("toilet", toilet.getText().toString());
            data_bundle.putString("toiletDescription", desc_toilet.getText().toString());
            data_bundle.putString("toilet_no", ntoilet.getText().toString());
        }
        FragmentManager fragmentManager = this.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down);
        fragmentTransaction.addToBackStack(null);
        ReviewLifts lifts = new ReviewLifts();
        lifts.setArguments(data_bundle);
        fragmentTransaction.replace(R.id.fragment_review_container, lifts).commit();
    }
}