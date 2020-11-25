package com.myor.android.camera2raw.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.myor.android.camera2raw.CloudManager;
import com.myor.android.camera2raw.R;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TestUploadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestUploadFragment extends Fragment implements MyCallback {

    private static CloudManager mCloudManager;

    private String mPatientNumber;
    private String mPatientAge;
    private String mPatientGender;
    private String mPatientDetailsJpegPath;

    private static ProgressBar progressBar;


    public TestUploadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TestUploadFragment newInstance() {
        TestUploadFragment fragment = new TestUploadFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void uploadFailed(@NonNull Exception exception) {
        progressBar.setVisibility(View.INVISIBLE);

        String message = getString(R.string.upload_failed) + exception.getMessage();

        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    public void uploadFinished() {


            progressBar.setVisibility(View.INVISIBLE);

            new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.session_completed)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();


    }

    private void uploadFile(String path) {
        mCloudManager.uploadFile(path, mPatientNumber, mPatientAge,mPatientGender ,this);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        mPatientNumber = Camera2JpegFragmentArgs.fromBundle(getArguments()).getPatientNumber();
        mPatientAge = Camera2JpegFragmentArgs.fromBundle(getArguments()).getPatientAge();
        mPatientGender = Camera2JpegFragmentArgs.fromBundle(getArguments()).getPatientGender();
        mPatientDetailsJpegPath = Camera2JpegFragmentArgs.fromBundle(getArguments()).getPatientDetailsJpeg();

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_cyclic);

        Button btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear Nav Stack and Pop to first fragment
              //  TestUploadFragmentDirections.ActionUploadTestFragmentToFirstFragment action = TestUploadFragmentDirections.actionUploadTestFragmentToFirstFragment();
              //  Navigation.findNavController(view).navigate(action);

                progressBar.setVisibility(View.VISIBLE);

                uploadFile(mPatientDetailsJpegPath);
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