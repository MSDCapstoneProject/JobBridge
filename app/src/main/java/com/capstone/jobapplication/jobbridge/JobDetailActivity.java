package com.capstone.jobapplication.jobbridge;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;

import com.capstone.jobapplication.jobbridge.databinding.ActivityJobDetailBinding;
import com.capstone.jobapplication.jobbridge.entity.Job;
import com.capstone.jobapplication.jobbridge.util.CacheData;

public class JobDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityJobDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_job_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        int jobKey = (int) bundle.get("JobKey");
        Job job = CacheData.getJob(jobKey);
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
