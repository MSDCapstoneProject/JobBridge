package com.capstone.jobapplication.jobbridge;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;

import com.capstone.jobapplication.jobbridge.databinding.ActivityJobDetailBinding;
import com.capstone.jobapplication.jobbridge.entity.Job;
import com.capstone.jobapplication.jobbridge.util.CacheData;
import com.capstone.jobapplication.jobbridge.util.HttpClientPost;
import com.capstone.jobapplication.jobbridge.util.JsonConverter;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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
        int jobKey = (int) bundle.get("JobKey");
        job = CacheData.getJob(jobKey);
        job.setDescription(Html.fromHtml(job.getDescription()).toString());
        binding.setJob(job);
    }

    public void applyJob(View view) throws ExecutionException, InterruptedException {
        Map<String,String> keyValue = new HashMap<>();
        keyValue.put("EmployerId",String.valueOf(job.getEmployer().getId()));
        keyValue.put("JobId",String.valueOf(job.getId()));
        keyValue.put("JobSeekerId","1");

        HttpClientPost post = new HttpClientPost("/jobApplications/add");
        String retrunValue = post.doPost(keyValue);
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
}
