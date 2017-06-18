package com.capstone.jobapplication.jobbridge;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

    private String address = null;
    private JobSeeker jobSeeker;
    private Spinner genderSpinner;
    private Spinner statusSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActivityProfileBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        genderSpinner = (Spinner) findViewById(R.id.gender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.gender,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        statusSpinner = (Spinner) findViewById(R.id.status);
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this,R.array.status,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);

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

        int genderPosition = adapter.getPosition(jobSeeker.getGender());
        genderSpinner.setSelection(genderPosition);

        int statusPosition = adapter.getPosition(jobSeeker.getGender());
        statusSpinner.setSelection(statusPosition);
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
        jobSeeker.setStatus(statusSpinner.getSelectedItem().toString());
        jobSeeker.setGender(genderSpinner.getSelectedItem().toString());
        String json = JsonConverter.convertFromObject(jobSeeker);
        HttpClientPost client = new HttpClientPost("/jobSeekers/update");
        AsyncTask task = client.execute(json);
        String result = (String) task.get();
        System.out.println(result);
    }
}