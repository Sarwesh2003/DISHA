package com.example.disha.AddPlace;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.disha.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;


public class Wheelchair extends Fragment {
    AutoCompleteTextView wheelchair;
    ImageButton btn_img;
    TextInputEditText desc_wheelchair, wheelchairImg;
    AppCompatButton prev, submit;
    TextView step, title;
    LinearLayout wheelchairImgLayout;
    View root;
    ActivityResultLauncher<Intent> wheelchairRes;
    private Uri wheelchairFile;
    ArrayList<Uri> list = new ArrayList<>();
    public Wheelchair() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        wheelchairRes = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        wheelchairFile = result.getData().getData();
                        if(wheelchairFile != null){
                            list.add(wheelchairFile);
                            wheelchairImg.setText(wheelchairFile.toString());
                        }
                    }
                }
        );
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         root = inflater.inflate(R.layout.fragment_wheelchair, container, false);
        wheelchair = root.findViewById(R.id.wheelchair);
        btn_img = root.findViewById(R.id.wheelchairbtn);
        desc_wheelchair = root.findViewById(R.id.descWheelchair);
        prev = getActivity().findViewById(R.id.prev);
        submit = getActivity().findViewById(R.id.continueBtn);
        step = getActivity().findViewById(R.id.step);
        title = getActivity().findViewById(R.id.tit);
        wheelchairImg = root.findViewById(R.id.wheelchairImg);
        wheelchairImgLayout = root.findViewById(R.id.wheelchairImgLayout);
        initializeAdapters();
        prev.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        submit.setOnClickListener(v -> {
            sendData();
        });

        btn_img.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            wheelchairRes.launch(intent);
        });
        return root;
    }

    private void sendData(){
        if(wheelchair.getText().toString().isEmpty() || desc_wheelchair.getText().toString().isEmpty()){
            TextView warn = root.findViewById(R.id.warn);
            warn.setText("* All fields are mandatory.");
            return;
        }
        Bundle data_bundle = this.getArguments();
        if (data_bundle != null) {
            data_bundle.putString("wheelchair", wheelchair.getText().toString());
            data_bundle.putString("wheelchairDescription", desc_wheelchair.getText().toString());
        }
        if(list.size() > 0)
            data_bundle.putString("wheelchairImg", list.get(0).toString());
        else
            data_bundle.putString("wheelchairImg", null);


        FragmentManager fragmentManager = this.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down);
        fragmentTransaction.addToBackStack(null);
        UploadImages uploadImages = new UploadImages();
        uploadImages.setArguments(data_bundle);
        fragmentTransaction.replace(R.id.fragment_form_container,uploadImages).commit();
    }

    public void initializeAdapters(){
        step.setText("Step: 03");
        title.setText("Facilities Available");
        String[] facility=getResources().getStringArray(R.array.facilities);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(),R.layout.dropdown_btype,facility);
        wheelchair.setAdapter(adapter);
        wheelchair.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(facility[position].equals("Available")){
                    wheelchairImgLayout.setVisibility(View.VISIBLE);
                }else{
                    wheelchairImg.setText("");
                    wheelchairImgLayout.setVisibility(View.GONE);
                }
            }
        });
    }
}