package com.example.disha.Main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.disha.R;
import com.example.disha.Main.BottomSheet.CustomBottomSheet;
import com.example.disha.locationModel.Location;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Location location;
    View root;
    private final ArrayList<String> dummyArray = new ArrayList<String>();

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
                    Toast.makeText(getApplicationContext(),status.getStatusMessage(),Toast.LENGTH_LONG).show();
                }
            });
    private Place nashik;

    private CustomBottomSheet sheet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        setContentView(root);

        MainController controller = new MainController(MainActivity.this, root, launcher);
        controller.checkUser();
        sheet = new CustomBottomSheet(root,MainActivity.this);
        controller.init();
        location = new Location(MainActivity.this, R.id.map_fragment);
        location.CheckPrerequisite();
        controller.setSheet(sheet, location);
    }

    @Override
    public void onBackPressed()
    {
        switch(sheet.getState())
        {
            case BottomSheetBehavior.STATE_HIDDEN:
            case BottomSheetBehavior.STATE_COLLAPSED:
                super.onBackPressed();
                break;
            case BottomSheetBehavior.STATE_DRAGGING:
                sheet.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case BottomSheetBehavior.STATE_EXPANDED:
                sheet.setState(BottomSheetBehavior.STATE_HIDDEN);
            default:
                sheet.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
        }
    }
    private void ShowData(Place placeData) {
        sheet.setState(BottomSheetBehavior.STATE_EXPANDED);
        location.RemoveAllMarkers();
        location.AddMarkerToPos(placeData.getLatLng(), false);
        sheet.setPlace(placeData, location);
        sheet.initComponent();
        sheet.setData();
    }
}