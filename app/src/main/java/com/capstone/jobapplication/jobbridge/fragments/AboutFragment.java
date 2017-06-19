package com.capstone.jobapplication.jobbridge.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.capstone.jobapplication.jobbridge.ProfileActivity;
import com.capstone.jobapplication.jobbridge.R;
import com.capstone.jobapplication.jobbridge.fcm.FcmActivity;

/**
 * Created by Aicun on 5/31/2017.
 */

public class AboutFragment extends Fragment implements View.OnClickListener {

    private View layout;

    public AboutFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.about_fragment,container,false);
        setOnListener();
        return layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile:
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.signup:
                break;
            case R.id.setting:
                Intent intentSetting = new Intent(getActivity(), FcmActivity.class);
                //Intent intentSetting = new Intent(getActivity(), MyNoticeActivity.class);
                startActivity(intentSetting);
                break;
            default:
                break;
        }
    }

    private void setOnListener() {
        layout.findViewById(R.id.profile).setOnClickListener(this);
        layout.findViewById(R.id.signup).setOnClickListener(this);
        layout.findViewById(R.id.setting).setOnClickListener(this);
    }
}
