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

import com.myor.android.camera2raw.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HandPhotoInstructionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HandPhotoInstructionsFragment extends Fragment {
    private String mPatientNumber;
    private String mPatientDetailsJpegPath;

    public HandPhotoInstructionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HandPhotoInstructionsFragment newInstance() {
        HandPhotoInstructionsFragment fragment = new HandPhotoInstructionsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        mPatientNumber = Camera2JpegFragmentArgs.fromBundle(getArguments()).getPatientNumber();
        mPatientDetailsJpegPath = Camera2JpegFragmentArgs.fromBundle(getArguments()).getPatientDetailsJpeg();

        Button btnContinue = view.findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HandPhotoInstructionsFragmentDirections.ActionHandPhotoInstructionsFragmentToRawFragment action = HandPhotoInstructionsFragmentDirections.actionHandPhotoInstructionsFragmentToRawFragment();
                action.setPatientNumber(mPatientNumber);
                action.setPatientDetailsJpeg(mPatientDetailsJpegPath);
                Navigation.findNavController(view).navigate(action);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hand_photo_instructions, container, false);
    }
}