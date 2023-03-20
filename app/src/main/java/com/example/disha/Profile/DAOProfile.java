package com.example.disha.Profile;

import android.net.Uri;

import com.example.disha.Reviews.dataModel.Review;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class DAOProfile {
    public DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth mAuth;
    public DAOProfile() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(ProfileData.class.getSimpleName());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
    }
    public DatabaseReference getReference(){
        return databaseReference;
    }

    public Task<Void> add(HashMap<String, Object> data) {
        if (data != null) {
            return databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(data);
//            return databaseReference.child(data.getAadharNo()).child().push().setValue(data);
        }
        return null;
    }
    public UploadTask addImg(Uri filepath, String placeName, String sub, String filename) {
        if (filepath != null) {
            StorageReference ref;
            if(sub == null || sub.equals("")){
                ref = storageReference
                        .child(
                                placeName + "/"
                                        + UUID.randomUUID().toString());
            }else{
                ref = storageReference
                        .child(
                                placeName + "/" + sub + "/"
                                        + filename);
            }
            return ref.putFile(filepath);
        }
        return null;
    }
    public Query get(){
        return databaseReference.orderByKey();
    }

}
