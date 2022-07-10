package com.example.disha.ViewDetails;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.disha.R;
import com.example.disha.AddPlace.data.DAOPlaceData;
import com.example.disha.AddPlace.data.PlaceData;
import com.example.disha.AddPlace.data.SliderAdapter;
import com.example.disha.AddPlace.data.SliderData;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;


public class BottomsheetShowData extends BottomSheetDialogFragment {
    View root;
    Place placeData;
    DAOPlaceData dao;
    private ArrayList<SliderData> sliderDataArrayList;
    SliderView sliderView;
    ImageButton btn_close;
    TextView title, showDes, showFacility, showAdd;
    AppCompatButton directions, details;
    public BottomsheetShowData(Place placeData) {
        this.placeData = placeData;
        dao = new DAOPlaceData();
        //mStorageRef = dao.getStorageReference().getReference().child(placeData.getPlaceName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_bottomsheet_show_data, container, false);
        sliderView = root.findViewById(R.id.slider);
        btn_close = root.findViewById(R.id.close);
        title = root.findViewById(R.id.viewTitle);
        showDes = root.findViewById(R.id.abt);
        showFacility = root.findViewById(R.id.facility);
        showAdd = root.findViewById(R.id.addrs);
        directions = root.findViewById(R.id.directions);
        details = root.findViewById(R.id.details);
        Initallize();
        title.setText(placeData.getName());
        btn_close.setOnClickListener(v->{
            dismiss();
        });

        directions.setOnClickListener(v -> {
            LatLng instance=placeData.getLatLng();
            String uri = "geo:" + instance.latitude + ","
                    +instance.longitude + "?q=" + instance.latitude
                    + "," + instance.longitude;
            startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
        });
//
//        details.setOnClickListener(v -> {
//            Intent startnew = new Intent(getActivity(), ViewDetails.class);
//            String[] data = new String[]{
//                    placeData.getPlaceName(),
//                    placeData.getPlacedescription(),
//                    placeData.getPhoneNo(),
//                    placeData.getPlaceType(),
//                    placeData.getInfraType(),
//                    placeData.getLang(),
//                    placeData.getLat(),
//                    placeData.getAddress(),
//                    placeData.getRamp(),
//                    placeData.getHandrail(),
//                    placeData.getToilet(),
//                    placeData.getNtoilet(),
//                    placeData.getBraille(),
//                    placeData.getLifts(),
//                    placeData.getWheelchair(),
//                    placeData.getDesc()
//            };
//            startnew.putExtra("Data", data);
//            startActivity(startnew);
//        });

        return root;
    }

    private void Initallize() {
//        getImages();
        setData();
    }

    private void setData() {
        showAdd.setText(placeData.getAddress());
        showDes.setText((CharSequence) placeData.getOpeningHours());
        showFacility.setText(placeData.getPhoneNumber());
    }

    private void getImages() {
//        StorageReference listRef = FirebaseStorage.getInstance().getReference().child(placeData.getPlaceName());
//        sliderDataArrayList = new ArrayList<>();
//        listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
//            @Override
//            public void onSuccess(ListResult listResult) {
//
//                for(StorageReference file:listResult.getItems()){
//                    file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            sliderDataArrayList.add(new SliderData(uri.toString()));
//                        }
//                    }).addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            SliderAdapter adapter = new SliderAdapter(getContext(), sliderDataArrayList);
//                            sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
//                            sliderView.setSliderAdapter(adapter);
//                            sliderView.setScrollTimeInSec(3);
//                            sliderView.setAutoCycle(true);
//                            sliderView.startAutoCycle();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);

    }
}