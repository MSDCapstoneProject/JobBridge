package com.capstone.jobapplication.jobbridge;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.jobapplication.jobbridge.databinding.ActivityJobDetailBinding;
import com.capstone.jobapplication.jobbridge.entity.Job;
import com.capstone.jobapplication.jobbridge.util.CacheData;
import com.capstone.jobapplication.jobbridge.util.CalendarUtil;
import com.capstone.jobapplication.jobbridge.util.HttpClientGet;
import com.capstone.jobapplication.jobbridge.util.HttpClientPost;
import com.capstone.jobapplication.jobbridge.util.JsonConverter;
import com.capstone.jobapplication.jobbridge.util.StringUtil;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class JobDetailActivity extends AppCompatActivity {

    private Job job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityJobDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_job_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        int jobKey = (int) bundle.get("jobId");
        job = CacheData.getJob(jobKey);

        // new job is not loaded in local cache
        if(job == null) {
            job = getJobFromServer(jobKey);
        }
        if(job != null) {
            String formattedWage = StringUtil.formatWage(job.getWage());
            job.setWage(formattedWage);
            job.setJobLocation();
            WebView desc = (WebView) findViewById(R.id.job_description);
            desc.setBackgroundColor(Color.TRANSPARENT);
            desc.loadDataWithBaseURL("", job.getDescription(), "text/html", "UTF-8", "");
            updateJobViewCount(job.getId());
            binding.setJob(job);
        }
    }

    public void applyJob(View view) throws ExecutionException, InterruptedException {
        Map<String,String> keyValue = new HashMap<>();
        keyValue.put("employerId",String.valueOf(job.getEmployer().getId()));
        keyValue.put("jobId",String.valueOf(job.getId()));
        //// TODO: 6/25/2017 should be changed into real job server's id
        keyValue.put("jobSeekerId","3");

        HttpClientPost post = new HttpClientPost("/jobApplications/add");
        String retrunValue = post.doPost(keyValue);
        if(retrunValue.contains("\"jobApplicationStatusId\":1")) {
            Toast.makeText(this, "You have successfully applied for this job", Toast.LENGTH_SHORT).show();
            CacheData.reSetAppliedJobsCache();
        }else {
            Toast.makeText(this,"Sorry, you failed to applied for this job. Try again later", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //each time a job seeker views the detail of the job, the job view count +1
    private void updateJobViewCount(int jobId) {
        Map<String,String> keyValue = new HashMap<>();
        keyValue.put("jobId",String.valueOf(jobId));
        HttpClientPost post = new HttpClientPost("/jobs/view");
        try {
            post.doPost(keyValue);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Job getJobFromServer(int jobId) {
        String path = "/jobs/"+jobId;
        HttpClientGet client = new HttpClientGet(path);
        String jobJsonData = client.getJsonData();
        Job jobFromServer = null;
        if (jobJsonData != null) {
            jobFromServer = JsonConverter.convertFromJson(jobJsonData, Job.class);
            if(jobFromServer != null)
                jobFromServer.setJobAddress();
                CacheData.addJob(jobFromServer.getId(), jobFromServer);
        }
        return jobFromServer;
    }

   /* @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }*/
}
