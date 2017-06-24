package com.capstone.jobapplication.jobbridge;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.capstone.jobapplication.jobbridge.databinding.ActivityJobApplicationDetailBinding;
import com.capstone.jobapplication.jobbridge.databinding.ActivityJobDetailBinding;
import com.capstone.jobapplication.jobbridge.entity.Job;
import com.capstone.jobapplication.jobbridge.entity.JobApplication;
import com.capstone.jobapplication.jobbridge.util.CacheData;

public class JobApplicationDetailActivity extends AppCompatActivity {

    private JobApplication jobApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityJobApplicationDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_job_application_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        int jobApplicationId = (int) bundle.get("jobApplicationId");
        jobApplication = CacheData.getJobApplication(jobApplicationId);

        Button action = (Button) findViewById(R.id.job_application_action);
        String text = actionText(jobApplication.getApplicationStatus());
        action.setText(text);

        binding.setJobApplication(jobApplication);
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
            case "cancelled":
                onBackPressed();
                break;
            case "applied":
                //// TODO: 6/24/2017 to cancel application
                break;
            default:
                onBackPressed();
        }
    }

    private String actionText(String jobApplicationStatus) {
        switch (jobApplicationStatus) {
            case "denied":
            case "accepted":
            case "cancelled":
                return "OK";
            case "applied":
                return "Cancel";
            default:
                return "OK";
        }
    }
}
