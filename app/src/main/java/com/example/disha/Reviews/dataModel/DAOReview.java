package com.example.disha.Reviews.dataModel;

import com.example.disha.AddPlace.data.PlaceData;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DAOReview {
    public DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;

    public DAOReview() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Review.class.getSimpleName());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    public DatabaseReference getReference(){
        return databaseReference;
    }

    public Task<Void> add(Review data) {
        if (data != null) {
            return databaseReference.push().setValue(data);
        }
        return null;
    }
    public Query get(){
        return databaseReference.orderByKey();
    }
}
