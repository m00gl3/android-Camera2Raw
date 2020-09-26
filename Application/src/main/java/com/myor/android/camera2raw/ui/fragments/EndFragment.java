package com.myor.android.camera2raw.ui.fragments;

import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.myor.android.camera2raw.CloudManager;
import com.myor.android.camera2raw.R;
import com.myor.android.camera2raw.ui.AutoFitTextureView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EndFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EndFragment extends Fragment implements MyCallback {

    private static CloudManager mCloudManager;

    public EndFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EndFragment newInstance() {
        EndFragment fragment = new EndFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void uploadFinished() {
        // TODO: Pop back to home
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        Button btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // UPLOAD FILE UPON COMPLETION
             //   mCloudManager.uploadFile(mImagePath, "123", EndFragment.this);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mCloudManager = new CloudManager();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finish, container, false);
    }
}