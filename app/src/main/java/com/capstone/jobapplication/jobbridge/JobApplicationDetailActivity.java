package com.capstone.jobapplication.jobbridge;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.capstone.jobapplication.jobbridge.databinding.ActivityJobApplicationDetailBinding;
import com.capstone.jobapplication.jobbridge.databinding.ActivityJobDetailBinding;
import com.capstone.jobapplication.jobbridge.entity.Job;
import com.capstone.jobapplication.jobbridge.entity.JobApplication;
import com.capstone.jobapplication.jobbridge.util.CacheData;
import com.capstone.jobapplication.jobbridge.util.HttpClientGet;
import com.capstone.jobapplication.jobbridge.util.HttpClientPost;
import com.capstone.jobapplication.jobbridge.util.JsonConverter;
import com.capstone.jobapplication.jobbridge.util.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class JobApplicationDetailActivity extends AppCompatActivity {

    private JobApplication jobApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityJobApplicationDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_job_application_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        int jobApplicationId = (int) bundle.get("jobApplicationId");

        String jsonData = getJsonData("/jobapplications?jobApplicationId="+jobApplicationId);
        jobApplication = JsonConverter.convertFromJson(jsonData,JobApplication.class);

        if(jobApplication != null) {
            CacheData.addJobApplication(jobApplicationId,jobApplication);
            String jobType = CacheData.getJobType(jobApplication.getJob().getJobTypeId()).getDescription();
            jobApplication.getJob().setJobType(jobType);

            String formattedWage = StringUtil.formatWage(jobApplication.getJob().getWage());
            jobApplication.getJob().setWage(formattedWage);

            Button action = (Button) findViewById(R.id.job_application_action);
            String text = actionText(jobApplication.getApplicationStatus());
            action.setText(text);

            binding.setJobApplication(jobApplication);
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

    public void action(View view) {
        String status = jobApplication.getApplicationStatus();
        switch (status) {
            case "denied":
            case "accepted":
                onBackPressed();
                break;
            case "canceled":
                updateJobApplicationStatus(jobApplication.getId(),"applied");
                break;
            case "applied":
                //cancel application
                updateJobApplicationStatus(jobApplication.getId(),"canceled");
                break;
            default:
                onBackPressed();
        }
    }

    private String actionText(String jobApplicationStatus) {
        switch (jobApplicationStatus) {
            case "denied":
            case "accepted":
                return "OK";
            case "canceled":
                return "Apply Now";
            case "applied":
                return "Cancel";
            default:
                return "OK";
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

    private void updateJobApplicationStatus(int jobApplicationId, String status) {
        Map<String,String> keyValue = new HashMap<>();
        keyValue.put("jobApplicationId",String.valueOf(jobApplicationId));
        keyValue.put("applicationStatus",status);
        HttpClientPost post = new HttpClientPost("/jobApplications/update");
        try {
            String result =post.doPost(keyValue);
            Toast.makeText(this,"Job status has changed to "+status,Toast.LENGTH_SHORT).show();

            jobApplication.setApplicationStatus(status);
            CacheData.addJobApplication(jobApplicationId,jobApplication);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
