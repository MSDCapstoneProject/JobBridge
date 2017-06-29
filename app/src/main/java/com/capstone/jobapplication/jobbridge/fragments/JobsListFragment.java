package com.capstone.jobapplication.jobbridge.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.capstone.jobapplication.jobbridge.JobDetailActivity;
import com.capstone.jobapplication.jobbridge.entity.Job;
import com.capstone.jobapplication.jobbridge.util.StableArrayAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Aicun on 5/31/2017.
 */

public class JobsListFragment extends ListFragment{

    private List<Job> jobLists = new ArrayList<>();

    public void setJobLists(List<Job> jobLists) {
        this.jobLists = jobLists;
    }

    public JobsListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final StableArrayAdapter adapter = new StableArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, jobLists,getListView());
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Job job = (Job) listView.getAdapter().getItem(position);
        Intent intent = new Intent(getActivity(), JobDetailActivity.class);
        intent.putExtra("jobId",job.getId());
        startActivity(intent);
    }
}
