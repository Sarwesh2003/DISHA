package com.example.disha.ViewDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.disha.AddPlace.data.DAOPlaceData;
import com.example.disha.AddPlace.data.PlaceData;
import com.example.disha.R;
import com.example.disha.Reviews.dataModel.DAOReview;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class ViewDetails extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    DAOPlaceData dao;
    private PlacesClient placesClient;
    String placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        placesClient = Places.createClient(ViewDetails.this);
        dao = new DAOPlaceData();
        Bundle b = getIntent().getExtras();
        boolean dataAvailable = b.getBoolean("PlaceFound");
        placeId = b.getString("PlaceId");
//        if(dataAvailable){
            String placeName = b.getString("PlaceName");
            LoadData(placeName);
    }

    private void LoadData(String place) {
        Query retquery = dao.getReference().orderByChild("placeName").equalTo(place);
        PlaceData data  = new PlaceData();
        retquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot element : snapshot.getChildren()){
                        data.setPlaceName(element.child("placeName").getValue(String.class));
                        data.setAddress(element.child("address").getValue(String.class));
                        data.setBraille(element.child("braille").getValue(String.class));
                        data.setBrailleDescription(element.child("brailleDescription").getValue(String.class));
                        data.setPlaceDescription(element.child("placeDescription").getValue(String.class));
                        data.setHandrail(element.child("handrail").getValue(String.class));
                        data.setHandrailDescription(element.child("handrailDescription").getValue(String.class));
                        data.setInfrastructureType(element.child("infrastructureType").getValue(String.class));
                        data.setLang(element.child("lang").getValue(String.class));
                        data.setLat(element.child("lat").getValue(String.class));
                        data.setLifts(element.child("lifts").getValue(String.class));
                        data.setLiftsDescription(element.child("liftsDescription").getValue(String.class));
                        data.setToilet(element.child("toilet").getValue(String.class));
                        data.setNo_of_toilet(element.child("no_of_toilet").getValue(String.class));
                        data.setToiletDescription(element.child("toiletDescription").getValue(String.class));
                        data.setPhoneNo(element.child("phoneNo").getValue(String.class));
                        data.setPlaceType(element.child("placeType").getValue(String.class));
                        data.setRamp(element.child("ramp").getValue(String.class));
                        data.setRampDescription(element.child("rampDescription").getValue(String.class));
                        data.setWheelchair(element.child("wheelchair").getValue(String.class));
                        data.setWheelchairDescription(element.child("wheelchairDescription").getValue(String.class));
                    }
                    viewPagerAdapter = new ViewPagerAdapter(
                            getSupportFragmentManager(), data, getApplicationContext());
                    viewPager.setAdapter(viewPagerAdapter);
                    // It is used to join TabLayout with ViewPager.
                    tabLayout.setupWithViewPager(viewPager);
                }else{
                    List<Place.Field> fieldList = Arrays.asList(Place.Field.NAME,Place.Field.PHONE_NUMBER, Place.Field.ADDRESS);
                    final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, fieldList);

                    placesClient.fetchPlace(request).addOnSuccessListener(fetchPlaceResponse -> {
                        Place place = fetchPlaceResponse.getPlace();
                        if(place.getPhoneNumber() != null)
                            data.setPhoneNo(place.getPhoneNumber());
                        else
                            data.setPhoneNo("Not Available");

                        Place.BusinessStatus status = place.getBusinessStatus();
                        if(status != null){
                            data.setPlaceDescription(customToString(status.toString()));
                        }
                        data.setPlaceName(place.getName());
                        data.setAddress(place.getAddress());
                        data.setPlaceType("");
                        data.setInfrastructureType("");

                        viewPagerAdapter = new ViewPagerAdapter(
                                getSupportFragmentManager(), data, getApplicationContext());
                        viewPager.setAdapter(viewPagerAdapter);
                        // It is used to join TabLayout with ViewPager.
                        tabLayout.setupWithViewPager(viewPager);
                    }).addOnFailureListener(e -> Toast.makeText(ViewDetails.this, "Failed to get data", Toast.LENGTH_SHORT).show());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewDetails.this, "Something went wrong. Try Again", Toast.LENGTH_SHORT).show();
            }
        });
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