package com.myor.android.camera2raw.ui.base;

import androidx.fragment.app.Fragment;
import android.os.Bundle;

import android.view.View;

import androidx.annotation.Nullable;

public abstract class BaseFragment extends Fragment {
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUp(view);
    }

    protected abstract void setUp(View view);
}
