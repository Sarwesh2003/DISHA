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

import com.example.disha.AddPlace.Toilet;
import com.example.disha.R;
import com.google.android.material.textfield.TextInputEditText;

public class ReviewBraille extends Fragment {
    AutoCompleteTextView braille;
    TextInputEditText desc_braille;
    AppCompatButton prev, submit;
    public ReviewBraille() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_review_braille, container, false);
        braille = root.findViewById(R.id.braille);
        desc_braille = root.findViewById(R.id.desc_braille);
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
        braille.setAdapter(adapter);
    }
    private void sendData(){
        Bundle data_bundle = this.getArguments();
        if (data_bundle != null) {
            data_bundle.putString("braille", braille.getText().toString());
            data_bundle.putString("brailleDescription", desc_braille.getText().toString());
        }
        FragmentManager fragmentManager = this.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down);
        fragmentTransaction.addToBackStack(null);

        ReviewToilet toilet = new ReviewToilet();
        toilet.setArguments(data_bundle);
        fragmentTransaction.replace(R.id.fragment_review_container, toilet).commit();
    }
}