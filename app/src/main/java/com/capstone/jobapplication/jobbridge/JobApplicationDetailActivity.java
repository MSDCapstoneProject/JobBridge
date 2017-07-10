package com.capstone.jobapplication.jobbridge;

import android.content.ContentValues;
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

    private static int CAL_ID = 1;
    private static final String APPLIED = "Applied";
    private static final String APPROVED = "Approved By Employer";
    private static final String DENIED = "Denied By Employer";
    private static final String CANCELED = "Cancelled By User";
    private static final String CANCELED2 = "Cancelled By Employer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityJobApplicationDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_job_application_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
            case DENIED:
            case APPROVED:
                onBackPressed();
                break;
            case CANCELED:
                updateJobApplicationStatus(jobApplication.getId(), APPLIED);
                break;
            case APPLIED:
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
        String status = jobApplication.getApplicationStatus();
        switch (status) {
            case DENIED:
            case APPROVED:
                onBackPressed();
                break;
            case CANCELED:
                updateJobApplicationStatus(jobApplication.getId(), "applied");
                break;
            case APPLIED:
                //cancel application
                updateJobApplicationStatus(jobApplication.getId(), "canceled");
                break;
            default:
                onBackPressed();
        }
    }

    private String actionText(String jobApplicationStatus) {
        switch (jobApplicationStatus) {
            case DENIED:
            case APPROVED:
                return "OK";
            case CANCELED:
                return "Apply Now";
            case APPLIED:
                Button addCalendar = (Button) findViewById(R.id.job_application_addCalendar);
                addCalendar.setVisibility(View.VISIBLE);
                Button showMap = (Button) findViewById(R.id.job_application_showMap);
                showMap.setVisibility(View.VISIBLE);
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
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
