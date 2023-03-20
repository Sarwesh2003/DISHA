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

public class Handrail extends Fragment {

    AutoCompleteTextView handrail;
    ImageButton btn_img;
    TextInputEditText desc_handrail, handrailImg;
    AppCompatButton prev, submit;
    TextView step, title;
    LinearLayout handrailImgLayout;
    ActivityResultLauncher<Intent> handrailRes;
    private Uri handrailFile;
    View root;
    ArrayList<Uri> list = new ArrayList<>();
    public Handrail() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        handrailRes = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        handrailFile = result.getData().getData();
                        if(handrailFile != null){
                            list.add(handrailFile);
                            handrailImg.setText(handrailFile.toString());
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         root = inflater.inflate(R.layout.fragment_handrail, container, false);
        handrail = root.findViewById(R.id.handrail);
        btn_img = root.findViewById(R.id.handrailbtn);
        desc_handrail = root.findViewById(R.id.desc_facility_handrail);
        handrailImg = root.findViewById(R.id.handrailsImg);
        handrailImgLayout = root.findViewById(R.id.handrailImgLayout);
        prev = getActivity().findViewById(R.id.prev);
        submit = getActivity().findViewById(R.id.continueBtn);
        step = getActivity().findViewById(R.id.step);
        title = getActivity().findViewById(R.id.tit);
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
            handrailRes.launch(intent);
        });
        return root;
    }
    public void initializeAdapters(){
        step.setText("Step: 03");
        title.setText("Facilities Available");
        String[] facility=getResources().getStringArray(R.array.facilities);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(),R.layout.dropdown_btype,facility);
        handrail.setAdapter(adapter);
        handrail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(facility[position].equals("Available")){
                    handrailImgLayout.setVisibility(View.VISIBLE);
                }else{
                    handrailImg.setText("");
                    handrailImgLayout.setVisibility(View.GONE);
                }
            }
        });
    }
    private void sendData(){
        if(handrail.getText().toString().isEmpty() || desc_handrail.getText().toString().isEmpty()){
            TextView warn = root.findViewById(R.id.warn);
            warn.setText("* All fields are mandatory.");
            return;
        }
        Bundle data_bundle = this.getArguments();
        if (data_bundle != null) {
            data_bundle.putString("handrail", handrail.getText().toString());
            data_bundle.putString("handrailDescription", desc_handrail.getText().toString());
        }
        if(list.size() > 0)
            data_bundle.putString("HandrailImg", list.get(0).toString());
        else
            data_bundle.putString("HandrailImg", null);

        FragmentManager fragmentManager = this.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down);
        fragmentTransaction.addToBackStack(null);
//        UploadImages uploadImages = new UploadImages();
//        uploadImages.setArguments(b);
        Braille braille = new Braille();
        braille.setArguments(data_bundle);
        fragmentTransaction.replace(R.id.fragment_form_container, braille).commit();
    }
}