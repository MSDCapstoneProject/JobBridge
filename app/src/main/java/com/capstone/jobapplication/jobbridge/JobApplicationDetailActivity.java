package com.capstone.jobapplication.jobbridge;

import android.content.ContentValues;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.capstone.jobapplication.jobbridge.databinding.ActivityJobApplicationDetailBinding;
import com.capstone.jobapplication.jobbridge.entity.JobApplication;
import com.capstone.jobapplication.jobbridge.fragments.ReminderDialogFragment;
import com.capstone.jobapplication.jobbridge.map.LocationFragment;
import com.capstone.jobapplication.jobbridge.util.CacheData;
import com.capstone.jobapplication.jobbridge.util.CalendarUtil;
import com.capstone.jobapplication.jobbridge.util.HttpClientGet;
import com.capstone.jobapplication.jobbridge.util.HttpClientPost;
import com.capstone.jobapplication.jobbridge.util.JsonConverter;
import com.capstone.jobapplication.jobbridge.util.StringUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

public class JobApplicationDetailActivity extends AppCompatActivity implements ReminderDialogFragment.NoticeDialogListener{

    private JobApplication jobApplication;
    private ActivityJobApplicationDetailBinding binding;
    private Button action;
    private Button addCalendar;
    private Button showMap;

    private static int CAL_ID = 1;
    private static final String APPLIED = "Applied";
    private static final String APPROVED = "Approved By Employer";
    private static final String DENIED = "Denied By Employer";
    private static final String CANCELED = "Cancelled";
    private static final String CANCELED2 = "Cancelled By Employer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_job_application_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addCalendar = (Button) findViewById(R.id.job_application_addCalendar);
        showMap = (Button) findViewById(R.id.job_application_showMap);

        Bundle bundle = getIntent().getExtras();
        int jobApplicationId = (int) bundle.get("jobApplicationId");

        String jsonData = getJsonData("/jobapplications?jobApplicationId=" + jobApplicationId);
        jobApplication = JsonConverter.convertFromJson(jsonData, JobApplication.class);

        if (jobApplication != null) {
            CacheData.addJobApplication(jobApplicationId, jobApplication);
            String jobType = CacheData.getJobType(jobApplication.getJob().getJobTypeId()).getDescription();
            jobApplication.getJob().setJobType(jobType);
            jobApplication.getJob().setJobLocation();
            String formattedWage = StringUtil.formatWage(jobApplication.getJob().getWage());
            jobApplication.getJob().setWage(formattedWage);
            jobApplication.getJob().setJobAddress();

            WebView desc = (WebView) findViewById(R.id.job_application_description);
            desc.setBackgroundColor(Color.TRANSPARENT);
            desc.loadDataWithBaseURL("", jobApplication.getJob().getDescription(), "text/html", "UTF-8", "");

            action = (Button) findViewById(R.id.job_application_action);
            String text = actionText(jobApplication.getApplicationStatus());
            setVisible(text);
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
            case DENIED:
                onBackPressed();
                break;
            case CANCELED:
                updateJobApplicationStatus(jobApplication.getId(), APPLIED);
                break;
            case APPLIED:
            case APPROVED:
                //cancel application
                updateJobApplicationStatus(jobApplication.getId(), CANCELED);
                break;
            default:
                onBackPressed();
        }
    }

    public void addToCalendar(View view) {
        showNoticeDialog();
    }

    public void showMap(View view) {
        Intent intentMap = new Intent(this, LocationFragment.class);

        intentMap.putExtra("toAddress",jobApplication.getJob().getJobAddress());

        startActivity(intentMap);
    }

    private String actionText(String jobApplicationStatus) {
        switch (jobApplicationStatus) {
            case DENIED:
                return "OK";
            case APPROVED:
            case APPLIED:
                return "Cancel";
            case CANCELED:
                return "Apply Now";
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
        Map<String, String> keyValue = new HashMap<>();
        keyValue.put("jobApplicationId", String.valueOf(jobApplicationId));
        keyValue.put("applicationStatus", status);
        HttpClientPost post = new HttpClientPost("/jobApplications/update");
        try {
            post.doPost(keyValue);
            Toast.makeText(this, "Job status has changed to " + status, Toast.LENGTH_SHORT).show();

            jobApplication.setApplicationStatus(status);
            CacheData.addJobApplication(jobApplicationId, jobApplication);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        binding.setJobApplication(jobApplication);
        String text = actionText(jobApplication.getApplicationStatus());
        setVisible(text);
        action.setText(text);
    }

    private void setVisible(String text) {
        if(text.equals("Cancel")) {
            addCalendar.setVisibility(View.VISIBLE);
            showMap.setVisibility(View.VISIBLE);
        }else {
            addCalendar.setVisibility(View.GONE);
            showMap.setVisibility(View.GONE);
        }

    }

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new ReminderDialogFragment();
        dialog.show(getSupportFragmentManager(), "Remind me");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        ReminderDialogFragment reminderDialogFragment = (ReminderDialogFragment) dialog;
        ContentValues values = new ContentValues();

        TimeZone timeZone = TimeZone.getDefault();

        String startDate = jobApplication.getJob().getStartDate();
        String startTime = jobApplication.getJob().getStartTime();

        String endDate = jobApplication.getJob().getEndDate();
        String endTime = jobApplication.getJob().getEndTime();

        Date start = StringUtil.formateDateTime(startDate + " " + startTime);
        Date end = StringUtil.formateDateTime(endDate + " " + endTime);

        values.put(CalendarContract.Events.CALENDAR_ID, CAL_ID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        values.put(CalendarContract.Events.DTSTART, start.getTime());
        values.put(CalendarContract.Events.DTEND, end.getTime());
        values.put(CalendarContract.Events.TITLE, jobApplication.getJob().getTitle());
        values.put(CalendarContract.Events.DESCRIPTION, jobApplication.getJob().getJobAddress());
        values.put(CalendarContract.Events.HAS_ALARM, 1);

        CalendarUtil.addEventToCalendar(this,values,reminderDialogFragment.remindMinutes);
        Toast.makeText(this,"You have added this event to Google Calender",Toast.LENGTH_SHORT).show();
        addCalendar.setVisibility(View.GONE);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
