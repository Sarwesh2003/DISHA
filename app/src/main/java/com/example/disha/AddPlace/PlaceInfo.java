package com.example.disha.AddPlace;

import androidx.appcompat.app.AppCompatActivity;
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
    Button cont, prev;
    ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_info);
        Initialize();

        prev = findViewById(R.id.prev);
        backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });
        prev.setEnabled(false);

    }

    public void Initialize(){
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).replace(R.id.fragment_form_container, new PlaceDetails()).commit();
    }
}