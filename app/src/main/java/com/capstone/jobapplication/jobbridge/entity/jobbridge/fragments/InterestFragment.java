package com.capstone.jobapplication.jobbridge.entity.jobbridge.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.capstone.jobapplication.jobbridge.R;


/**
 * Created by Aicun on 5/31/2017.
 */

public class InterestFragment extends Fragment {

    public InterestFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.interest_fragment,container,false);
    }
}
