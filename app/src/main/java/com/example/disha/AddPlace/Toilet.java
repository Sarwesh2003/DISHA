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

public class Toilet extends Fragment {
    AutoCompleteTextView toilet;
    ImageButton btn_img;
    TextInputEditText desc_toilet, ntoilet, toiletImg;
    AppCompatButton prev, submit;
    TextView title, step;
    LinearLayout toiletImgLayout;
    ActivityResultLauncher<Intent> toiletRes;
    private Uri toiletFile;
    View root;
    ArrayList<Uri> list = new ArrayList<>();
    public Toilet() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        toiletRes = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        toiletFile = result.getData().getData();
                        if(toiletFile != null){
                            list.add(toiletFile);
                            toiletImg.setText(toiletFile.toString());
                        }
                    }
                }
        );
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_toilet, container, false);
        toilet = root.findViewById(R.id.toilet);
        btn_img = root.findViewById(R.id.toiletbtn);
        desc_toilet = root.findViewById(R.id.descToilet);
        ntoilet = root.findViewById(R.id.toiletsNo);
        prev = getActivity().findViewById(R.id.prev);
        submit = getActivity().findViewById(R.id.continueBtn);
        step = getActivity().findViewById(R.id.step);
        title = getActivity().findViewById(R.id.tit);
        toiletImgLayout = root.findViewById(R.id.toiletImgLayout);
        toiletImg = root.findViewById(R.id.toiletsImg);
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
            toiletRes.launch(intent);
        });
        return root;
    }
    public void initializeAdapters(){
        step.setText("Step: 03");
        title.setText("Facilities Available");
        String[] facility=getResources().getStringArray(R.array.facilities);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(),R.layout.dropdown_btype,facility);
        toilet.setAdapter(adapter);
        toilet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(facility[position].equals("Available")){
                    toiletImgLayout.setVisibility(View.VISIBLE);
                }else{
                    toiletImgLayout.setVisibility(View.GONE);
                }
            }
        });
    }
    private void sendData(){
        if(toilet.getText().toString().isEmpty() || desc_toilet.getText().toString().isEmpty()){
            TextView warn = root.findViewById(R.id.warn);
            warn.setText("* All fields are mandatory.");
            return;
        }
        Bundle data_bundle = this.getArguments();
        if (data_bundle != null) {
            data_bundle.putString("toilet", toilet.getText().toString());
            data_bundle.putString("toiletDescription", desc_toilet.getText().toString());
            data_bundle.putString("toilet_no", ntoilet.getText().toString());
        }
        if(list.size() > 0)
            data_bundle.putString("toiletImg", list.get(0).toString());
        else
            data_bundle.putString("toiletImg", null);
        FragmentManager fragmentManager = this.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down);
        fragmentTransaction.addToBackStack(null);
        Lifts lifts = new Lifts();
        lifts.setArguments(data_bundle);
        fragmentTransaction.replace(R.id.fragment_form_container, lifts).commit();
    }
}