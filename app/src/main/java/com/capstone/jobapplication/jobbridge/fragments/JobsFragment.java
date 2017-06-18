package com.capstone.jobapplication.jobbridge.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.capstone.jobapplication.jobbridge.R;
import com.capstone.jobapplication.jobbridge.entity.Job;
import com.capstone.jobapplication.jobbridge.entity.JobType;
import com.capstone.jobapplication.jobbridge.util.HttpClientGet;
import com.capstone.jobapplication.jobbridge.util.CacheData;
import com.capstone.jobapplication.jobbridge.util.JsonConverter;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aicun on 6/15/2017.
 */

public class JobsFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private static int MINIMUM_WAGE = 10;

    private List<Job> jobLists = new ArrayList<Job>();
    private List<String> jobTypeNames = new ArrayList<>();
    private List<Job> searchedJobs = new ArrayList<Job>();

    public JobsFragment() {
    }

    private View view;
    private EditText search;
    private View filter;
    private SeekBar wageFilterSeekBar;
    private TextView wageFilterTextView;
    private Spinner jobTypeFilterSpinner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String jobsJsonData = getJobsJsonData();
        String jobTypesJsonData = getJobTypesJsonData();
        if (jobsJsonData != null) {
            Type listType = new TypeToken<ArrayList<Job>>(){}.getType();
            jobLists = JsonConverter.convertFromJsonList(jobsJsonData, listType);
            searchedJobs = jobLists;
            for (Job job : jobLists) {
                CacheData.addJob(job.getId(), job);
            }
        }
        if(jobTypesJsonData !=null) {
            Type listType = new TypeToken<ArrayList<JobType>>(){}.getType();
            List<JobType> jobTypes = JsonConverter.convertFromJsonList(jobTypesJsonData, listType);
            for (JobType type : jobTypes) {
                CacheData.addJobType(type.getId(), type);
                jobTypeNames.add(type.getDescription());
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.jobs_fragment, container, false);
        Button searchJobs = (Button) view.findViewById(R.id.searchJobs);
        Button filterJobs = (Button) view.findViewById(R.id.filterJobs);
        Button filterUpdate = (Button) view.findViewById(R.id.filter_update);
        search = (EditText) view.findViewById(R.id.search);
        filter = view.findViewById(R.id.jobs_filter_parameters);
        filter.setVisibility(View.GONE);
        wageFilterSeekBar = (SeekBar) view.findViewById(R.id.filter_wage_selector);
        wageFilterTextView = (TextView) view.findViewById(R.id.filter_wage);

        jobTypeFilterSpinner = (Spinner) view.findViewById(R.id.filter_jobType);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1, jobTypeNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobTypeFilterSpinner.setAdapter(adapter);

        JobsListFragment jobsListFragment = new JobsListFragment();
        jobsListFragment.setJobLists(jobLists);

        getFragmentManager().beginTransaction().add(R.id.job_list_fragment, jobsListFragment).commit();

        searchJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchJobs();
            }
        });

        filterJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter.setVisibility(filter.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });

        filterUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateJobsSearch();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
            }
        });

        wageFilterSeekBar.setOnSeekBarChangeListener(this);

        return view;
    }

    private void searchJobs() {
        String keyWord = search.getText().toString();
        searchedJobs = searchJobs(keyWord);
        JobsListFragment jobsListFragment = new JobsListFragment();
        jobsListFragment.setJobLists(searchedJobs);
        getFragmentManager().beginTransaction().replace(R.id.job_list_fragment, jobsListFragment).commit();
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

    private String getJobTypesJsonData() {
        String jsonData = null;
        try {
            HttpClientGet client = new HttpClientGet("/jobtypes");
            AsyncTask task = client.execute();
            jsonData = (String) task.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return jsonData;
    }

    private List<Job> searchJobs(String keyWord) {
        List<Job> jobs = new ArrayList<Job>();
        for (Job job : jobLists) {
            if (contains(job.getTitle(), keyWord)
                    || contains(job.getEmployer().getName(), keyWord)
                    || contains(job.getJobType(), keyWord)
                    || contains(job.getJobCategory(), keyWord)) {
                jobs.add(job);
            }
        }
        return jobs;
    }

    private void updateJobsSearch() {
        float wage = wageFilterSeekBar.getProgress() + MINIMUM_WAGE;
        String jobType = jobTypeFilterSpinner.getSelectedItem().toString();
        List<Job> filteredJobs = new ArrayList<>();
        for(Job job: searchedJobs) {
            if(Float.valueOf(job.getWage())>wage && jobType.equalsIgnoreCase(job.getJobType())) {
                filteredJobs.add(job);
            }
        }

        JobsListFragment jobsListFragment = new JobsListFragment();
        jobsListFragment.setJobLists(filteredJobs);
        getFragmentManager().beginTransaction().replace(R.id.job_list_fragment, jobsListFragment).commit();
    }

    private boolean contains(String source, String keyWord) {
        if (source == null)
            return false;
        int length = keyWord.length();
        if (length == 0)
            return true;

        char firstLo = Character.toLowerCase(keyWord.charAt(0));
        char firstUp = Character.toUpperCase(keyWord.charAt(0));

        for (int i = source.length() - length; i >= 0; i--) {
            char ch = source.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (source.regionMatches(true, i, keyWord, 0, length))
                return true;
        }

        return false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        progress = progress + MINIMUM_WAGE;
        wageFilterTextView.setText(String.format("$%d /h", progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}