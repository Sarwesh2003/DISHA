package com.example.disha.ViewDetails.BottomSheet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.disha.MainActivity;
import com.example.disha.R;
import com.example.disha.locationModel.Location;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Period;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.maps.android.SphericalUtil;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomBottomSheet {
    LinearLayout mLayout;
    Context context;
    Place place;
    View root;
    TextView placeName, address, distance, ratings_txt, disp_phone, bstatus;
    RatingBar ratings;
    PlacesClient placesClient;
    Location location;
    private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;

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

    public void initComponent() {
        placeName = (TextView) mLayout.findViewById(R.id.placeName);
        address = (TextView) mLayout.findViewById(R.id.paddress);
        distance = (TextView) mLayout.findViewById(R.id.distance);
        ratings = (RatingBar) mLayout.findViewById(R.id.display_ratings);
        ratings_txt = (TextView) mLayout.findViewById(R.id.rating_txt);
        disp_phone = (TextView) mLayout.findViewById(R.id.display_phone);
        bstatus = (TextView) mLayout.findViewById(R.id.business_status);
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
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
                Place.Field.RATING, Place.Field.USER_RATINGS_TOTAL, Place.Field.PHOTO_METADATAS,
                Place.Field.BUSINESS_STATUS, Place.Field.ICON_URL, Place.Field.ICON_BACKGROUND_COLOR);
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
