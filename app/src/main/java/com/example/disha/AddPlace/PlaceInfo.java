package com.example.disha.AddPlace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import com.example.disha.R;
import com.google.android.material.textfield.TextInputEditText;

public class PlaceInfo extends AppCompatActivity {
    Toolbar toolbar;
    AppCompatButton prev;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_info);
        Initialize();

        prev = findViewById(R.id.prev);
        prev.setEnabled(false);
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
    }

    public void Initialize(){
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).addToBackStack(null).replace(R.id.fragment_form_container, new PlaceDetails()).commit();
    }
}