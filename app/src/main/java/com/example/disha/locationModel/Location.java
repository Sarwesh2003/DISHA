package com.example.disha.locationModel;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.disha.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Location {
    Context context;
    SupportMapFragment supportMapFragment;
    GoogleApiAvailability api;
    FusedLocationProviderClient client;
    LocationRequest request;
    Marker marker, marker2;
    LocationCallback callback;
    GoogleMap mGoogleMap;
    private LatLng myLocation;

    public Location(Context context, int id) {
        this.context = context;
        supportMapFragment = (SupportMapFragment) ((FragmentActivity) context).getSupportFragmentManager().findFragmentById(id);
        client = LocationServices.getFusedLocationProviderClient(context);
    }
    private LocationRequest getConfig() {
        request = LocationRequest.create();
        request.setInterval(10000);
        request.setFastestInterval(3000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return request;
    }

    public boolean PlayServiceAvailable() {
        api = GoogleApiAvailability.getInstance();
        int res = api.isGooglePlayServicesAvailable(context);
        return res == ConnectionResult.SUCCESS;

    }

    public void CheckPrerequisite() {
        if (PlayServiceAvailable()) {
            StartLocationService();
        } else {
            Toast.makeText(context, "Google Play Service Not Available", Toast.LENGTH_SHORT).show();
        }
    }


    private void StartLocationService() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }

        Task<android.location.Location> current = client.getLastLocation();
        current.addOnSuccessListener(location -> supportMapFragment.getMapAsync(googleMap -> {
            mGoogleMap = googleMap;
            myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            AddMarkerToPos(myLocation,true);
        }));
        //KeepUpdatingLocation();
    }


    public void getUpdates() {
        StartLocationService();
//        KeepUpdatingLocation();
    }

    public void AddMarkerToPos(LatLng latLng, boolean my_location) {
        MarkerOptions options;

        if(my_location){
            if (marker != null) {
                marker.remove();
            }
            options = new MarkerOptions().position(latLng)
                    .title("You are here").icon(BitmapFromVector(context,R.drawable.ic_location_pin));
        }
        else{
            options = new MarkerOptions().position(latLng)
                    .title("Happy Journey");
        }


        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        marker = mGoogleMap.addMarker(options);
    }

    public void KeepUpdatingLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        callback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                supportMapFragment.getMapAsync(googleMap -> {
                    for (android.location.Location locationUpdate : locationResult.getLocations()) {
                        LatLng latLng = new LatLng(locationUpdate.getLatitude(), locationUpdate.getLongitude());
                        AddMarkerToPos(latLng,true);
                    }
                });
            }
        };
        client.requestLocationUpdates(getConfig(), callback, null);
    }

    public LatLng GetLatLng() {
        return marker.getPosition();
    }

    public String GetAddress(LatLng latLng) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
                return "No Address Found";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Something went wrong";
        }
        return strAdd;
    }

    public void getPlace(String loc){
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                List<Address> addList = null;
                if(loc != null || !loc.equals("")){
                    Geocoder geocoder = new Geocoder(context);
                    try{
                        addList = geocoder.getFromLocationName(loc,1);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    Address add = addList.get(0);
                    AddMarkers(new LatLng(add.getLatitude(), add.getLongitude()));
                }
            }
        });

    }

    public void AddMarkers(LatLng latLng) {

        MarkerOptions options;
        if(marker2 != null){
            marker2.remove();
        }
        options = new MarkerOptions().position(latLng)
                    .title("Location");
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        marker2 = mGoogleMap.addMarker(options);
    }

    public void RemoveAllMarkers(){
        if(marker != null){
            marker.remove();
        }
        if(marker2 != null){
            marker2.remove();
        }
    }
    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    public LatLng getMyLocation(){
        return myLocation;
    }

}
