package com.example.disha.ViewDetails;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.disha.R;

public class ImageFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_image, container, false);
        Bundle b = this.getArguments();
        ImageView img = root.findViewById(R.id.showImage);
        Uri photo = Uri.parse(b.get("Photo").toString());
        Glide.with(root.getContext()).load(photo).into(img);
        return root;
    }
}