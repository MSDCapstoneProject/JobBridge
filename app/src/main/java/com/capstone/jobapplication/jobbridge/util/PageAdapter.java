package com.capstone.jobapplication.jobbridge.util;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.capstone.jobapplication.jobbridge.fragments.AboutFragment;
import com.capstone.jobapplication.jobbridge.fragments.InterestFragment;
import com.capstone.jobapplication.jobbridge.fragments.JobsFragment;
import com.capstone.jobapplication.jobbridge.fragments.TabFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aicun on 6/11/2017.
 */

public class PageAdapter extends FragmentPagerAdapter {

    private final FragmentManager mFragmentManager;
    private List<Fragment> tabs = new ArrayList<Fragment>();

    public PageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        mFragmentManager = fragmentManager;
        tabs.add(new JobsFragment());
        tabs.add(new InterestFragment());
        tabs.add(new TabFragment());
        tabs.add(new AboutFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }
}
