package com.example.disha.Main.BottomSheet;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.example.disha.AddPlace.data.DAOPlaceData;
import com.example.disha.AddPlace.data.PlaceData;
import com.example.disha.R;
import com.example.disha.Reviews.ActivityReview;
import com.example.disha.ViewDetails.ViewDetails;
import com.example.disha.locationModel.Location;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomBottomSheet {
    LinearLayout mLayout;
    Context context;
    Place place;
    View root;
    TextView placeName, address, distance, ratings_txt, disp_phone, bstatus;
    RatingBar ratings;
    PlacesClient placesClient;
    Location location;
    String[] data = null;
    boolean flag = false;
    private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;
    private AppCompatButton view_details_btn, reviewBtn;
    TextView directions_btn;

    public CustomBottomSheet(View root, Context context) {
        this.root = root;
        this.context = context;
        this.mLayout = (LinearLayout) root.findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(mLayout);
        bottomSheetBehavior.setFitToContents(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        placesClient = Places.createClient(context);
    }

    public void setState(int state){
        bottomSheetBehavior.setState(state);
    }
    public int getState(){
        return bottomSheetBehavior.getState();
    }
    public void initComponent() {
        placeName = (TextView) mLayout.findViewById(R.id.placeName);
        address = (TextView) mLayout.findViewById(R.id.paddress);
        distance = (TextView) mLayout.findViewById(R.id.distance);
        ratings = (RatingBar) mLayout.findViewById(R.id.display_ratings);
        ratings_txt = (TextView) mLayout.findViewById(R.id.rating_txt);
        disp_phone = (TextView) mLayout.findViewById(R.id.display_phone);
        bstatus = (TextView) mLayout.findViewById(R.id.business_status);
        view_details_btn = root.findViewById(R.id.view_details_page);
        directions_btn = root.findViewById(R.id.redirect_maps);
        reviewBtn = root.findViewById(R.id.reviewPlace);
        view_details_btn.setOnClickListener(v -> {
            getAllDetails(place);
        });
        reviewBtn.setOnClickListener(v -> {
            Intent view = new Intent(context, ActivityReview.class);
            view.putExtra("Place", place.getName());
            context.startActivity(view);
        });
        directions_btn.setOnClickListener(v -> {
            String lat = String.valueOf(place.getLatLng().latitude);
            String lng = String.valueOf(place.getLatLng().longitude);
            String label = "Redirected from DISHA";
            Uri mapUri = Uri.parse("geo:0,0?q="+lat+","+lng+"("+label+")");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(mapIntent);
        });
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
    }

    private void getAllDetails(Place place) {
        DAOPlaceData daoPlaceData = new DAOPlaceData();
        Query retquery = daoPlaceData.getReference().orderByChild("placeName").equalTo(place.getName());
        retquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    detailsFetched(true);
                }else{
                    detailsFetched(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Something went wrong. Try Again", Toast.LENGTH_SHORT).show();
            }
        });

//        daoPlaceData.getReference().addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot root) {
//                if(root.exists()){
//                    boolean flag = false;
//                    for(DataSnapshot snapshot : root.getChildren()){
//                        String child = snapshot.child("placeName").getValue(String.class);
//                        if(child.equals(place.getName())){
//                            PlaceData data = snapshot.getValue(PlaceData.class);
//                            flag = true;
//                            detailsFetched(data, true);
//                        }
//                    }
//                    if(!flag){
//                        detailsFetched(null, false);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(context, "Error Occurred", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    private void detailsFetched(boolean flg) {
        Intent view = new Intent(context, ViewDetails.class);
        if(flg){
            view.putExtra("PlaceFound", true);
            view.putExtra("PlaceName", place.getName());
        }else{
            view.putExtra("PlaceFound", false);
        }
        context.startActivity(view);
    }

    public void setPlace(Place place, Location location){
        this.place = place;
        this.location = location;
    }

    public void setData() {
        placeName.setText(place.getName());
        address.setText(place.getAddress());
        double dis = 0.0;
        if (place.getLatLng() != null){
            dis = SphericalUtil.computeDistanceBetween(location.getMyLocation(), place.getLatLng());
            distance.setText(String.format("%.2f", dis / 1000) + " km");
        }
        try{
            requestToGoogle(place.getId());
        }catch (Exception e){
            Toast.makeText(context, "Failed to get data", Toast.LENGTH_SHORT).show();
        }

    }
    public void requestToGoogle(String placeId) throws Exception {
        if(placeId == null){
            throw new Exception();
        }
        List<Place.Field> fieldList = Arrays.asList(Place.Field.PHONE_NUMBER,
                Place.Field.RATING, Place.Field.USER_RATINGS_TOTAL,
                Place.Field.BUSINESS_STATUS);
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, fieldList);

        placesClient.fetchPlace(request).addOnSuccessListener(fetchPlaceResponse -> {
            Place place = fetchPlaceResponse.getPlace();
            if(place.getPhoneNumber() != null)
                disp_phone.setText(place.getPhoneNumber());
            else
                disp_phone.setText("Not Available");

            if(place.getRating() != null) {
                if(ratings.getVisibility() == View.GONE)
                    ratings.setVisibility(View.VISIBLE);
                ratings.setRating(Float.parseFloat(String.valueOf(place.getRating())));
            }else {
                ratings.setVisibility(View.GONE);
                ratings_txt.setText("Not rated");
            }
            if(place.getUserRatingsTotal() != null)
                ratings_txt.setText(ratings.getRating()+"("+place.getUserRatingsTotal()+")");
            else
                ratings_txt.setText("Ratings not available");

            Place.BusinessStatus status = place.getBusinessStatus();
            if(status != null){
                bstatus.setText(customToString(status.toString()));
            }
//            Log.d("Returned", String.valueOf(place.getTypes()));
//            Log.d("Returned", String.valueOf(place.getBusinessStatus()));
        }).addOnFailureListener(e -> Toast.makeText(context, "Failed to get data", Toast.LENGTH_SHORT).show());

    }


    private String customToString(String str){
        str = str.toLowerCase();
        String t = "";
        for(String s:str.split("_")){
            t = t+s.substring(0,1).toUpperCase()+s.substring(1)+" ";
        }
        return t;
    }
}
