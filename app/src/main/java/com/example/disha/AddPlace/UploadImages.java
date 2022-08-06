package com.example.disha.AddPlace;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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


public class UploadImages extends Fragment {


    private static final int PICK_IMAGE_REQUEST = 22;
    View root;
    TextView title, step;
    TextInputEditText ramps, handrails, lifts, wheelchairs, toilets, braille, mainImg;
    ActivityResultLauncher<Intent> rampsRes, handrailRes, brailleRes, liftsRes, toiletRes, wheelchairRes, mainImgRes;
    private Uri rampsFile, handrailFile, toiletFile, brailleFile, wheelchairFile, liftsFile, mainImgFile;
    private ImageButton rampsBtn, handrailsBtn, liftsBtn, brailleBtn, wheelchairsBtn, toiletsBtn, mainImgBtn;
    AppCompatButton submit,prev;
    ArrayList<Uri> list;
    ArrayList<Uri> list1;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainImgRes = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        mainImgFile = result.getData().getData();
                        if(mainImgFile != null){
                            list.add(mainImgFile);
                            mainImg.setText(mainImgFile.toString());
                        }
                    }
                }
        );
        rampsRes = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        rampsFile = result.getData().getData();
                        if(rampsFile != null){
                            list.add(rampsFile);
                            ramps.setText(rampsFile.toString());
                        }
                    }
                }
        );
        handrailRes = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        handrailFile = result.getData().getData();
                        if(handrailFile != null){
                            list.add(handrailFile);
                            handrails.setText(handrailFile.toString());
                        }
                    }
                }
        );
        brailleRes = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        brailleFile = result.getData().getData();
                        if(brailleFile != null){
                            list.add(brailleFile);
                            braille.setText(brailleFile.toString());
                        }
                    }
                }
        );
        liftsRes = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        liftsFile = result.getData().getData();
                        if(liftsFile != null){
                            list.add(liftsFile);
                            lifts.setText(liftsFile.toString());
                        }
                    }
                }
        );
        wheelchairRes = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        wheelchairFile = result.getData().getData();
                        if(wheelchairFile != null){
                            list.add(wheelchairFile);
                            wheelchairs.setText(wheelchairFile.toString());
                        }
                    }
                }
        );

        toiletRes = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        toiletFile = result.getData().getData();
                        if(toiletFile != null){
                            list.add(toiletFile);
                            toilets.setText(toiletFile.toString());
                        }
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
        ramps = root.findViewById(R.id.rampsImg);
        title = getActivity().findViewById(R.id.tit);
        handrails = root.findViewById(R.id.handrailsImg);
        lifts = root.findViewById(R.id.liftsImg);
        wheelchairs = root.findViewById(R.id.wheelchairImg);
        toilets = root.findViewById(R.id.toiletsImg);
        braille = root.findViewById(R.id.brailleImg);
        submit = getActivity().findViewById(R.id.continueBtn);
        rampsBtn = root.findViewById(R.id.rampsbtn);
        handrailsBtn = root.findViewById(R.id.handrailbtn);
        liftsBtn = root.findViewById(R.id.liftsbtn);
        wheelchairsBtn = root.findViewById(R.id.wheelchairbtn);
        toiletsBtn = root.findViewById(R.id.toiletbtn);
        brailleBtn = root.findViewById(R.id.braillebtn);
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

        rampsBtn.setOnClickListener(v -> {
            openIntent(rampsRes);
        });

        handrailsBtn.setOnClickListener(v -> {
            openIntent(handrailRes);

        });
        liftsBtn.setOnClickListener(v -> {
            openIntent(liftsRes);

        });
        wheelchairsBtn.setOnClickListener(v -> {
            openIntent(wheelchairRes);

        });
        toiletsBtn.setOnClickListener(v -> {
            openIntent(toiletRes);

        });

        brailleBtn.setOnClickListener(v -> {
            openIntent(brailleRes);

        });

        prev.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        Bundle dataBundle = this.getArguments();
        ArrayList<String> data = dataBundle.getStringArrayList("data");
        list1= new ArrayList<>();
        submit.setOnClickListener(v -> {
            ProgressDialog progressDialog
                    = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading Data...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            int ctr=0;
            DAOPlaceData dao = new DAOPlaceData();

        PlaceData placeData = new PlaceData(data.get(0), data.get(1), data.get(2), data.get(3), data.get(4), data.get(5),
                data.get(6), data.get(7), data.get(8), data.get(9), data.get(10), data.get(11), data.get(12), data.get(13)
        ,data.get(14),data.get(15));
        dao.add(placeData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.setTitle("Uploading Images");
            }
        });

            for(Uri filepath : list){
                ctr++;
                int finalCtr = ctr;
                dao.addImg(filepath, data.get(0)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
        Toast.makeText(getContext(), String.valueOf(list1.size()), Toast.LENGTH_SHORT).show();
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
        intent.setAction(Intent.ACTION_GET_CONTENT);
        res.launch(intent);
    }
}