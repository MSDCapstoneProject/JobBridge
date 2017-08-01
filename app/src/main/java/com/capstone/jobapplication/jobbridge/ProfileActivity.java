package com.capstone.jobapplication.jobbridge;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
    private EditText firstName;
    private EditText lastName;
    private EditText phoneNo;
    private EditText email;
    private EditText sin;
    private EditText dob;
    private Spinner genderSpinner;
    private Spinner statusSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActivityProfileBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        phoneNo = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        sin = (EditText) findViewById(R.id.sin);
        dob = (EditText) findViewById(R.id.birthidate);

        genderSpinner = (Spinner) findViewById(R.id.gender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.gender,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        statusSpinner = (Spinner) findViewById(R.id.status);
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this,R.array.status,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);

        String jsonData = getProfileData();
        jobSeeker = JsonConverter.convertFromJson(jsonData, JobSeeker.class);
        if(jobSeeker!=null) {
            binding.setJobSeeker(jobSeeker);


            // google address service
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
            getAddress();
            autocompleteFragment.setText(address);

            int genderPosition = adapter.getPosition(jobSeeker.getGender());
            genderSpinner.setSelection(genderPosition);

            int statusPosition = adapter.getPosition(jobSeeker.getGender());
            statusSpinner.setSelection(statusPosition);
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

    public void submit(View view){
        jobSeeker.setAddress(address);
        jobSeeker.setStatus(statusSpinner.getSelectedItem().toString());
        jobSeeker.setGender(genderSpinner.getSelectedItem().toString());
        jobSeeker.setEmail(email.getText().toString());
        jobSeeker.setPhone(phoneNo.getText().toString());
        jobSeeker.setSin(sin.getText().toString());
        jobSeeker.setDOB(dob.getText().toString());
        setAddress();
        String json = JsonConverter.convertFromObject(jobSeeker);
        try {
            HttpClientPost client = new HttpClientPost(getString(R.string.url_profileUpdate));
            AsyncTask task = client.execute(json);
            String result = (String) task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Toast.makeText(this,"Profile Updated!",Toast.LENGTH_SHORT).show();
    }
    
    private String getProfileData() {
        String jsonData = null;
        try {
            // // TODO: 6/25/2017  should be changed into real job server's id 
            HttpClientGet client = new HttpClientGet(getString(R.string.url_profile));
            AsyncTask task = client.execute();
            jsonData = (String) task.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonData;
    }

    private void getAddress() {
        String[] objs = {jobSeeker.getStreet(),jobSeeker.getCity(),jobSeeker.getProvince(),jobSeeker.getPostalCode(),jobSeeker.getCountry()};
        address = String.format("%s, %s, %s %s, %s",objs);
    }

    private void setAddress() {
        String[] info = address.split(",");
        if(info.length != 4) return;
        jobSeeker.setStreet(info[0]);
        jobSeeker.setCity(info[1]);
        String[] pc = info[2].split(" ");
        jobSeeker.setProvince(pc[0]);
        jobSeeker.setPostalCode(pc[1]+" "+pc[2]);
        jobSeeker.setCountry(info[3]);
    }
}