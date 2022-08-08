package com.example.disha.ViewDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.disha.AddPlace.data.DAOPlaceData;
import com.example.disha.AddPlace.data.PlaceData;
import com.example.disha.R;
import com.example.disha.Reviews.dataModel.DAOReview;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ViewDetails extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    DAOPlaceData dao;
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
        dao = new DAOPlaceData();
        Bundle b = getIntent().getExtras();
        boolean dataAvailable = b.getBoolean("PlaceFound");
        if(dataAvailable){
            String placeName = b.getString("PlaceName");
            LoadData(placeName);
        }
    }

    private void LoadData(String place) {
        Query retquery = dao.getReference().orderByChild("placeName").equalTo(place);
        PlaceData data  = new PlaceData();
        DAOReview review = new DAOReview();
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
                            getSupportFragmentManager(), data);

                }else{
                    Query getReview = review.getReference().orderByChild("placeName").equalTo(place);
                    getReview.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){

                            }else{

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                viewPager.setAdapter(viewPagerAdapter);
                // It is used to join TabLayout with ViewPager.
                tabLayout.setupWithViewPager(viewPager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewDetails.this, "Something went wrong. Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}