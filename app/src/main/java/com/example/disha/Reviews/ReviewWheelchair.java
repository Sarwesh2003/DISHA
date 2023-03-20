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
import android.widget.TextView;

import com.example.disha.AddPlace.UploadImages;
import com.example.disha.R;
import com.google.android.material.textfield.TextInputEditText;


public class ReviewWheelchair extends Fragment {
    AutoCompleteTextView wheelchair;
    TextInputEditText desc_wheelchair;
    AppCompatButton prev, submit;
    View root;
    public ReviewWheelchair(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_review_wheelchair, container, false);
        wheelchair = root.findViewById(R.id.wheelchair);
        desc_wheelchair = root.findViewById(R.id.descWheelchair);
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
    private void sendData(){
        if(wheelchair.getText().toString().isEmpty() || desc_wheelchair.getText().toString().isEmpty()){
            TextView warn = root.findViewById(R.id.warn);
            warn.setText("* All fields are mandatory.");
            return;
        }
        Bundle data_bundle = this.getArguments();
        if (data_bundle != null) {
            data_bundle.putString("wheelchair", wheelchair.getText().toString());
            data_bundle.putString("wheelchairDescription", desc_wheelchair.getText().toString());
        }

        FragmentManager fragmentManager = this.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down);
        fragmentTransaction.addToBackStack(null);
        ReviewUploadImages uploadImages = new ReviewUploadImages();
        uploadImages.setArguments(data_bundle);
        fragmentTransaction.replace(R.id.fragment_review_container,uploadImages).commit();
    }

    public void initializeAdapters(){
        String[] facility=getResources().getStringArray(R.array.facilities);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(),R.layout.dropdown_btype,facility);
        wheelchair.setAdapter(adapter);
    }
}