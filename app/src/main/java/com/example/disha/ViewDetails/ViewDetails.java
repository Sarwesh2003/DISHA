package com.example.disha.ViewDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.disha.AddPlace.data.DAOPlaceData;
import com.example.disha.AddPlace.data.PlaceData;
import com.example.disha.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ViewDetails extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    ImageView backBtn;
    DAOPlaceData dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        backBtn = findViewById(R.id.back);
        dao = new DAOPlaceData();
        Bundle b = getIntent().getExtras();
        boolean dataAvailable = b.getBoolean("PlaceFound");
        if(dataAvailable){
            String placeName = b.getString("PlaceName");
            LoadData(placeName);
        }
        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });
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
//                        data.setDesc(element.child("desc").getValue(String.class));
                        data.setHandrail(element.child("handrail").getValue(String.class));
//                        data.setInfraType(element.child("indraType").getValue(String.class));
                        data.setLang(element.child("lang").getValue(String.class));
                        data.setLat(element.child("lat").getValue(String.class));
                        data.setLifts(element.child("lifts").getValue(String.class));
//                        data.setNtoilet(element.child("ntoilet").getValue(String.class));
                        data.setPhoneNo(element.child("phoneNo").getValue(String.class));
                        data.setPlaceType(element.child("placeType").getValue(String.class));
//                        data.setPlacedescription(element.child("placedescription").getValue(String.class));
                        data.setRamp(element.child("ramp").getValue(String.class));
                        data.setToilet(element.child("toilet").getValue(String.class));
                        data.setWheelchair(element.child("wheelchair").getValue(String.class));
                    }
                    viewPagerAdapter = new ViewPagerAdapter(
                            getSupportFragmentManager(), data);
                    viewPager.setAdapter(viewPagerAdapter);
                    // It is used to join TabLayout with ViewPager.
                    tabLayout.setupWithViewPager(viewPager);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewDetails.this, "Something went wrong. Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}