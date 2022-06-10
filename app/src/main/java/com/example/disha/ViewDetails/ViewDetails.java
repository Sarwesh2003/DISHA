package com.example.disha.ViewDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.disha.R;
import com.google.android.material.tabs.TabLayout;

public class ViewDetails extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        backBtn = findViewById(R.id.back);
        String[] data = getIntent().getStringArrayExtra("Data");
        viewPagerAdapter = new ViewPagerAdapter(
                getSupportFragmentManager(), data);
        viewPager.setAdapter(viewPagerAdapter);

        // It is used to join TabLayout with ViewPager.
        tabLayout.setupWithViewPager(viewPager);
        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}