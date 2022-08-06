package com.example.disha.AddPlace;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.disha.R;
import com.google.android.material.textfield.TextInputEditText;

public class Handrail extends Fragment {

    AutoCompleteTextView handrail;
    ImageButton btn_img;
    TextInputEditText desc_handrail;
    AppCompatButton prev, submit;
    TextView step, title;
    public Handrail() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_handrail, container, false);
        handrail = root.findViewById(R.id.handrail);
        btn_img = root.findViewById(R.id.handrailbtn);
        desc_handrail = root.findViewById(R.id.desc_facility_handrail);
        prev = getActivity().findViewById(R.id.prev);
        submit = getActivity().findViewById(R.id.continueBtn);
        step = getActivity().findViewById(R.id.step);
        title = getActivity().findViewById(R.id.tit);
        initializeAdapters();
        prev.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        submit.setOnClickListener(v -> {
            sendData();
        });

        btn_img.setOnClickListener(v -> {

        });
        return root;
    }
    public void initializeAdapters(){
        step.setText("Step: 03");
        title.setText("Facilities Available");
        String[] facility=getResources().getStringArray(R.array.facilities);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(),R.layout.dropdown_btype,facility);
//        String[] facility_lift=getResources().getStringArray(R.array.facility_lift);
//        ArrayAdapter<String> adapterLift=new ArrayAdapter<>(getContext(),R.layout.dropdown_btype,facility_lift);
        handrail.setAdapter(adapter);
//        braille.setAdapter(adapter);
//        lifts.setAdapter(adapterLift);
//        wheelchair.setAdapter(adapter);
    }
    private void sendData(){
        FragmentManager fragmentManager = this.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down);
        fragmentTransaction.addToBackStack(null);
//        UploadImages uploadImages = new UploadImages();
//        uploadImages.setArguments(b);
        fragmentTransaction.replace(R.id.fragment_form_container, new Braille()).commit();
    }
}