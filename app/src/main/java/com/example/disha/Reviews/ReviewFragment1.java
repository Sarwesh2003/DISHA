package com.example.disha.Reviews;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.disha.Main.MainActivity;
import com.example.disha.R;
import com.example.disha.Reviews.dataModel.DAOReview;
import com.example.disha.Reviews.dataModel.Review;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;


public class ReviewFragment1 extends Fragment implements RatingBar.OnRatingBarChangeListener, MaterialPickerOnPositiveButtonClickListener {
    View root;
    TextInputEditText placeName, username, date, description;
    RatingBar ratingBar;
    ImageView emoji;
    ImageButton picker;
    MaterialDatePicker materialDatePicker;
    Button submit;
    TextView title;
    final String[] dateres = {""};
    ArrayList<String> data;
    String name = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_review1, container, false);
        placeName = root.findViewById(R.id.pname);
        username = root.findViewById(R.id.username);
        date = root.findViewById(R.id.datePicker);
        description = root.findViewById(R.id.reviewdesc);
        ratingBar = root.findViewById(R.id.overall);
        emoji = root.findViewById(R.id.emoji);
        picker = root.findViewById(R.id.picker);
        submit = root.findViewById(R.id.submit_review);
        title = getActivity().findViewById(R.id.headline);
        title.setText("Fill Information");
        name = getArguments() != null ? getArguments().getString("PlaceName") : "";
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        placeName.setText(name);
        materialDateBuilder.setTitleText("SELECT A DATE");
        materialDatePicker = materialDateBuilder.build();
        picker.setOnClickListener(v -> {
            materialDatePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");
        });
        submit.setOnClickListener(v -> {
            SubmitData();
        });
        materialDatePicker.addOnPositiveButtonClickListener(this);
        ratingBar.setOnRatingBarChangeListener(this);
        return root;
    }

    private void SubmitData() {
        getData();
        DAOReview dao = new DAOReview();
        Review reviewData = new Review(data.get(0), data.get(1), data.get(2), data.get(3), data.get(4));
        dao.add(reviewData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setMessage("Review added successfully");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                dialog.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setMessage("Something went wrong");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                dialog.show();
            }
        });

    }

    public void getData(){
        data = new ArrayList<>();
        String place = placeName.getText().toString().trim();
        String usernm = username.getText().toString().trim();
        String dt = date.getText().toString().trim();
        String des = description.getText().toString().trim();
        String ratings = String.valueOf(ratingBar.getRating());
        data.add(place);
        data.add(usernm);
        data.add(dt);
        data.add(des);
        data.add(ratings);
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        if(rating <= 1){
            emoji.setImageDrawable(getResources().getDrawable(R.drawable.star1));
        }else if(rating > 1 && rating <= 2){
            emoji.setImageDrawable(getResources().getDrawable(R.drawable.star2));
        }else if(rating > 2 &&rating <= 3){
            emoji.setImageDrawable(getResources().getDrawable(R.drawable.star3));
        }else if(rating > 3 && rating <= 4){
            emoji.setImageDrawable(getResources().getDrawable(R.drawable.star4));
        }else if(rating>4){
            emoji.setImageDrawable(getResources().getDrawable(R.drawable.star5));
        }
    }

    @Override
    public void onPositiveButtonClick(Object selection) {
        dateres[0] = materialDatePicker.getHeaderText();
        date.setText(dateres[0]);
    }
}