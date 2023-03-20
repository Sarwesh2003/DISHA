package com.example.disha.AddPlace;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.disha.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.List;

public class PlaceDetails extends Fragment {
    AutoCompleteTextView btype, infratype;
    TextInputEditText placeName, desc, pno;
    AppCompatButton cont, prev;
    TextView title, step;
    ImageButton btn;
    Bundle data;
    public ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if(result.getData()!=null){
                        Place place = Autocomplete.getPlaceFromIntent(result.getData());
                        ShowData(place);
                    }
                }else if(result.getResultCode() == AutocompleteActivity.RESULT_ERROR){
                    Status status = Autocomplete.getStatusFromIntent(result.getData());
                    Toast.makeText(getContext(),status.getStatusMessage(),Toast.LENGTH_LONG).show();
                }
            });

    private void ShowData(Place place) {
        placeName.setText(place.getName());
        data.putString("Lat", String.valueOf(place.getLatLng().latitude));
        data.putString("Lang", String.valueOf(place.getLatLng().longitude));
        data.putString("Address", place.getAddress());
    }

    public PlaceDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_place_details, container, false);
        data = new Bundle();
        btype = (AutoCompleteTextView) v.findViewById(R.id.btype);
        infratype = (AutoCompleteTextView) v.findViewById(R.id.infraType);
        cont = getActivity().findViewById(R.id.continueBtn);
        prev = getActivity().findViewById(R.id.prev);
        step = getActivity().findViewById(R.id.step);
        placeName = v.findViewById(R.id.pname);
        desc = v.findViewById(R.id.bdes);
        pno = v.findViewById(R.id.phno);
        btn = v.findViewById(R.id.addPlace);
        title = getActivity().findViewById(R.id.tit);
        initializeAdapters();

        cont.setOnClickListener(l -> {
            if(placeName.getText().toString().isEmpty() || desc.getText().toString().isEmpty() ||
            pno.getText().toString().isEmpty() || btype.getText().toString().isEmpty() || infratype.getText().toString().isEmpty()){
                TextView warn = v.findViewById(R.id.warn);
                warn.setText("* All fields are mandatory.");
                return;
            }

            data.putString("PlaceName",placeName.getText().toString());
            data.putString("Desc",desc.getText().toString());
            data.putString("PhNo",pno.getText().toString());
            data.putString("PType",btype.getText().toString());
            data.putString("InfraType",infratype.getText().toString());


            FragmentManager fragmentManager = this.getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down);
            fragmentTransaction.addToBackStack(null);
            Ramp q1 = new Ramp();
            q1.setArguments(data);
            fragmentTransaction.replace(R.id.fragment_form_container,q1).commit();

            prev.setEnabled(true);

        });
        placeName.setEnabled(false);
        btn.setOnClickListener(t -> {
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG,
                    Place.Field.NAME, Place.Field.ID);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                    .build(getActivity());
            launcher.launch(intent);
        });
        return v;
    }

    public void initializeAdapters(){
        step.setText("Step: 01");
        title.setText("Fill in the basic information");
        String[] business=getResources().getStringArray(R.array.bussiness_type);
        String[] infrastructure=getResources().getStringArray(R.array.infrastructure_type);
        ArrayAdapter<String> badapter=new ArrayAdapter<>(getContext(),R.layout.dropdown_btype,business);
        ArrayAdapter<String> iadapter=new ArrayAdapter<>(getContext(),R.layout.dropdown_btype,infrastructure);
        btype.setAdapter(badapter);
        infratype.setAdapter(iadapter);

    }
}