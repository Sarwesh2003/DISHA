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

public class Braille extends Fragment {

    AutoCompleteTextView braille;
    ImageButton btn_img;
    TextInputEditText desc_braille, brailleImg;
    AppCompatButton prev, submit;
    TextView step, title;
    LinearLayout brailleImgLayout;
    ActivityResultLauncher<Intent> brailleRes;
    private Uri brailleFile;
    View root;
    ArrayList<Uri> list = new ArrayList<>();
    public Braille() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        brailleRes = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        brailleFile = result.getData().getData();
                        if(brailleFile != null){
                            list.add(brailleFile);
                            brailleImg.setText(brailleFile.toString());
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_braille, container, false);

        braille = root.findViewById(R.id.braille);
        btn_img = root.findViewById(R.id.braillebtn);
        desc_braille = root.findViewById(R.id.desc_braille);
        brailleImgLayout = root.findViewById(R.id.brailleImgLayout);
        brailleImg = root.findViewById(R.id.brailleImg);
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
            brailleRes.launch(intent);
        });
        return root;
    }
    public void initializeAdapters(){
        step.setText("Step: 03");
        title.setText("Facilities Available");
        String[] facility=getResources().getStringArray(R.array.facilities);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(),R.layout.dropdown_btype,facility);
        braille.setAdapter(adapter);
        braille.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(facility[position].equals("Available")){
                    brailleImgLayout.setVisibility(View.VISIBLE);
                }else{
                    brailleImgLayout.setVisibility(View.GONE);
                }
            }
        });
    }
    private void sendData(){
        if(braille.getText().toString().isEmpty() || desc_braille.getText().toString().isEmpty()){
            TextView warn = root.findViewById(R.id.warn);
            warn.setText("* All fields are mandatory.");
            return;
        }
        Bundle data_bundle = this.getArguments();
        if (data_bundle != null) {
            data_bundle.putString("braille", braille.getText().toString());
            data_bundle.putString("brailleDescription", desc_braille.getText().toString());
        }
        if(list.size() > 0)
            data_bundle.putString("brailleImg", list.get(0).toString());
        else
            data_bundle.putString("brailleImg", null);
        FragmentManager fragmentManager = this.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down);
        fragmentTransaction.addToBackStack(null);

        Toilet toilet = new Toilet();
        toilet.setArguments(data_bundle);
        fragmentTransaction.replace(R.id.fragment_form_container, toilet).commit();
    }
}