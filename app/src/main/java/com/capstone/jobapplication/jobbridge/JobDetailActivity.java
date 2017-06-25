package com.capstone.jobapplication.jobbridge;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
        int jobKey = (int) bundle.get("jobId");
        job = CacheData.getJob(jobKey);
        job.setDescription(Html.fromHtml(job.getDescription()).toString());
        updateJobViewCount(job.getId());
        binding.setJob(job);
    }

    public void applyJob(View view) throws ExecutionException, InterruptedException {
        Map<String,String> keyValue = new HashMap<>();
        keyValue.put("EmployerId",String.valueOf(job.getEmployer().getId()));
        keyValue.put("JobId",String.valueOf(job.getId()));
        keyValue.put("JobSeekerId","3");

        HttpClientPost post = new HttpClientPost("/jobApplications/add");
        String retrunValue = post.doPost(keyValue);
        if(retrunValue.contains("applied")) {
            Toast.makeText(this, "You have successfully applied for this job", Toast.LENGTH_SHORT).show();
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

    private void updateJobViewCount(int jobId) {
        Map<String,String> keyValue = new HashMap<>();
        keyValue.put("jobId",String.valueOf(job.getId()));
        HttpClientPost post = new HttpClientPost("/jobs/view");
        try {
            post.doPost(keyValue);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
