package com.example.disha.Reviews;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.disha.AddPlace.data.DAOPlaceData;
import com.example.disha.AddPlace.data.PlaceData;
import com.example.disha.Main.MainActivity;
import com.example.disha.R;
import com.example.disha.Reviews.dataModel.DAOReview;
import com.example.disha.Reviews.dataModel.Review;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class ReviewUploadImages extends Fragment {
    TextInputEditText mainImg;
    ActivityResultLauncher<Intent> mainImgRes;
    private Uri mainImgFile;
    private ImageButton mainImgBtn;
    AppCompatButton submit,prev;
    ArrayList<Uri> list;
    HashMap<String,Uri> list1;
    int position = 0;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainImgRes = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getData() != null){
                        Intent data = result.getData();
                        if (result.getData().getClipData() != null) {
                            ClipData mClipData = data.getClipData();
                            int cout = mClipData.getItemCount();
                            for (int i = 0; i < cout; i++) {
                                // adding imageuri in array
                                Uri imageurl = mClipData.getItemAt(i).getUri();
                                list.add(imageurl);
                                mainImg.setText("Images Selected: "+list.size());
                            }
                            position = 0;
                        } else {
                            Uri imageurl = data.getData();
                            list.add(imageurl);
                            position = 0;
                            mainImg.setText("Images Selected: "+list.size());
                        }
                    }else{
                        Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_review_upload_images, container, false);
        submit = getActivity().findViewById(R.id.continueBtn);
        mainImgBtn = root.findViewById(R.id.mainImgbtn);
        mainImg = root.findViewById(R.id.mainImg);
        prev = getActivity().findViewById(R.id.prev);
        list = new ArrayList<>();
        Initallize();

        mainImgBtn.setOnClickListener(v -> {
            openIntent(mainImgRes);
        });
        Bundle dataBundle = this.getArguments();
        list1= new HashMap<>();
        submit.setOnClickListener(v -> {
            ProgressDialog progressDialog
                    = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading Data...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            int ctr=0;
            DAOReview dao = new DAOReview();
            Review placeData = getPlaceData(dataBundle);
            dao.add(placeData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    progressDialog.setTitle("Uploading Images");
                }
            });

            progressDialog.setTitle("Uploading Display Images");
            ctr = 0;
            for(Uri filepath : list){
                ctr++;
                int finalCtr = ctr;
                dao.addImg(filepath, placeData.getPlaceName(), "Display", "Review"+ctr).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.setTitle("Uploading Image "+ finalCtr);
                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (finalCtr == list.size()){
                            progressDialog.dismiss();
                            taskCompleted(true);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        taskCompleted(false);
                    }
                });
            }
        });
        return root;
    }
    private void openIntent(ActivityResultLauncher<Intent> res){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        res.launch(intent);
    }
    private void Initallize() {
        prev.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        prev.setEnabled(true);
    }
    private void taskCompleted(boolean success){
        String msg = "Review Added Successfully";
        if(!success){
            msg = "Something Went Wrong. Please check you internet connection";
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setMessage(msg);
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
    private Review getPlaceData(Bundle dataBundle) {
        Review review = new Review();
        review.setPlaceName(dataBundle.get("PlaceName").toString());
        review.setUsername(dataBundle.get("UserName").toString());
        review.setDescription(dataBundle.get("ReviewDescription").toString());
        review.setDate(dataBundle.get("Date").toString());
        review.setRatings(dataBundle.get("Ratings").toString());
        review.setRamp(dataBundle.get("ramp").toString());
        review.setRampDescription(dataBundle.get("rampDescription").toString());
        review.setHandrail(dataBundle.get("handrail").toString());
        review.setHandrailDescription(dataBundle.get("handrailDescription").toString());
        review.setBraille(dataBundle.get("braille").toString());
        review.setBrailleDescription(dataBundle.get("brailleDescription").toString());
        review.setToilet(dataBundle.get("toilet").toString());
        review.setToiletDescription(dataBundle.get("toiletDescription").toString());
        review.setNtoilet(dataBundle.get("toilet_no").toString());
        review.setLifts(dataBundle.get("lifts").toString());
        review.setLiftsDescription(dataBundle.get("liftsDescription").toString());
        review.setWheelchair(dataBundle.get("wheelchair").toString());
        review.setWheelchairDescription(dataBundle.get("wheelchairDescription").toString());
        return review;
    }
}