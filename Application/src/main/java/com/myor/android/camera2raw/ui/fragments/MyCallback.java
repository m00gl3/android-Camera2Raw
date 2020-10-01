package com.myor.android.camera2raw.ui.fragments;

import androidx.annotation.NonNull;

// The callback interface
public interface MyCallback {
    void uploadFinished();
    void uploadFailed(@NonNull Exception exception);
}
