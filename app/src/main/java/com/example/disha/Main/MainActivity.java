package com.example.disha.Main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.disha.R;
import com.example.disha.Main.BottomSheet.CustomBottomSheet;
import com.example.disha.locationModel.Location;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Location location;
    View root;
    private PlacesClient placeClient;

    public ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if(result.getData()!=null){
                        Place place = Autocomplete.getPlaceFromIntent(result.getData());
                        ShowData(place);
                    }
                }else if(result.getResultCode() == AutocompleteActivity.RESULT_ERROR){
                    Status status = Autocomplete.getStatusFromIntent(result.getData());
                    Toast.makeText(getApplicationContext(),status.getStatusMessage(),Toast.LENGTH_LONG).show();
                }
            });
    public ActivityResultLauncher<Intent> Audiolauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null) {
                        ArrayList<String> list = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (list.size() > 0) {
                            AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
                            FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                                    .setCountries("IN")
                                    .setSessionToken(token)
                                    .setQuery(list.get(0))
                                    .build();
                            placeClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
                                if(response.getAutocompletePredictions().size() > 0){
                                    AutocompletePrediction pred = response.getAutocompletePredictions().get(0);
                                    String placeId = pred.getPlaceId();
                                    final List<Place.Field> placeFields = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG,
                                            Place.Field.NAME, Place.Field.ID);

                                    final FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeId, placeFields);

                                    placeClient.fetchPlace(placeRequest).addOnSuccessListener((res) -> {
                                        Place place = res.getPlace();
                                        ShowData(place);

                                    }).addOnFailureListener((exception) -> {
                                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    });
                                }
                            }).addOnFailureListener((exception) -> {
                                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                }else if(result.getResultCode() == AutocompleteActivity.RESULT_ERROR){
                    Status status = Autocomplete.getStatusFromIntent(result.getData());
                    Toast.makeText(getApplicationContext(),status.getStatusMessage(),Toast.LENGTH_LONG).show();
                }
            });

    private CustomBottomSheet sheet;
    private TextToSpeech ttobj;
    MainController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        setContentView(root);

        controller = new MainController(MainActivity.this, root, launcher, Audiolauncher);
        controller.checkUser();
        sheet = new CustomBottomSheet(root,MainActivity.this);
        controller.init();
        placeClient = Places.createClient(MainActivity.this);
        location = new Location(MainActivity.this, R.id.map_fragment);
        location.CheckPrerequisite();
        controller.setSheet(sheet, location);
    }

    @Override
    public void onBackPressed()
    {
        switch(sheet.getState())
        {
            case BottomSheetBehavior.STATE_HIDDEN:
            case BottomSheetBehavior.STATE_COLLAPSED:
                super.onBackPressed();
                break;
            case BottomSheetBehavior.STATE_DRAGGING:
                sheet.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case BottomSheetBehavior.STATE_EXPANDED:
                sheet.setState(BottomSheetBehavior.STATE_HIDDEN);
            default:
                sheet.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
        }
    }
    private void ShowData(Place placeData) {
        sheet.setState(BottomSheetBehavior.STATE_EXPANDED);
        location.RemoveAllMarkers();
        location.AddMarkerToPos(placeData.getLatLng(), false);
        sheet.setPlace(placeData, location);
        sheet.initComponent();
        sheet.setData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        controller.speak();
    }
}