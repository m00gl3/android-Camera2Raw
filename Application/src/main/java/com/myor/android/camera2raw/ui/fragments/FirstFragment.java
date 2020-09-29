package com.myor.android.camera2raw.ui.fragments;

import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.myor.android.camera2raw.R;
import com.myor.android.camera2raw.ui.AutoFitTextureView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {
    private EditText patientId;
    private Button btnContinue;

    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance() {
        FirstFragment fragment = new FirstFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void enableSubmitIfReady() {

        boolean isReady = true; // patientId.getText().toString().length() > 3;
        btnContinue.setEnabled(isReady);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        patientId = getView().findViewById(R.id.editTxtPatientNumber);

        patientId.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                enableSubmitIfReady();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        btnContinue = view.findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Navigation.findNavController(view).navigate(R.id.jpegFragment);

                EditText editTxtPatientNumber = getView().findViewById(R.id.editTxtPatientNumber);

                FirstFragmentDirections.ActionFirstFragmentToJpegFragment action = FirstFragmentDirections.actionFirstFragmentToJpegFragment();
                action.setPatientNumber(editTxtPatientNumber.getText().toString());

                Navigation.findNavController(view).navigate(action);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_home, container, false);
    }
}