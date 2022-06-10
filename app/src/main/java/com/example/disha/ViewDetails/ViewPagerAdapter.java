package com.example.disha.ViewDetails;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    String[] data;
    public ViewPagerAdapter(@NonNull FragmentManager fm, String[] data) {
        super(fm);
        this.data = data;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle b = new Bundle();
        if (position == 0){
            fragment = new FragmentDetails(data);
        }
        else if (position == 1){
            fragment = new FragmentReview(data);
        }


        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
            title = "Place Details";
        else if (position == 1)
            title = "PLace Reviews";
        return title;
    }
}
