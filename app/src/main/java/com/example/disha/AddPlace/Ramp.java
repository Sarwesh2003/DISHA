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
import android.widget.TextView;

import com.example.disha.R;
import com.example.disha.AddPlace.data.DAOPlaceData;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;


public class Ramp extends Fragment {

   View root;
   TextView title, step;
   AutoCompleteTextView ramp;
   TextInputEditText ntoilet, desc;
   AppCompatButton prev, submit;
   ArrayList<String> data = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root =  inflater.inflate(R.layout.fragment_ramp, container, false);
        title = getActivity().findViewById(R.id.tit);
        ramp = root.findViewById(R.id.ramps);
        desc = root.findViewById(R.id.descRamp);
        prev = getActivity().findViewById(R.id.prev);
        submit = getActivity().findViewById(R.id.continueBtn);
        step = getActivity().findViewById(R.id.step);
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
//        Bundle data_bundle = this.getArguments();
//        data.addAll(data_bundle.getStringArrayList("data"));
//        data.add(ramp.getText().toString());
//        data.add(handrail.getText().toString());
//        data.add(toilet.getText().toString());
//        data.add(ntoilet.getText().toString());
//        data.add(braille.getText().toString());
//        data.add(lifts.getText().toString());
//        data.add(wheelchair.getText().toString());
//        data.add(desc.getText().toString());
//        DAOPlaceData dao = new DAOPlaceData();
//        Toast.makeText(getContext(), String.valueOf(data.size()), Toast.LENGTH_SHORT).show();
//        PlaceData placeData = new PlaceData(data.get(0), data.get(1), data.get(2), data.get(3), data.get(4), data.get(5),
//                data.get(6), data.get(7), data.get(8), data.get(9), data.get(10), data.get(11), data.get(12), data.get(13)
//        ,data.get(14),data.get(15));
//        dao.add(placeData).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Toast.makeText(getContext(), "Successfully Inserted", Toast.LENGTH_SHORT).show();
//            }
//        });
//        Bundle b = new Bundle();
//        b.putStringArrayList("data",data);
        FragmentManager fragmentManager = this.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down);
        fragmentTransaction.addToBackStack(null);
//        UploadImages uploadImages = new UploadImages();
//        uploadImages.setArguments(b);
        fragmentTransaction.replace(R.id.fragment_form_container, new Handrail()).commit();
    }

    public void initializeAdapters(){
        step.setText("Step: 03");
        title.setText("Facilities Available");
        String[] facility=getResources().getStringArray(R.array.facilities);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(),R.layout.dropdown_btype,facility);
        ramp.setAdapter(adapter);
//        String[] facility_lift=getResources().getStringArray(R.array.facility_lift);
//        ArrayAdapter<String> adapterLift=new ArrayAdapter<>(getContext(),R.layout.dropdown_btype,facility_lift);
//        handrail.setAdapter(adapter);
//        toilet.setAdapter(adapter);
//        braille.setAdapter(adapter);
//        lifts.setAdapter(adapterLift);
//        wheelchair.setAdapter(adapter);
    }
}