package com.example.disha.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.disha.Profile.ViewProfile;
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
import java.util.Objects;

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

    public ActivityResultLauncher<Intent> assistantLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null) {
                        ArrayList<String> list = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (list.size() > 0) {
                            String command = list.get(0);
                            if(command.toLowerCase(Locale.ROOT).contains("search") && command.toLowerCase(Locale.ROOT).contains("place")){
                                speak("Disha Says Okay, please spell place name to search when prompted");
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                                        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                                        i.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
                                        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-IN");
                                        Audiolauncher.launch(i);
                                    }
                                }, 5000);
                            }else if(command.toLowerCase(Locale.ROOT).contains("current") && command.toLowerCase(Locale.ROOT).contains("location")){
                                Location loc = new Location(MainActivity.this, R.id.map_fragment);
                                speak("You current location is "+loc.GetAddress(location.getMyLocation()));
                            }else if(command.toLowerCase(Locale.ROOT).contains("my") || command.toLowerCase(Locale.ROOT).contains("qr")){
                                Intent start_view_profile = new Intent(MainActivity.this, ViewProfile.class);
                                startActivity(start_view_profile);
                            }
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
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        setContentView(root);

        controller = new MainController(MainActivity.this, root, launcher, Audiolauncher, assistantLauncher);
        controller.checkUser();
        sheet = new CustomBottomSheet(root,MainActivity.this);

        placeClient = Places.createClient(MainActivity.this);
        location = new Location(MainActivity.this, R.id.map_fragment);
        location.CheckPrerequisite();

        controller.setSheet(sheet, location);
        controller.init();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }
    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 12) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-IN");
                assistantLauncher.launch(i);
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
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
        controller.speak("You are on the main screen and your current address is"
                + location.getCityStateCountry(location.getMyLocation()));
    }
    public void speak(String str){
        SharedPreferences settings = getSharedPreferences("Settings", 0);
        boolean silent = settings.getBoolean("audio", true);
        if(!silent)
            return;
        ttobj = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if(status != TextToSpeech.ERROR){
                    ttobj.setLanguage(Locale.ENGLISH);
                    ttobj.setSpeechRate(0.7f);
                    ttobj.speak(str, TextToSpeech.QUEUE_ADD,null);
                }
            }
        });

    }
    @Override
    protected void onResume() {
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
}