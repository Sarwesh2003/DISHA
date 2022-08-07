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
import android.widget.Toast;

import com.example.disha.R;
import com.example.disha.AddPlace.data.DAOPlaceData;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;


public class Ramp extends Fragment {

   View root;
   TextView title, step;
   AutoCompleteTextView ramp;
   TextInputEditText  imgUri, desc;
   AppCompatButton prev, submit;
   LinearLayout rampImgLayout;
   ImageButton imgbtn;
   ActivityResultLauncher<Intent> rampsRes;
   private Uri rampsFile;
   ArrayList<Uri> list = new ArrayList<>();
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        rampsRes = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        rampsFile = result.getData().getData();
                        if(rampsFile != null){
                            list.add(rampsFile);
                            imgUri.setText(rampsFile.toString());
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root =  inflater.inflate(R.layout.fragment_ramp, container, false);
        title = getActivity().findViewById(R.id.tit);
        ramp = root.findViewById(R.id.ramps);
        desc = root.findViewById(R.id.descRamp);
        prev = getActivity().findViewById(R.id.prev);
        submit = getActivity().findViewById(R.id.continueBtn);
        step = getActivity().findViewById(R.id.step);
        rampImgLayout = root.findViewById(R.id.rampImgLayout);
        imgbtn = root.findViewById(R.id.rampsbtn);
        imgUri = root.findViewById(R.id.rampsImg);
        initializeAdapters();
        prev.setOnClickListener(v->{
            getActivity().onBackPressed();
        });

        submit.setOnClickListener(v -> {
            sendData();
        });

        imgbtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            rampsRes.launch(intent);
        });
        return root;
    }

    private void sendData() {
        Bundle data_bundle = this.getArguments();
        if (data_bundle != null) {
            data_bundle.putString("ramp", ramp.getText().toString());
            data_bundle.putString("rampDescription", desc.getText().toString());
        }
        if(list.size() > 0)
            data_bundle.putString("RampsImg", list.get(0).toString());
        else
            data_bundle.putString("RampsImg", null);
        FragmentManager fragmentManager = this.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down);
        fragmentTransaction.addToBackStack(null);
        Handrail frg_handrail = new Handrail();
        frg_handrail.setArguments(data_bundle);
        fragmentTransaction.replace(R.id.fragment_form_container, frg_handrail).commit();
    }

    public void initializeAdapters(){
        step.setText("Step: 03");
        title.setText("Facilities Available");
        String[] facility=getResources().getStringArray(R.array.facilities);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(),R.layout.dropdown_btype,facility);
        ramp.setAdapter(adapter);

        ramp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(facility[position].equals("Available")){
                    rampImgLayout.setVisibility(View.VISIBLE);
                }else{
                    imgUri.setText("");
                    rampImgLayout.setVisibility(View.GONE);
                }
            }
        });
    }
}