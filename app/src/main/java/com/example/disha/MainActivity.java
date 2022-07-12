package com.example.disha;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.disha.AddPlace.PlaceInfo;
import com.example.disha.AddPlace.data.DAOPlaceData;
import com.example.disha.Reviews.ActivityReview;
import com.example.disha.locationModel.Location;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlusCode;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.appbar.MaterialToolbar;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.maps.android.SphericalUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    Location location;
    FloatingActionButton  refresh;
    ProgressDialog progressDialog;
    DAOPlaceData daoPlaceData;
    EditText search;
    FirebaseUser user;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView placeName, address, phone, description, distance;
    ActivityResultLauncher<Intent> profileImgIntent;
    private final ArrayList<String> suggestionsArray = new ArrayList<String>();
    private final ArrayList<String> dummyArray = new ArrayList<String>();
    private Bitmap profileImg;
    private Uri imgFile;
    private BottomSheetBehavior bottomSheetBehavior;
    public ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
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
    private Place nashik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initializing Facebook Auth
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(getApplication());

        //Getting User
        user = FirebaseAuth.getInstance().getCurrentUser();

        //Initializing PLaces API and DAO
        Places.initialize(getApplicationContext(),"AIzaSyDWh2tZNTZKRJQQIs6pqspqEiX7f8mxl08");
        daoPlaceData = new DAOPlaceData();

        //UI
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        location = new Location(this, R.id.map_fragment);
        location.CheckPrerequisite();
        refresh = findViewById(R.id.recenter);
        search = findViewById(R.id.searchEdit);
        search.setFocusable(false);

        //Getting Profile Image
        profileImg = drawableToBitmap(getDrawable(R.drawable.ic_profile));

