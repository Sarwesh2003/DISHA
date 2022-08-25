package com.example.disha.Main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.disha.AddPlace.data.DAOPlaceData;
import com.example.disha.Permission;
import com.example.disha.AddPlace.PrivacyPolicy;
import com.example.disha.R;
import com.example.disha.Main.BottomSheet.CustomBottomSheet;
import com.example.disha.locationModel.Location;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainController {
    private final DAOPlaceData daoPlaceData;
    private CustomBottomSheet sheet;
    Context context;
    View root;
    private Location location;
    private FloatingActionButton refresh, voiceAssistant;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private AutoCompleteTextView search;
    private FirebaseUser user;
    private Bitmap profileImg;
    private ImageButton menu_btn, voice_btn;
    private ActivityResultLauncher<Intent> launcher, audioLauncher, assistantLauncher;
    SpeechRecognizer speechRecognizer;
    TextToSpeech ttobj;
    boolean isListening = false;

    private AppCompatButton view_details_btn, directions_btn;

    private PlacesClient placeClient;

    public MainController(Context context, View root, ActivityResultLauncher<Intent> launcher, ActivityResultLauncher<Intent> audiolauncher, ActivityResultLauncher<Intent> assistantLauncher) {
        this.context = context;
        this.root = root;
        this.launcher = launcher;
        //Initializing PLaces API and DAO
        Places.initialize(context,"AIzaSyDWh2tZNTZKRJQQIs6pqspqEiX7f8mxl08");
        daoPlaceData = new DAOPlaceData();
        user = FirebaseAuth.getInstance().getCurrentUser();
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        this.audioLauncher = audiolauncher;
        this.assistantLauncher = assistantLauncher;
        placeClient = Places.createClient(context);
    }
    public void init(){
        drawerLayout = root.findViewById(R.id.drawer_layout);
        navigationView = root.findViewById(R.id.navigation_view);

        refresh = root.findViewById(R.id.recenter);
        search = root.findViewById(R.id.searchEdit);
        menu_btn = root.findViewById(R.id.menu_btn);
        voice_btn = root.findViewById(R.id.voice);
        voiceAssistant = root.findViewById(R.id.voiceAssistant);
        search.setFocusable(false);
        profileImg = drawableToBitmap(context.getDrawable(R.drawable.ic_profile));
        getPhoto();
        Log.d("Myaddr", "You are on the main screen and your current address is"
                + location.getCityStateCountry(location.getMyLocation()));
        speak("You are on the main screen and your current address is"
                + location.getCityStateCountry(location.getMyLocation()));

        menu_btn.setOnClickListener(v -> handleMenu());

        voice_btn.setOnClickListener(v -> {
            Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            i.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-IN");
            audioLauncher.launch(i);
        });
        voiceAssistant.setOnClickListener(v -> {
            Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            i.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-IN");
            assistantLauncher.launch(i);
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id)
                {

                    case R.id.add_place:
                        Intent start_privacy_policy = new Intent(context, PrivacyPolicy.class);
                        context.startActivity(start_privacy_policy);
                        break;
                    case R.id.view_profile:
                        Toast.makeText(context, "View Profile is Clicked",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        Intent start_settings = new Intent(context, Settings.class);
                        context.startActivity(start_settings);
                        break;
                        case R.id.logout:
                        signout();
                        break;
                    case R.id.share:
                        Toast.makeText(context, "Share is Clicked",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.feedback:
                        Toast.makeText(context, "Feedback is Clicked",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.contribute:
                        String uri = "https://github.com/Sarwesh2003/DISHA";
                        Intent sharingIntent = new Intent(Intent.ACTION_VIEW);
                        sharingIntent.setData(Uri.parse(uri));
                        context.startActivity(sharingIntent);
                        break;
                    default:
                        return true;

                }
                return true;
            }
        });
        search.setOnClickListener(v -> handleSearch());
        refresh.setOnClickListener(v -> handleRefresh());
    }

    public void speak(String str){
        SharedPreferences settings = context.getSharedPreferences("Settings", 0);
        boolean silent = settings.getBoolean("audio", true);
        if(!silent)
            return;
        ttobj = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if(status != TextToSpeech.ERROR){
                    ttobj.setLanguage(Locale.ENGLISH);
                    ttobj.setSpeechRate(0.7f);
                    ttobj.speak(str, TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });
    }


    private void handleRefresh() {
        location.RemoveAllMarkers();
        location.getUpdates();
        sheet.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void handleSearch() {
        List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG,
                Place.Field.NAME, Place.Field.ID);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                .build(context );
        launcher.launch(intent);
//        ShowData(nashik);

    }


    private void ShowData(Place placeData) {
        sheet.setState(BottomSheetBehavior.STATE_EXPANDED);
        location.RemoveAllMarkers();
        location.AddMarkerToPos(placeData.getLatLng(), false);
        sheet.setPlace(placeData, location);
        sheet.initComponent();
        sheet.setData();
    }
    private void handleMenu() {

        FirebaseUser finalUser = user;
        drawerLayout.openDrawer(GravityCompat.START);
        TextView user_name = navigationView.findViewById(R.id.user_name);
        TextView gmail = navigationView.findViewById(R.id.gmail);
        CircleImageView img = navigationView.findViewById(R.id.profilepic);
        if(finalUser != null) {
            String name = finalUser.getDisplayName();
            String email = finalUser.getEmail();
            String imgUri = String.valueOf(user.getPhotoUrl());
            if (name == null || name.isEmpty()) {
                user_name.setText(finalUser.getPhoneNumber());
            } else {
                user_name.setText(name);
            }

            if (email == null || email.isEmpty()) {
                gmail.setText("Gmail not found");
            } else {
                gmail.setText(finalUser.getEmail());
            }
            img.setImageBitmap(profileImg);
        }
    }
    public void checkUser(){

        if(user == null){
            Intent intent = new Intent(context, Permission.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
    private void getPhoto(){
        new DownloadImageFromInternet(profileImg)
                .execute(String.valueOf(user.getPhotoUrl()));
    }


    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        public DownloadImageFromInternet(Bitmap profileImg) {
//            Toast.makeText(), "Getting Information...",Toast.LENGTH_SHORT).show();
        }
        protected Bitmap doInBackground(String... urls) {
            String imageURL=urls[0];
            Bitmap bimage = null;
            try {
                InputStream in=new java.net.URL(imageURL).openStream();
                bimage= BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
            }
            return bimage;
        }
        protected void onPostExecute(Bitmap result) {
            if(result != null)
                profileImg = result;
        }
    }
    private void signout(){

        //To Sign Out User
        AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(context, Permission.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        Toast.makeText(context,"Sign-Out Successful",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"Sign-Out Unsuccessful",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setSheet(CustomBottomSheet sheet, Location location){
        this.sheet = sheet;
        this.location = location;
    }

}
