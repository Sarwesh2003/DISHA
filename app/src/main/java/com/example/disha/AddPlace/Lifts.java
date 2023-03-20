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

public class Lifts extends Fragment {
    AutoCompleteTextView lift;
    ImageButton btn_img;
    TextInputEditText desc_lift, liftsImg;
    AppCompatButton prev, submit;
    TextView step, title;
    LinearLayout liftsImgLayout;
    ActivityResultLauncher<Intent> liftsRes;
    private Uri liftsFile;
    ArrayList<Uri> list = new ArrayList<>();
    View root;
    public Lifts() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        liftsRes = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        liftsFile = result.getData().getData();
                        if(liftsFile != null){
                            list.add(liftsFile);
                            liftsImg.setText(liftsFile.toString());
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_lifts, container, false);
        lift = root.findViewById(R.id.lifts);
        btn_img = root.findViewById(R.id.liftsbtn);
        desc_lift = root.findViewById(R.id.descLifts);
        prev = getActivity().findViewById(R.id.prev);
        submit = getActivity().findViewById(R.id.continueBtn);
        step = getActivity().findViewById(R.id.step);
        title = getActivity().findViewById(R.id.tit);
        liftsImg = root.findViewById(R.id.liftsImg);
        liftsImgLayout = root.findViewById(R.id.liftsImgLayout);
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
            liftsRes.launch(intent);
        });
        return root;
    }
    public void initializeAdapters(){
        step.setText("Step: 03");
        title.setText("Facilities Available");
        String[] facility_lift=getResources().getStringArray(R.array.facility_lift);
        ArrayAdapter<String> adapterLift=new ArrayAdapter<>(getContext(),R.layout.dropdown_btype,facility_lift);
        lift.setAdapter(adapterLift);
        lift.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(facility_lift[position].equals("Available")){
                    liftsImgLayout.setVisibility(View.VISIBLE);
                }else{
                    liftsImg.setText("");
                    liftsImgLayout.setVisibility(View.GONE);
                }
            }
        });
    }
    private void sendData(){
        if(lift.getText().toString().isEmpty() || desc_lift.getText().toString().isEmpty()){
            TextView warn = root.findViewById(R.id.warn);
            warn.setText("* All fields are mandatory.");
            return;
        }
        Bundle data_bundle = this.getArguments();
        if (data_bundle != null) {
            data_bundle.putString("lifts", lift.getText().toString());
            data_bundle.putString("liftsDescription", desc_lift.getText().toString());
        }
        if(list.size() > 0 )
            data_bundle.putString("liftsImg", list.get(0).toString());
        else
            data_bundle.putString("liftsImg", null);
        FragmentManager fragmentManager = this.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down);
        fragmentTransaction.addToBackStack(null);
        Wheelchair wheelchair = new Wheelchair();
        wheelchair.setArguments(data_bundle);
        fragmentTransaction.replace(R.id.fragment_form_container, wheelchair).commit();
    }
}