//        boolean phone = checkPhone(user);
        new DownloadImageFromInternet()
            .execute(String.valueOf(user.getPhotoUrl()));
        initComponent();

        nashik = new Place() {
            @Nullable
            @Override
            public Uri getWebsiteUri() {
                return null;
            }

            @Nullable
            @Override
            public LatLng getLatLng() {
                return new LatLng(19.9975, 73.7898);
            }

            @Nullable
            @Override
            public LatLngBounds getViewport() {
                return null;
            }

            @Nullable
            @Override
            public AddressComponents getAddressComponents() {
                return null;
            }

            @Nullable
            @Override
            public OpeningHours getOpeningHours() {
                return null;
            }

            @Nullable
            @Override
            public BusinessStatus getBusinessStatus() {
                return null;
            }

            @Nullable
            @Override
            public PlusCode getPlusCode() {
                return null;
            }

            @Nullable
            @Override
            public Double getRating() {
                return 5.0;
            }

            @Nullable
            @Override
            public Integer getIconBackgroundColor() {
                return null;
            }

            @Nullable
            @Override
            public Integer getPriceLevel() {
                return 4;
            }

            @Nullable
            @Override
            public Integer getUserRatingsTotal() {
                return 5;
            }

            @Nullable
            @Override
            public Integer getUtcOffsetMinutes() {
                return null;
            }

            @Nullable
            @Override
            public String getAddress() {
                return "Humare Dilo Ma...";
            }

            @Nullable
            @Override
            public String getIconUrl() {
                return null;
            }

            @Nullable
            @Override
            public String getId() {
                return null;
            }

            @Nullable
            @Override
            public String getName() {
                return "Nashik";
            }

            @Nullable
            @Override
            public String getPhoneNumber() {
                return "7709436123";
            }

            @Nullable
            @Override
            public List<String> getAttributions() {
                return null;
            }

            @Nullable
            @Override
            public List<PhotoMetadata> getPhotoMetadatas() {
                return null;
            }

            @Nullable
            @Override
            public List<Type> getTypes() {
                return null;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {

            }
        };

        //Setting click listeners
        toolbar.setNavigationOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        search.setOnClickListener(this);
        refresh.setOnClickListener(this);

    }

    private void initComponent() {
        // get the bottom sheet view
        LinearLayout llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        placeName = (TextView) llBottomSheet.findViewById(R.id.placeName);
        address = (TextView) llBottomSheet.findViewById(R.id.paddress);
        distance = (TextView) llBottomSheet.findViewById(R.id.distance);
        // init the bottom sheet behavior
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);

        // change the state of the bottom sheet
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        bottomSheetBehavior.setFitToContents(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void getImageURI(String imgName) {
        final Uri[] imgUri = {null};
        StorageReference storageRef = daoPlaceData.getStorageReference();
        storageRef.child("Users/"+imgName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(Uri.parse(String.valueOf(uri)))
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
//                                                    Toast.makeText(getApplicationContext(), "Profile Photo Updated", Toast.LENGTH_SHORT).show();
//                                                    updateUI();
                                    new DownloadImageFromInternet()
                                            .execute(String.valueOf(user.getPhotoUrl()));
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Something went wrong. Try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        return;
    }


    private void openIntent(ActivityResultLauncher<Intent> res){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        res.launch(intent);
    }
    private boolean checkPhone(FirebaseUser finalUser) {
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                String providerId = profile.getProviderId();
                if(providerId.contains("phone") || providerId.contains("Phone")){
                    return true;
                }
            }
        }
        return false;
    }

    private void signout(){

        //To Sign Out User
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(MainActivity.this, Permission.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Sign-Out Successful",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Sign-Out Unsuccessful",Toast.LENGTH_SHORT).show();
            }
        });
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

    private void ShowData(Place placeData) {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        location.RemoveAllMarkers();
        location.AddMarkerToPos(placeData.getLatLng(), false);

        placeName.setText(placeData.getName());
        address.setText(placeData.getAddress());

//        double dis = distance(location.getMyLocation().latitude, location.getMyLocation().longitude,
//                placeData.getLatLng().latitude,placeData.getLatLng().longitude);
        double dis = SphericalUtil.computeDistanceBetween(location.getMyLocation(), placeData.getLatLng());

        distance.setText(String.format("%.2f", dis / 1000)+" km");
        //        BottomsheetShowData fragment = new BottomsheetShowData(placeData);
//        fragment.setCancelable(false);
//        fragment.show(getSupportFragmentManager(), "TAG");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (id)
        {

            case R.id.add_place:
                Intent start_add_place = new Intent(MainActivity.this, PlaceInfo.class);
                startActivity(start_add_place);
                break;
            case R.id.survey:
                Intent start_review = new Intent(MainActivity.this, ActivityReview.class);
                startActivity(start_review);
                break;
            case R.id.view_profile:
                Toast.makeText(MainActivity.this, "View Profile is Clicked",Toast.LENGTH_SHORT).show();break;
            case R.id.logout:
                signout();
                break;
            case R.id.share:
                Toast.makeText(MainActivity.this, "Share is Clicked",Toast.LENGTH_SHORT).show();
                break;
            case R.id.feedback:
                Toast.makeText(MainActivity.this, "Feedback is Clicked",Toast.LENGTH_SHORT).show();
                break;
            case R.id.contribute:
                String uri = "https://github.com/Sarwesh2003/DISHA";
                Intent sharingIntent = new Intent(Intent.ACTION_VIEW);
                sharingIntent.setData(Uri.parse(uri));
                startActivity(sharingIntent);
                break;
            default:
                return true;

        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.topAppBar){
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
                if (imgUri.contains("null") || imgUri.isEmpty()) {
//                    Toast.makeText(getApplicationContext(), imgUri, Toast.LENGTH_SHORT).show();
                    img.setImageBitmap(profileImg);

                } else {
//                    Toast.makeText(getApplicationContext(), imgUri, Toast.LENGTH_SHORT).show();
                    img.setImageBitmap(profileImg);
                }
            }
        }else if(id == R.id.searchEdit){
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG,
                    Place.Field.NAME, Place.Field.OPENING_HOURS, Place.Field.PHONE_NUMBER);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                    .build(MainActivity.this);
//            someActivityResultLauncher.launch(intent);
            ShowData(nashik);
        } else if(id == R.id.recenter){
            location.RemoveAllMarkers();
            location.getUpdates();
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        CircleImageView imageView;
        public DownloadImageFromInternet() {
//            Toast.makeText(getApplicationContext(), "Getting Information...",Toast.LENGTH_SHORT).show();
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
    @Override
    public void onBackPressed()
    {
        switch(bottomSheetBehavior.getState())
        {
            case BottomSheetBehavior.STATE_HIDDEN:
                super.onBackPressed();
                break;
            case BottomSheetBehavior.STATE_DRAGGING:
                super.onBackPressed();
                break;
            case BottomSheetBehavior.STATE_EXPANDED:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case BottomSheetBehavior.STATE_HALF_EXPANDED:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case BottomSheetBehavior.STATE_COLLAPSED:
                super.onBackPressed();
                break;
            case BottomSheetBehavior.STATE_SETTLING:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            default:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
        }
    }

}