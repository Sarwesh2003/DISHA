package com.example.disha.AddPlace;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.disha.R;
import com.example.disha.locationModel.Location;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment {
    Location location;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    Context ctx;
    HashMap<String, String> data;
    Button submit, prev;
    TextView title, step;
    Bundle data_bundle;
    public MapFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ctx = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map, container, false);
        supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.add_loc_map);
        client = LocationServices.getFusedLocationProviderClient(ctx);
        data = new HashMap<>();
        submit = getActivity().findViewById(R.id.continueBtn);
        title = getActivity().findViewById(R.id.tit);
        step = getActivity().findViewById(R.id.step);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        prev = getActivity().findViewById(R.id.prev);
        step.setText("Step: 02");
        title.setText("Select Location");
        data_bundle = this.getArguments();
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return v;
        }
        Task<android.location.Location> current = client.getLastLocation();
        current.addOnSuccessListener(location -> supportMapFragment.getMapAsync(googleMap -> {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            AddMarkerToPos(latLng, googleMap);
        }));

        submit.setOnClickListener(c -> {
            getAddress(data);
        });
        prev.setOnClickListener(l -> {
            getActivity().onBackPressed();
        });
        return v;
    }
    public void AddMarkerToPos(LatLng latLng, GoogleMap googleMap) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }
    public void getAddress(HashMap<String, String> d){
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                LatLng latlng=googleMap.getCameraPosition().target;
                String address = GetAddressofFragment(latlng);
                ShowToast(d, String.valueOf(latlng.latitude), String.valueOf(latlng.longitude), address);
            }
        });
    }

    private void ShowToast(HashMap<String, String> d, String lat, String lng, String address) {
        d.put("Lat",lat);
        d.put("Lang", lng);
        d.put("Address", address);
        sendData(d);
    }

    private void sendData(HashMap<String, String> d) {
//        Bundle b = new Bundle();
        data_bundle.putString("Lat", d.get("Lat"));
        data_bundle.putString("Lang", d.get("Lang"));
        data_bundle.putString("Address", d.get("Address"));
        FragmentManager fragmentManager = this.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down);
        fragmentTransaction.addToBackStack(null);
        Ramp q1 = new Ramp();
        q1.setArguments(data_bundle);
        fragmentTransaction.replace(R.id.fragment_form_container,q1).commit();
    }

    public String GetAddressofFragment(LatLng latLng) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
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
}