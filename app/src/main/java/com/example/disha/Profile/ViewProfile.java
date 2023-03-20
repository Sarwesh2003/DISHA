package com.example.disha.Profile;

import static com.example.disha.Profile.CreateProfile.drawableToBitmap;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.disha.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfile extends AppCompatActivity {
    Toolbar toolbar;
    ImageView showQR;
    TextInputEditText usernm, email, sos, aadhar, description, disabilityType;
    CircleImageView img;
    private FirebaseUser user;
    private Bitmap profileImg;
    String myUri = "";
    private Uri filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        toolbar = findViewById(R.id.toolbar);
        usernm = findViewById(R.id.usersName);
        email = findViewById(R.id.usersEmail);
        sos = findViewById(R.id.sos);
        aadhar = findViewById(R.id.aadhar);
        description = findViewById(R.id.disabilityDescription);
        disabilityType = findViewById(R.id.typeDisability);
        user = FirebaseAuth.getInstance().getCurrentUser();
        img = findViewById(R.id.profilepic);
        TextView user_name = findViewById(R.id.user_name);
        TextView gmail = findViewById(R.id.gmail);
        profileImg = drawableToBitmap(getDrawable(R.drawable.ic_profile));
        showQR = findViewById(R.id.qr);

        if (user != null) {
            String name = user.getDisplayName();
            String uemail = user.getEmail();
            if (name == null || name.isEmpty()) {
                user_name.setText(user.getPhoneNumber());
                usernm.setText(user.getPhoneNumber());
            } else {
                user_name.setText(name);
                usernm.setText(name);
            }

            if (uemail == null || uemail.isEmpty()) {
                gmail.setText("Gmail not found");
                email.setText("Gmail not found");
            } else {
                gmail.setText(user.getEmail());
                email.setText(user.getEmail());
            }
//            img.setImageBitmap(profileImg);
            DAOProfile dao = new DAOProfile();
            dao.getReference().child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists() && snapshot.getChildrenCount() > 0){
                        String imgUri = snapshot.child("image").getValue(String.class);
                        Picasso.get().load(imgUri).into(img);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        PopulateData();
        showQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = getDataString();
                createQR(str);
            }
        });
    }

    public void PopulateData(){
        DAOProfile dao = new DAOProfile();
        ProfileData data = new ProfileData();
        dao.getReference().child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount() > 0){
//                    Toast.makeText(ViewProfile.this, "Found", Toast.LENGTH_SHORT).show();
                    HashMap<String, Object> map = (HashMap<String, Object>)snapshot.getValue();
                    data.setAadharNo(Objects.requireNonNull(map.getOrDefault("aadharNo", "")).toString());
                    data.setDisabilityDescription(Objects.requireNonNull(map.getOrDefault("disabilityDescription", "")).toString());
                    data.setDisabilityType(Objects.requireNonNull(map.getOrDefault("disabilityType", "")).toString());
                    data.setEmergencyContact(Objects.requireNonNull(map.getOrDefault("emergencyContact", "")).toString());
                    aadhar.setText(data.getAadharNo());
                    disabilityType.setText(data.getDisabilityType());
                    description.setText(data.getDisabilityDescription());
                    sos.setText(data.getEmergencyContact());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private String getDataString() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        final String[] str = {""};
//        DAOProfile dao = new DAOProfile();
//        ProfileData profileData = new ProfileData();
//        Query retquery = dao.getReference().orderByChild("userName").equalTo(user.getDisplayName());
//        retquery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    for(DataSnapshot element : snapshot.getChildren()){
//                        profileData.setUserName(element.child("userName").getValue(String.class));
//                        profileData.setUserEmail(element.child("userEmail").getValue(String.class));
//                        profileData.setDisabilityDescription(element.child("disabilityDescription").getValue(String.class));
//                        profileData.setDisabilityType(element.child("disabilityType").getValue(String.class));
//                        profileData.setAadharNo(element.child("aadharNo").getValue(String.class));
//                        profileData.setEmergencyContact(element.child("emergencyContact").getValue(String.class));
//                    }
//                    createQR("http://192.168.41.148:5000/with_url_variables/" + profileData.getAadharNo());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        return "http://192.168.41.148:5000/with_url_variables/" +
                user.getUid();
    }

    private void createQR(String str) {
        Dialog dialog = new Dialog(ViewProfile.this);
        dialog.setContentView(R.layout.qr_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        Button okay_text = dialog.findViewById(R.id.okay_text);
        ImageView showQR = dialog.findViewById(R.id.qr_dialog);

        okay_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(str, BarcodeFormat.QR_CODE,350,350);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            showQR.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Toast.makeText(ViewProfile.this, "Something went wrong while generating QR", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        dialog.show();

    }
}