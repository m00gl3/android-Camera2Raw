package com.myor.android.camera2raw.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.myor.android.camera2raw.CloudManager;
import com.myor.android.camera2raw.R;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EndFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EndFragment extends Fragment implements MyCallback {

    private static CloudManager mCloudManager;

    private String mPatientNumber;
    private String mPatientAge;
    private String mPatientGender;
    private String mPatientDetailsJpegPath;
    private String mPatientDetailsRawPath1;
    private String mPatientDetailsRawPath2;
    private String mPatientDetailsVidPath;

    private static ProgressBar progressBar;

    private static final int NUM_OF_FILES = 4;
    private int filesUploadedCounter = 0;

    private View btnView;

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

    public void uploadFailed(@NonNull Exception exception) {
        progressBar.setVisibility(View.INVISIBLE);

        String message = getString(R.string.upload_failed) + exception.getMessage();

        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    public void uploadFinished() {
        // Go to the next one
        filesUploadedCounter++;

        // Pop back to home
        if (filesUploadedCounter == NUM_OF_FILES) {
            filesUploadedCounter = 0;

            progressBar.setVisibility(View.INVISIBLE);

            new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.session_completed)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Delete uploaded files ?
                            // deleteFiles();

                            // Clear Nav Stack and Pop to first fragment
                            EndFragmentDirections.ActionEndFragmentToFirstFragment action = EndFragmentDirections.actionEndFragmentToFirstFragment();
                            Navigation.findNavController(btnView).navigate(action);
                        }
                    })
                    .show();

        } else {
            switch (filesUploadedCounter) {
                case 1:
                    uploadFile(mPatientDetailsRawPath1);
                    break;
                case 2:
                    uploadFile(mPatientDetailsRawPath2);
                    break;
                case 3:
                    uploadFile(mPatientDetailsVidPath);
                    break;
            }
        }

    }

    private void deleteFiles() {
        File file = new File(mPatientDetailsJpegPath);
        file.delete();

        File file2 = new File(mPatientDetailsRawPath1);
        file2.delete();

        File file3 = new File(mPatientDetailsRawPath2);
        file3.delete();

        File file4 = new File(mPatientDetailsVidPath);
        file4.delete();
    }

    private void uploadFile(String path) {
        mCloudManager.uploadFile(path, mPatientNumber,mPatientAge,mPatientGender, this);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        mPatientNumber = Camera2VideoFragmentArgs.fromBundle(getArguments()).getPatientNumber();
        mPatientAge = Camera2VideoFragmentArgs.fromBundle(getArguments()).getPatientAge();
        mPatientGender = Camera2VideoFragmentArgs.fromBundle(getArguments()).getPatientGender();
        mPatientDetailsJpegPath = Camera2VideoFragmentArgs.fromBundle(getArguments()).getPatientDetailsJpeg();
        mPatientDetailsRawPath1 = Camera2VideoFragmentArgs.fromBundle(getArguments()).getPatientDetailsRaw1();
        mPatientDetailsRawPath2 = Camera2VideoFragmentArgs.fromBundle(getArguments()).getPatientDetailsRaw2();
        mPatientDetailsVidPath = Camera2VideoFragmentArgs.fromBundle(getArguments()).getPatientDetailsVid();

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_cyclic);

        Button btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Used later in Navigatoin
                btnView = view;

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