package com.example.disha.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;

import com.example.disha.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class Settings extends AppCompatActivity {
    SwitchMaterial audioSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        audioSwitch = findViewById(R.id.voice_switch);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        SharedPreferences settings = getSharedPreferences("Settings", 0);
        boolean silent = settings.getBoolean("audio", true);
        audioSwitch.setChecked(silent);
        SharedPreferences.Editor editor = settings.edit();

        audioSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("audio", isChecked);
                editor.commit();
            }
        });

    }
}