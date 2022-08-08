package com.example.disha.AddPlace;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.disha.Main.MainActivity;
import com.example.disha.R;
import com.example.disha.AddPlace.data.DAOPlaceData;
import com.example.disha.AddPlace.data.PlaceData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;


public class UploadImages extends Fragment {


    private static final int PICK_IMAGE_REQUEST = 22;
    View root;
    TextView title, step;
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //provisions of ramps, handrails, accessible toilets, Braille signage, accessible counters, lifts, wheelchairs etc.
        root = inflater.inflate(R.layout.fragment_upload_images, container, false);
        title = getActivity().findViewById(R.id.tit);
        submit = getActivity().findViewById(R.id.continueBtn);
        mainImgBtn = root.findViewById(R.id.mainImgbtn);
        mainImg = root.findViewById(R.id.mainImg);
        step = getActivity().findViewById(R.id.step);
        prev = getActivity().findViewById(R.id.prev);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
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
            DAOPlaceData dao = new DAOPlaceData();
            PlaceData placeData = getPlaceData(dataBundle);
            dao.add(placeData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    progressDialog.setTitle("Uploading Images");
                }
            });
            if(list1.size() > 0){
                if(list1.containsKey("RampsImg")){
                    ctr++;
                    progressDialog.setTitle("Uploading Image" + ctr);
                    dao.addImg(list1.get("RampsImg"), placeData.getPlaceName(), "Facilities", "Ramp")
                    .addOnSuccessListener(taskSnapshot -> {});
                }
                if(list1.containsKey("HandrailImg")){
                    ctr++;
                    progressDialog.setTitle("Uploading Image" + ctr);
                    dao.addImg(list1.get("HandrailImg"), placeData.getPlaceName(), "Facilities", "Handrail")
                            .addOnSuccessListener(taskSnapshot -> {
                            });
                }
                if(list1.containsKey("brailleImg")){
                    ctr++;
                    progressDialog.setTitle("Uploading Image" + ctr);
                    dao.addImg(list1.get("brailleImg"), placeData.getPlaceName(), "Facilities", "Braille")
                            .addOnSuccessListener(taskSnapshot -> {

                            });
                }
                if(list1.containsKey("toiletImg")){
                    ctr++;
                    progressDialog.setTitle("Uploading Image" + ctr);
                    dao.addImg(list1.get("toiletImg"), placeData.getPlaceName(), "Facilities", "Toilet")
                            .addOnSuccessListener(taskSnapshot -> {

                            });
                }
                if(list1.containsKey("liftsImg")){
                    ctr++;
                    progressDialog.setTitle("Uploading Image" + ctr);
                    dao.addImg(list1.get("liftsImg"), placeData.getPlaceName(), "Facilities", "Lifts")
                            .addOnSuccessListener(taskSnapshot -> {
                            });
                }
                if(list1.containsKey("wheelchairImg")){
                    ctr++;
                    progressDialog.setTitle("Uploading Image" + ctr);
                    dao.addImg(list1.get("wheelchairImg"), placeData.getPlaceName(), "Facilities", "wheelchair")
                            .addOnSuccessListener(taskSnapshot -> {

                            });
                }
            }
            progressDialog.setTitle("Uploading Display Images");
            ctr = 0;
            for(Uri filepath : list){
                ctr++;
                int finalCtr = ctr;
                dao.addImg(filepath, placeData.getPlaceName(), "Display", "Disp"+ctr).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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

    private void Initallize() {
        step.setText("Step: 04");
        title.setText("Upload Images");
        prev.setEnabled(true);
    }

    private void taskCompleted(boolean success){
        String msg = "Place Added Successfully";
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
    private void openIntent(ActivityResultLauncher<Intent> res){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        res.launch(intent);
    }
    private PlaceData getPlaceData(Bundle dataBundle) {
        PlaceData placeData = new PlaceData();
        placeData.setPlaceName(dataBundle.get("PlaceName").toString());
        placeData.setPlaceType(dataBundle.get("PType"). toString());
        placeData.setPlaceDescription(dataBundle.get("Desc").toString());
        placeData.setPhoneNo(dataBundle.get("PhNo").toString());
        placeData.setInfrastructureType(dataBundle.get("InfraType").toString());
        placeData.setLat(dataBundle.get("Lat").toString());
        placeData.setLang(dataBundle.get("Lang").toString());
        placeData.setAddress(dataBundle.get("Address").toString());
        placeData.setRamp(dataBundle.get("ramp").toString());
        placeData.setRampDescription(dataBundle.get("rampDescription").toString());
        placeData.setHandrail(dataBundle.get("handrail").toString());
        placeData.setHandrailDescription(dataBundle.get("handrailDescription").toString());
        placeData.setBraille(dataBundle.get("braille").toString());
        placeData.setBrailleDescription(dataBundle.get("brailleDescription").toString());
        placeData.setToilet(dataBundle.get("toilet").toString());
        placeData.setToiletDescription(dataBundle.get("toiletDescription").toString());
        placeData.setNo_of_toilet(dataBundle.get("toilet_no").toString());
        placeData.setLifts(dataBundle.get("lifts").toString());
        placeData.setLiftsDescription(dataBundle.get("liftsDescription").toString());
        placeData.setWheelchair(dataBundle.get("wheelchair").toString());
        placeData.setWheelchairDescription(dataBundle.get("wheelchairDescription").toString());

//        Adding Images to list
        if(dataBundle.get("RampsImg") != null)
            list1.put("RampsImg", Uri.parse(dataBundle.get("RampsImg").toString()));
        if(dataBundle.get("HandrailImg") != null)
            list1.put("HandrailImg",Uri.parse(dataBundle.get("HandrailImg").toString()));
        if(dataBundle.get("brailleImg") != null)
            list1.put("brailleImg", Uri.parse(dataBundle.get("brailleImg").toString()));
        if(dataBundle.get("toiletImg") != null)
            list1.put("toiletImg", Uri.parse(dataBundle.get("toiletImg").toString()));
        if(dataBundle.get("liftsImg") != null)
            list1.put("liftsImg", Uri.parse(dataBundle.get("liftsImg").toString()));
        if(dataBundle.get("wheelchairImg") != null)
            list1.put("wheelchairImg", Uri.parse(dataBundle.get("wheelchairImg").toString()));
        return placeData;
    }
}