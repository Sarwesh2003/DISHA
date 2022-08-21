package com.example.disha;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.disha.Main.MainActivity;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Permission extends AppCompatActivity {
    private Button btnGrant, btnSignIn;
    TextToSpeech ttobj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        btnGrant = findViewById(R.id.btn_grant);
        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignIn.setEnabled(false);
        SharedPreferences settings = getSharedPreferences("Settings", 0);
        boolean silent = settings.getBoolean("audio", true);
        if(silent){
            ttobj = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(status != TextToSpeech.ERROR){
                        ttobj.setLanguage(Locale.ENGLISH);
                        ttobj.setSpeechRate(0.7f);
                        ttobj.speak("Welcome To Disha", TextToSpeech.QUEUE_ADD,null);
                    }
                }
            });
        }

        if(ContextCompat.checkSelfPermission(Permission.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(Permission.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
            btnGrant.setText("Permission Granted");
            if(silent){
                ttobj = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR){
                            ttobj.setLanguage(Locale.ENGLISH);
                            ttobj.setSpeechRate(0.7f);
                            ttobj.speak("Permissions are granted move to sign-in", TextToSpeech.QUEUE_ADD,null);
                        }
                    }
                });
            }
            btnGrant.setEnabled(false);
            btnSignIn.setEnabled(true);
        }
        FacebookSdk.setClientToken(getString(R.string.facebook_client_token));
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

        btnGrant.setOnClickListener(v -> {
            Dexter.withContext(Permission.this)
                        .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.RECORD_AUDIO)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            if(multiplePermissionsReport.areAllPermissionsGranted()){
                                btnGrant.setText("Permission Granted");
                                btnGrant.setEnabled(false);
                                btnSignIn.setEnabled(true);
                            }else{
                                if(multiplePermissionsReport.isAnyPermissionPermanentlyDenied()){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Permission.this);
                                    builder.setTitle("Permission Denied")
                                            .setMessage("Permissions are permanently denied. you need to go to setting to allow the permission.")
                                            .setNegativeButton("Cancel", null)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent();
                                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                                                }
                                            })
                                            .show();
                                } else {
                                    Toast.makeText(Permission.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).onSameThread()
                    .check();
        });

        btnSignIn.setOnClickListener(v->{
            if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                Intent startMain = new Intent(Permission.this, MainActivity.class);
                startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                return;
            }
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.PhoneBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                    new AuthUI.IdpConfig.TwitterBuilder().build(),
                    new AuthUI.IdpConfig.EmailBuilder().build());
            AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                    .Builder(R.layout.activity_sign_in)
                    .setGoogleButtonId(R.id.signInGoogle)
                    .setPhoneButtonId(R.id.signInPhone)
                    .setTwitterButtonId(R.id.signInTwitter)
                    .setEmailButtonId(R.id.signInEmail)
                    .build();
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setIsSmartLockEnabled(false)
                    .setAuthMethodPickerLayout(customLayout)
                    .setTheme(R.style.AppTheme)
                    .build();

            signInLauncher.launch(signInIntent);
        });

    }
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            Intent startMain = new Intent(Permission.this, MainActivity.class);
            startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        } else {
            Toast.makeText(getApplicationContext(), String.valueOf(response.getError()), Toast.LENGTH_SHORT).show();
        }
    }
}