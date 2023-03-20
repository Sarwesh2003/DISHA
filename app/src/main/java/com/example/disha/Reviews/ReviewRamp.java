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

import com.example.disha.AddPlace.Handrail;
import com.example.disha.R;
import com.google.android.material.textfield.TextInputEditText;

public class ReviewRamp extends Fragment {
    AutoCompleteTextView ramp;
    TextInputEditText desc;
    AppCompatButton prev, submit;
    View root;
    public ReviewRamp() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_review_ramp, container, false);
        ramp = root.findViewById(R.id.ramps);
        desc = root.findViewById(R.id.descRamp);
        prev = getActivity().findViewById(R.id.prev);
        submit = getActivity().findViewById(R.id.continueBtn);

        initializeAdapters();
        prev.setOnClickListener(v->{
            getActivity().onBackPressed();
        });

        submit.setOnClickListener(v -> {
            sendData();
        });
        return root;
    }
    private void sendData() {
        if(ramp.getText().toString().isEmpty() || desc.getText().toString().isEmpty()){
            TextView warn = root.findViewById(R.id.warn);
            warn.setText("* All fields are mandatory.");
            return;
        }
        Bundle data_bundle = this.getArguments();
        if (data_bundle != null) {
            data_bundle.putString("ramp", ramp.getText().toString());
            data_bundle.putString("rampDescription", desc.getText().toString());
        }
        FragmentManager fragmentManager = this.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down);
        fragmentTransaction.addToBackStack(null);
        ReviewHandrail frg_handrail = new ReviewHandrail();
        frg_handrail.setArguments(data_bundle);
        fragmentTransaction.replace(R.id.fragment_review_container, frg_handrail).commit();
    }

    public void initializeAdapters(){
        String[] facility=getResources().getStringArray(R.array.facilities);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(),R.layout.dropdown_btype,facility);
        ramp.setAdapter(adapter);
    }
}