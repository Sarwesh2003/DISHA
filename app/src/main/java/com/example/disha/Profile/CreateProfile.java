package com.example.disha.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.appsearch.StorageInfo;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.disha.AddPlace.data.PlaceData;
import com.example.disha.Main.MainActivity;
import com.example.disha.Main.MainController;
import com.example.disha.R;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateProfile extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 201;
    Toolbar toolbar;
    TextInputEditText usernm, email, sos, aadhar, description, disabilityType;
    CircleImageView img;
    AppCompatButton submit;
    Bitmap profileImg;
    FirebaseUser user;
    String myUri = "";
    private Uri filePath;
    private StorageReference storageProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        storageProfile = FirebaseStorage.getInstance().getReference().child("User");

        usernm = findViewById(R.id.usersName);
        email = findViewById(R.id.usersEmail);
        sos = findViewById(R.id.sos);
        aadhar = findViewById(R.id.aadhar);
        description = findViewById(R.id.disabilityDescription);
        disabilityType = findViewById(R.id.typeDisability);
        submit = findViewById(R.id.submit);
        user = FirebaseAuth.getInstance().getCurrentUser();
        profileImg = drawableToBitmap(getDrawable(R.drawable.ic_profile));
//        getPhoto();
        TextView user_name = findViewById(R.id.user_name);
        TextView gmail = findViewById(R.id.gmail);
        img = findViewById(R.id.profilepic);
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
            img.setOnClickListener(v -> {
                SelectImage();
            });
        }
            submit.setOnClickListener(v -> {
                UploadImage();
            });
    }
    public void UploadImage() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Profile");
        dialog.setMessage("Please Wait");
        dialog.show();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(filePath != null){
            final StorageReference fileref = storageProfile.child(mAuth.getCurrentUser().getUid() + ".jpg");
            UploadTask upload = fileref.putFile(filePath);
            upload.continueWithTask((Continuation) task -> {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return fileref.getDownloadUrl();
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Uri download = (Uri) task.getResult();
                        myUri = download.toString();
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("aadharNo", aadhar.getText().toString());
                        userMap.put("disabilityDescription", description.getText().toString());
                        userMap.put("disabilityType", disabilityType.getText().toString());
                        userMap.put("emergencyContact", sos.getText().toString());
                        userMap.put("userEmail", email.getText().toString());
                        userMap.put("image", myUri);
                        DAOProfile profile = new DAOProfile();
                        profile.getReference().child(mAuth.getCurrentUser().getUid()).updateChildren(userMap);
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Successully Created Profile", Toast.LENGTH_SHORT).show();
                        Intent start = new Intent(CreateProfile.this, MainActivity.class);
                        startActivity(start);
                    }
                }
            });
        }
    }
    public void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {
        super.onActivityResult(requestCode,
                resultCode,
                data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                img.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void getPhoto(){
        new CreateProfile.DownloadImageFromInternet(profileImg)
                .execute(String.valueOf(user.getPhotoUrl()));
    }
    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        public DownloadImageFromInternet(Bitmap profileImg) {
//            Toast.makeText(), "Getting Information...",Toast.LENGTH_SHORT).show();
        }
        protected Bitmap doInBackground(String... urls) {
            String imageURL=urls[0];
            Bitmap bimage = null;
            try {
                InputStream in=new java.net.URL(imageURL).openStream();
                bimage= BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
            }
            return bimage;
        }
        protected void onPostExecute(Bitmap result) {
            if(result != null)
                profileImg = result;
        }
    }
}