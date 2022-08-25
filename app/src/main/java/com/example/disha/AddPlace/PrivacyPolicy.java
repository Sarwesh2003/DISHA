package com.example.disha.AddPlace;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.disha.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;

public class PrivacyPolicy extends AppCompatActivity {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        CheckBox cbx = findViewById(R.id.iagree);
        AppCompatButton submit = findViewById(R.id.continueBtn);
        // Find the WebView by its unique ID
        WebView webView = findViewById(R.id.web);

        // loading http://www.google.com url in the WebView.
        webView.loadUrl("https://diksha-prasad.github.io/T-C_disha/");

        // this will enable the javascript.
        webView.getSettings().setJavaScriptEnabled(true);

        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        webView.setWebViewClient(new WebViewClient());
        webView.getProgress();
        submit.setOnClickListener(v -> {
            if(!cbx.isChecked()){
                cbx.setTextColor(Color.RED);
            }else{
                Intent start_add_place = new Intent(PrivacyPolicy.this, PlaceInfo.class);
                startActivity(start_add_place);
            }
        });
    }
}