package com.example.disha.Reviews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.disha.R;

public class ActivityReview extends AppCompatActivity {
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            onBackPressed();
        });
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_review_container,new ListLocations()).
                setCustomAnimations(R.anim.slide_up,R.anim.slide_down).
                commit();
    }
}