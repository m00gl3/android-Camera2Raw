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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.myor.android.camera2raw.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {
    private EditText patientId;
    private EditText patientAge;
    private RadioGroup radioGroup;
    private RadioButton selectedRadioButton;
    private String patientGender;
    private Button btnContinue;
    boolean genderIsChecked;
    private int ID;

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
        if (patientId.getText().toString().length() > 0 && patientAge.getText().toString().length() > 0 && genderIsChecked) {
            btnContinue.setEnabled(isReady);
        } else { btnContinue.setEnabled(false);}
//        btnContinue.setEnabled(isReady);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        patientId = getView().findViewById(R.id.editTxtPatientNumber);
        patientAge = getView().findViewById(R.id.editTxtPatientAge);

        genderIsChecked=false;
        radioGroup =getView().findViewById(R.id.radioGroup);
        selectedRadioButton= getView().findViewById(radioGroup.getCheckedRadioButtonId());
//        patientGender=selectedRadioButton.getText().toString();
//        int ID = selectedRadioButton
////        String yourVote = selectedRadioButton.getText().toString();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton= (RadioButton)group.findViewById(checkedId);
                genderIsChecked=true;
                enableSubmitIfReady();
            }
        });
        patientId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                enableSubmitIfReady();
            }
        });
        patientAge.addTextChangedListener(new TextWatcher() {
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
                EditText editTxtPatientAge = getView().findViewById(R.id.editTxtPatientAge);

                RadioGroup radioGroup =getView().findViewById(R.id.radioGroup);
                RadioButton selectedRadioButton= getView().findViewById(radioGroup.getCheckedRadioButtonId());

                FirstFragmentDirections.ActionFirstFragmentToJpegFragment action = FirstFragmentDirections.actionFirstFragmentToJpegFragment();
                action.setPatientNumber(editTxtPatientNumber.getText().toString());
                action.setPatientAge(editTxtPatientAge.getText().toString());
//                patientGender=selectedRadioButton.getText().toString();
                if (selectedRadioButton.getText().toString().equals("נקבה")) {patientGender="female";} else if (selectedRadioButton.getText().toString().equals("זכר"))  {patientGender="male";} else {patientGender="Unreconized";}
                action.setPatientGender(patientGender);
//                action.setPatientGender(editRadioPatientGender.getText().toString());

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