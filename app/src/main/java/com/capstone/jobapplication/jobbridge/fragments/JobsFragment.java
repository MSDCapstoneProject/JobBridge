package com.capstone.jobapplication.jobbridge.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.capstone.jobapplication.jobbridge.R;
import com.capstone.jobapplication.jobbridge.entity.Job;
import com.capstone.jobapplication.jobbridge.entity.JobRating;
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
    private JobsListFragment jobsListFragment;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        loadJobData();
        view = inflater.inflate(R.layout.jobs_fragment, container, false);
        Button searchJobs = (Button) view.findViewById(R.id.searchJobs);
        Button filterJobs = (Button) view.findViewById(R.id.filterJobs);
        Button filterUpdate = (Button) view.findViewById(R.id.filter_update);
        search = (EditText) view.findViewById(R.id.search);
        filter = view.findViewById(R.id.jobs_filter_parameters);
        filter.setVisibility(View.GONE);
        wageFilterSeekBar = (SeekBar) view.findViewById(R.id.filter_wage_selector);
        wageFilterTextView = (TextView) view.findViewById(R.id.filter_wage);
        wageFilterTextView.setText(String.format("$%d /h", MINIMUM_WAGE));

        jobTypeFilterSpinner = (Spinner) view.findViewById(R.id.filter_jobType);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1, jobTypeNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobTypeFilterSpinner.setAdapter(adapter);

        //job list
        jobsListFragment = new JobsListFragment();
        jobsListFragment.setJobLists(jobLists);
        getFragmentManager().beginTransaction().add(R.id.job_list_fragment, jobsListFragment).commit();

        //click search button to search jobs
        searchJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchJobs();
                hideSoftInput();
            }
        });

        // click filter button to show filter criteria
        filterJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter.setVisibility(filter.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                hideSoftInput();
            }
        });

        // click update button to update filter result
        filterUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateJobsSearch();
                hideSoftInput();
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

    //search job according to key word and update job list fragement
    private void searchJobs() {
        String keyWord = search.getText().toString();
        searchedJobs = searchJobs(keyWord);
        jobsListFragment.setJobLists(searchedJobs);
        getFragmentManager().beginTransaction().replace(R.id.job_list_fragment, jobsListFragment).commit();
    }

    // search jobs from local cache
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

    //filter jobs base on criteria
    private void updateJobsSearch() {
        float wage = wageFilterSeekBar.getProgress() + MINIMUM_WAGE;
        String jobType = jobTypeFilterSpinner.getSelectedItem().toString();
        List<Job> filteredJobs = new ArrayList<>();
        for(Job job: searchedJobs) {
            if(Float.valueOf(job.getWage())>=wage && jobType.equalsIgnoreCase(job.getJobType())) {
                filteredJobs.add(job);
            }
        }

        jobsListFragment.setJobLists(filteredJobs);
        getFragmentManager().beginTransaction().replace(R.id.job_list_fragment, jobsListFragment).commit();
        filter.setVisibility(View.GONE);
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

    //get jobs and jobTypes
    private void loadJobData() {
        if (!CacheData.isEmpty()) {
            jobLists = CacheData.cachedJobs();
        } else {
            loadJobDataFromServer();
        }
    }

    private void loadJobDataFromServer() {
        String jobsJsonData = getJsonData(getString(R.string.url_jobsList));
        String jobTypesJsonData = getJsonData(getString(R.string.url_jobTypesList));
        String jobRatingsJsonData = getJsonData(getString(R.string.url_jobRatings));
        if (jobsJsonData != null) {
            Type listType = new TypeToken<ArrayList<Job>>() {}.getType();
            jobLists = JsonConverter.convertFromJsonList(jobsJsonData, listType);
            if(jobLists != null) {
                searchedJobs = jobLists;
                for (Job job : jobLists) {
                    job.setJobAddress();
                    job.setJobLocation();
                    CacheData.addJob(job.getId(), job);
                }
            }
        }
        if (jobTypesJsonData != null) {
            Type listType = new TypeToken<ArrayList<JobType>>() {
            }.getType();
            List<JobType> jobTypes = JsonConverter.convertFromJsonList(jobTypesJsonData, listType);
            if(jobTypes != null) {
                for (JobType type : jobTypes) {
                    CacheData.addJobType(type.getId(), type);
                    jobTypeNames.add(type.getDescription());
                }
            }
        }

        if (jobRatingsJsonData != null) {
            Type listType = new TypeToken<ArrayList<JobRating>>() {
            }.getType();
            List<JobRating> jobRatings = JsonConverter.convertFromJsonList(jobRatingsJsonData, listType);
            if(jobRatings != null) {
                for (JobRating rating : jobRatings) {
                    CacheData.addJobRating(rating.getJobId(),rating);
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

    private void hideSoftInput() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
