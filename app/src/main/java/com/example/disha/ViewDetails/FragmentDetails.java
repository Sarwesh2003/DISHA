package com.example.disha.ViewDetails;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.disha.AddPlace.data.PlaceData;
import com.example.disha.R;

public class FragmentDetails extends Fragment {
    View root;
    PlaceData data;
    TextView placeName, placeDescription, placeAddress, contact, toilet, ramp, handrail, braille, facilities, lift, wheelchair;

    public FragmentDetails(PlaceData data) {

        this.data = data;
//        Log.d("From: FragmentDetails", data[0]);
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
        if(data != null)
            Inititalize();
        return root;
    }

    public void Inititalize(){
        placeName.setText(data.getPlaceName());
        placeDescription.setText(data.getPlaceType()+", "+data.getInfraType()+"\n"+data.getPlacedescription());
        placeAddress.setText(data.getAddress());
        contact.setText(data.getPhoneNo());
        toilet.setText(data.getToilet()+"\n"+"Total Toilets Available: "+data.getNtoilet());
        ramp.setText(data.getRamp());
        handrail.setText(data.getHandrail());
        braille.setText(data.getBraille());
        facilities.setText(data.getPlacedescription());
        lift.setText(data.getLifts());
        wheelchair.setText(data.getWheelchair());
    }
}