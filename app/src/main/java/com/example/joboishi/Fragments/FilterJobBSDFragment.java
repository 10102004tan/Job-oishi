package com.example.joboishi.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.joboishi.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
public class FilterJobBSDFragment extends BottomSheetDialogFragment {

    public static FilterJobBSDFragment newInstance() {
        return new FilterJobBSDFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter_job, container, false);
    }

    public interface OnData{
        void onData(String data);
    }
}