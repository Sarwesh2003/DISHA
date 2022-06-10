package com.example.disha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.disha.AddPlace.PlaceInfo;
import com.example.disha.Reviews.ActivityReview;
import com.example.disha.ViewDetails.BottomsheetShowData;
import com.example.disha.AddPlace.data.DAOPlaceData;
import com.example.disha.AddPlace.data.PlaceData;
import com.example.disha.locationModel.Location;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    Location location;
    FloatingActionButton  refresh;
    ProgressDialog progressDialog;
    DAOPlaceData daoPlaceData;
    private final ArrayList<String> suggestionsArray = new ArrayList<String>();
    private final ArrayList<String> dummyArray = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        daoPlaceData = new DAOPlaceData();
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
//        toolbar.getOverflowIcon().setColorFilter(Color.BLACK , PorterDuff.Mode.SRC_ATOP);
        location = new Location(this, R.id.map_fragment);
        location.CheckPrerequisite();
        refresh = findViewById(R.id.recenter);
        refresh.setOnClickListener(v ->{
            location.getUpdates();
        });
        getAllPlaceNames();
    }

    private void getAllPlaceNames() {
        Query retquery = daoPlaceData.getReference();
        retquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                populateSearch(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void populateSearch(DataSnapshot snapshot) {
        if(snapshot.exists()){
            for(DataSnapshot s : snapshot.getChildren()){
                String nm = s.child("placeName").getValue(String.class);
                dummyArray.add(nm);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.search){
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setQueryHint("Type here");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    progressDialog
                            = new ProgressDialog(MainActivity.this);
                    progressDialog.setTitle("Searching Data...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    PlaceData placeData;
                    placeData = new PlaceData();
                    String loc = searchView.getQuery().toString().trim();
                    Query retquery = daoPlaceData.getReference().orderByChild("placeName").equalTo(loc);
                    retquery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                for (DataSnapshot s : snapshot.getChildren()) {
                                    placeData.setPlaceName(s.child("placeName").getValue(String.class));
                                    placeData.setAddress(s.child("address").getValue(String.class));
                                    placeData.setBraille(s.child("braille").getValue(String.class));
                                    placeData.setDesc(s.child("desc").getValue(String.class));
                                    placeData.setHandrail(s.child("handrail").getValue(String.class));
                                    placeData.setInfraType(s.child("infraType").getValue(String.class));
                                    placeData.setLang(s.child("lang").getValue(String.class));
                                    placeData.setLat(s.child("lat").getValue(String.class));
                                    placeData.setLifts(s.child("lifts").getValue(String.class));
                                    placeData.setNtoilet(s.child("ntoilet").getValue(String.class));
                                    placeData.setPhoneNo(s.child("phoneNo").getValue(String.class));
                                    placeData.setPlaceType(s.child("placeType").getValue(String.class));
                                    placeData.setPlacedescription(s.child("placedescription").getValue(String.class));
                                    placeData.setRamp(s.child("ramp").getValue(String.class));
                                    placeData.setToilet(s.child("toilet").getValue(String.class));
                                    placeData.setWheelchair(s.child("wheelchair").getValue(String.class));
                                    ShowData(placeData);
                                }
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Place not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }

                    });
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

        }
        else if(item.getItemId() == R.id.add_place){
            Intent placeInfo = new Intent(MainActivity.this, PlaceInfo.class);
            startActivity(placeInfo);
        }
        else if(item.getItemId() == R.id.survey){
            Intent review = new Intent(MainActivity.this, ActivityReview.class);
            startActivity(review);
        }

        return super.onOptionsItemSelected(item);
    }

    private void ShowData(PlaceData placeData) {
        location.RemoveAllMarkers();
        location.AddMarkerToPos(new LatLng(Double.parseDouble(placeData.getLat()), Double.parseDouble(placeData.getLang())), false);
        progressDialog.dismiss();
        BottomsheetShowData fragment = new BottomsheetShowData(placeData);
        fragment.setCancelable(false);
        fragment.show(getSupportFragmentManager(), "TAG");
    }
}