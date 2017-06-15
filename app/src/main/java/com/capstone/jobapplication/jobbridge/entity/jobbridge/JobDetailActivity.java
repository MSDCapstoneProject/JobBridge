package com.capstone.jobapplication.jobbridge.entity.jobbridge;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;

import com.capstone.jobapplication.jobbridge.R;
import com.capstone.jobapplication.jobbridge.databinding.ActivityJobDetailBinding;
import com.capstone.jobapplication.jobbridge.entity.jobbridge.entity.Job;
import com.capstone.jobapplication.jobbridge.entity.jobbridge.util.JobsCache;

public class JobDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityJobDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_job_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        int jobKey = (int) bundle.get("JobKey");
        Job job = JobsCache.getJob(jobKey);
        job.setDescription(Html.fromHtml(job.getDescription()).toString());
        binding.setJob(job);
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
