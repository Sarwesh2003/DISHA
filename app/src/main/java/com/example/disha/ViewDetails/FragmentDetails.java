package com.example.disha.ViewDetails;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.disha.AddPlace.data.DAOPlaceData;
import com.example.disha.AddPlace.data.PlaceData;
import com.example.disha.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Locale;

public class FragmentDetails extends Fragment {
    View root;
    PlaceData data;
    TextView placeName, placeDescription, placeAddress, contact, toilet, ramp, handrail, braille, facilities, lift, wheelchair;
    ImageView rampImg, handrailImg, brailleImg, liftImg, wheelchairImg, toiletImg;
    DAOPlaceData dao;
    public FragmentDetails(PlaceData data) {
        dao = new DAOPlaceData();
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
        toiletImg = root.findViewById(R.id.toilets_image);
        ramp = root.findViewById(R.id.ramp);
        rampImg = root.findViewById(R.id.ramp_image);
        handrail = root.findViewById(R.id.handrailFacility);
        handrailImg = root.findViewById(R.id.handrail_image);
        braille = root.findViewById(R.id.brailleInfo);
        brailleImg = root.findViewById(R.id.braille_image);
        lift = root.findViewById(R.id.lift);
        liftImg = root.findViewById(R.id.lifts_image);
        wheelchair = root.findViewById(R.id.wheelchair);
        wheelchairImg = root.findViewById(R.id.wheelchair_image);
        if(data != null)
            Inititalize();

        return root;
    }

    public void Inititalize(){
        ProgressDialog progress = new ProgressDialog(getContext());
        progress.setMessage("Getting Information");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        placeName.setText(data.getPlaceName());
        placeDescription.setText(data.getPlaceType()+", "+data.getInfrastructureType()+"\n"+data.getPlaceDescription());
        placeAddress.setText(data.getAddress());
        contact.setText(data.getPhoneNo());
        toilet.setText(data.getToilet()+"\n"+"Total Toilets Available: "+data.getNo_of_toilet()+"\n"+data.getToiletDescription());
        ramp.setText(data.getRamp() + "\n" + data.getRampDescription());
        handrail.setText(data.getHandrail() + "\n" + data.getHandrailDescription());
        braille.setText(data.getBraille() + "\n" + data.getBrailleDescription());
        lift.setText(data.getLifts() + "\n" +data.getLiftsDescription());
        wheelchair.setText(data.getWheelchair() + "\n" + data.getWheelchairDescription());

//        progress.setIndeterminate(true);
//        StorageReference rampRef = dao.getStorageReference().child(data.getPlaceName() + "/Facilities/Ramp");
//        rampRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                rampImg.setVisibility(View.VISIBLE);
//                Glide.with(getContext()).load(uri).into(rampImg);
//            }
//        });
        StorageReference rampRef = dao.getStorageReference().child(data.getPlaceName() + "/Facilities");
        rampRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for(StorageReference img : listResult.getItems()){
                    if(img.getName().toLowerCase(Locale.ROOT).contains("ramp")){
                        rampImg.setVisibility(View.VISIBLE);
                        img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(getContext()).load(uri).into(rampImg);
                            }
                        });

                    }else if(img.getName().toLowerCase(Locale.ROOT).contains("handrail") ){
                        handrailImg.setVisibility(View.VISIBLE);
                        img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(getContext()).load(uri).into(handrailImg);
                            }
                        });
                    }else if(img.getName().toLowerCase(Locale.ROOT).contains("lifts")){
                        liftImg.setVisibility(View.VISIBLE);
                        img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(getContext()).load(uri).into(liftImg);
                            }
                        });
                    }else if(img.getName().toLowerCase(Locale.ROOT).contains("braille")){
                        brailleImg.setVisibility(View.VISIBLE);
                        img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(getContext()).load(uri).into(brailleImg);
                            }
                        });
                    }else if(img.getName().toLowerCase(Locale.ROOT).contains("toilet")){
                        toiletImg.setVisibility(View.VISIBLE);
                        img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(getContext()).load(uri).into(toiletImg);
                            }
                        });
                    }else if(img.getName().toLowerCase(Locale.ROOT).contains("wheelchair")){
                        wheelchairImg.setVisibility(View.VISIBLE);
                        img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(getContext()).load(uri).into(wheelchairImg);
                            }
                        });
                    }
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<ListResult>() {
            @Override
            public void onComplete(@NonNull Task<ListResult> task) {
                progress.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.dismiss();
            }
        });
    }
}