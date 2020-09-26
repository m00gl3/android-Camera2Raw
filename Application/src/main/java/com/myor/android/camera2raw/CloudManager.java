package com.myor.android.camera2raw;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myor.android.camera2raw.ui.fragments.MyCallback;

import java.io.File;

public class CloudManager {
    private  StorageReference storageRef = null;

    public CloudManager() {
        // Upload files to Firebase Cloud Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a storage reference from our app
        storageRef = storage.getReference();
    }

    public void uploadFile(String filePath, String patientId, MyCallback callback) {
        Uri file = Uri.fromFile(new File(filePath));
        StorageReference imageRef = storageRef.child(patientId + "/" + file.getLastPathSegment());
        UploadTask uploadTask = imageRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                callback.uploadFinished();
            }
        });
    }
}