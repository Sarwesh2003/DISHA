package com.example.disha.AddPlace.data;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class DAOPlaceData {
    public DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;

    public DAOPlaceData(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(PlaceData.class.getSimpleName());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    public DatabaseReference getReference(){
        return databaseReference;
    }

    public Task<Void> add(PlaceData data) {
        if (data != null) {
            return databaseReference.push().setValue(data);
        }
        return null;
    }

    public UploadTask addImg(Uri filepath, String placeName) {
        if (filepath != null) {
            StorageReference ref
                    = storageReference
                    .child(
                            placeName+"/"
                                    + UUID.randomUUID().toString());
            return ref.putFile(filepath);
        }
        return null;
    }
    public Query get(){
        return databaseReference.orderByKey();
    }
}
