package com.capstone.jobapplication.jobbridge;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.capstone.jobapplication.jobbridge.databinding.ActivityProfileBinding;
import com.capstone.jobapplication.jobbridge.entity.JobSeeker;
import com.capstone.jobapplication.jobbridge.util.HttpClientGet;
import com.capstone.jobapplication.jobbridge.util.HttpClientPost;
import com.capstone.jobapplication.jobbridge.util.JsonConverter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.util.concurrent.ExecutionException;

public class ProfileActivity extends AppCompatActivity {

    String address = null;
    String path = null;
    JobSeeker jobSeeker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActivityProfileBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        String jsonData = null;
        try {
            HttpClientGet client = new HttpClientGet("/jobSeekers/1");
            AsyncTask task = client.execute();
            jsonData = (String) task.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        jobSeeker = JsonConverter.convertFromJson(jsonData, JobSeeker.class);
        binding.setJobSeeker(jobSeeker);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                address = place.getAddress().toString();
            }

            @Override
            public void onError(Status status) {
                System.out.println(status.getStatusMessage());
            }
        });

        autocompleteFragment.setText(jobSeeker.getAddress());
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

    public void submit(View view) throws ExecutionException, InterruptedException {
        jobSeeker.setAddress(address);
        String json = JsonConverter.convertFromObject(jobSeeker);
        HttpClientPost client = new HttpClientPost("/jobSeekers/update");
        AsyncTask task = client.execute(json);
        String result = (String) task.get();
        System.out.println(result);
    }
}