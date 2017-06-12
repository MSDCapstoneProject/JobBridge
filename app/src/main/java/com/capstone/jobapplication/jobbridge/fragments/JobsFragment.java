package com.capstone.jobapplication.jobbridge.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.capstone.jobapplication.jobbridge.JobDetailActivity;
import com.capstone.jobapplication.jobbridge.entity.Job;
import com.capstone.jobapplication.jobbridge.util.HttpClientGet;
import com.capstone.jobapplication.jobbridge.util.JobsCache;
import com.capstone.jobapplication.jobbridge.util.JsonConverter;
import com.capstone.jobapplication.jobbridge.util.StableArrayAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Aicun on 5/31/2017.
 */

public class JobsFragment extends ListFragment{

    private List<Job> jobLists = new ArrayList<Job>();

    public JobsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String jsonData = getJobsJsonData();
        if(jsonData != null) {
           jobLists = JsonConverter.convertFromJsonList(jsonData);
            for(Job job:  jobLists) {
                JobsCache.add(job.getId(),job);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final StableArrayAdapter adapter = new StableArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, jobLists);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Job job = (Job) listView.getAdapter().getItem(position);
        Intent intent = new Intent(getActivity(), JobDetailActivity.class);
        intent.putExtra("JobKey",job.getId());
        startActivity(intent);
    }

    private String getJobsJsonData() {
        String jsonData = null;
        try {
            HttpClientGet client = new HttpClientGet("/jobs");
            AsyncTask task = client.execute();
            jsonData = (String) task.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return jsonData;
    }
}
