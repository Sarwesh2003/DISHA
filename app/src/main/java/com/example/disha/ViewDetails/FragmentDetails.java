package com.example.disha.ViewDetails;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.disha.R;

public class FragmentDetails extends Fragment {
    View root;
    String[] data;
    TextView placeName, placeDescription, placeAddress, contact, toilet, ramp, handrail, braille, facilities, lift, wheelchair;

    public FragmentDetails(String[] data) {
        this.data = data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_details, container, false);
        placeName = root.findViewById(R.id.placename);
        placeDescription = root.findViewById(R.id.placedescription);
        placeAddress = root.findViewById(R.id.address);
        contact = root.findViewById(R.id.contact);
        toilet = root.findViewById(R.id.toilets);
        ramp = root.findViewById(R.id.ramp);
        handrail = root.findViewById(R.id.handrailFacility);
        braille = root.findViewById(R.id.brailleInfo);
        facilities = root.findViewById(R.id.facilities);
        lift = root.findViewById(R.id.lift);
        wheelchair = root.findViewById(R.id.wheelchair);
        Inititalize();
        return root;
    }

    public void Inititalize(){
        placeName.setText(data[0]);
        placeDescription.setText(data[3]+", "+data[4]+"\n"+data[1]);
        placeAddress.setText(data[7]);
        contact.setText(data[2]);
        toilet.setText(data[10]+"\n"+"Total Toilets Available: "+data[11]);
        ramp.setText(data[8]);
        handrail.setText(data[9]);
        braille.setText(data[12]);
        facilities.setText(data[15]);
        lift.setText(data[13]);
        wheelchair.setText(data[14]);
    }
}