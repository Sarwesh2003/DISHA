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
import android.widget.TextView;

import com.example.disha.AddPlace.Braille;
import com.example.disha.R;
import com.google.android.material.textfield.TextInputEditText;

public class ReviewHandrail extends Fragment {
    AutoCompleteTextView handrail;
    TextInputEditText desc_handrail;
    AppCompatButton prev, submit;
    View root;
    public ReviewHandrail() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_review_handrail, container, false);
        handrail = root.findViewById(R.id.handrail);
        desc_handrail = root.findViewById(R.id.desc_facility_handrail);
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
        handrail.setAdapter(adapter);
    }
    private void sendData(){
        if(handrail.getText().toString().isEmpty() || desc_handrail.getText().toString().isEmpty()){
            TextView warn = root.findViewById(R.id.warn);
            warn.setText("* All fields are mandatory.");
            return;
        }
        Bundle data_bundle = this.getArguments();
        if (data_bundle != null) {
            data_bundle.putString("handrail", handrail.getText().toString());
            data_bundle.putString("handrailDescription", desc_handrail.getText().toString());
        }

        FragmentManager fragmentManager = this.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down);
        fragmentTransaction.addToBackStack(null);
        ReviewBraille braille = new ReviewBraille();
        braille.setArguments(data_bundle);
        fragmentTransaction.replace(R.id.fragment_review_container, braille).commit();
    }
}