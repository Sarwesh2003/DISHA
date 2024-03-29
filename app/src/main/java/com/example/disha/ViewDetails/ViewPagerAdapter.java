package com.example.disha.ViewDetails;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.disha.AddPlace.data.PlaceData;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    PlaceData data;
    Context context;
    public ViewPagerAdapter(@NonNull FragmentManager fm, PlaceData data, Context context) {
        super(fm);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0){
            fragment = new FragmentDetails(data);
        }
        else if (position == 1){
            fragment = new FragmentReview(data);
        }else{
            fragment = new PlacePhotos(data);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
            title = "Details";
        else if (position == 1)
            title = "Reviews";
        else
            title = "Photos";
        return title;
    }
}
