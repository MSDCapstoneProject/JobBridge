package com.capstone.jobapplication.jobbridge.fragments;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.capstone.jobapplication.jobbridge.JobApplicationDetailActivity;
import com.capstone.jobapplication.jobbridge.R;
import com.capstone.jobapplication.jobbridge.entity.JobApplication;
import com.capstone.jobapplication.jobbridge.util.CacheData;
import com.capstone.jobapplication.jobbridge.util.HttpClientGet;
import com.capstone.jobapplication.jobbridge.util.JsonConverter;
import com.capstone.jobapplication.jobbridge.util.StringUtil;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Aicun on 5/31/2017.
 */

public class InterestFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView itemsListView;
    private List<JobApplication> appliedJobs = new ArrayList<>();
    private static int DIVIDOR_HEIGHT = 50;
    public InterestFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.interest_fragment, container, false);
        itemsListView = (ListView) view.findViewById(R.id.appliedJobsListView);
        itemsListView.setOnItemClickListener(this);
        ColorDrawable drawable = new ColorDrawable(getResources().getColor(R.color.dividor));
        itemsListView.setDivider(drawable);
        itemsListView.setDividerHeight(DIVIDOR_HEIGHT);

        loadData();
        setListViewAdapter();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
        setListViewAdapter();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JobApplication jobApplication = appliedJobs.get(position);
        Intent intent = new Intent(getActivity(), JobApplicationDetailActivity.class);
        intent.putExtra("jobApplicationId", jobApplication.getId());
        startActivity(intent);
    }

    private void setListViewAdapter() {
        List<Map<String, String>> data = new ArrayList<>();

        for (JobApplication appliedJob : appliedJobs) {
            Map<String, String> map = new HashMap<>();
            map.put("jobTitle", appliedJob.getJob().getTitle());
            map.put("company", appliedJob.getEmployer().getName());
            map.put("location", StringUtil.formatLocation(appliedJob.getJob().getCity(),appliedJob.getJob().getProvince()));
            map.put("applicationStatus", appliedJob.getApplicationStatus());
            data.add(map);
        }

        String[] from = {"jobTitle", "company", "location", "applicationStatus"};
        int[] to = {R.id.jobTitle, R.id.company, R.id.location, R.id.applicationStatus};
        SimpleAdapter adapter = new SimpleAdapter(getContext(), data, R.layout.listview_applied_jobs, from, to);
        itemsListView.setAdapter(adapter);
    }

    private void loadData(){
        if (!CacheData.isEmpty()) {
            appliedJobs = CacheData.cachedJobApplications();
        } else {
            loadDateFromServer();
        }
    }

    private void loadDateFromServer() {
        String jsonData = getJsonData(getString(R.string.url_myJobs));
        if (jsonData != null) {
            Type listType = new TypeToken<ArrayList<JobApplication>>() {
            }.getType();
            appliedJobs = JsonConverter.convertFromJsonList(jsonData, listType);
            if(appliedJobs != null) {
                for (JobApplication appliedJob : appliedJobs) {
                    CacheData.addJobApplication(appliedJob.getId(), appliedJob);
                }
            }
        }
    }

    private String getJsonData(String path) {
        String jsonData;
        try {
            HttpClientGet client = new HttpClientGet(path);
            jsonData = client.getJsonData();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return jsonData;
    }
}
