package com.capstone.jobapplication.jobbridge;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.capstone.jobapplication.jobbridge.databinding.ActivityProfileBinding;
import com.capstone.jobapplication.jobbridge.entity.JobSeeker;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

public class ProfileActivity extends AppCompatActivity {

    String address = null;
    JobSeeker jobSeeker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityProfileBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        jobSeeker = new JobSeeker();
        jobSeeker.setLastName("a");
        jobSeeker.setFirstName("b");
        jobSeeker.setAddress("add");
        jobSeeker.setBirthDate("1234");
        jobSeeker.setEmail("addfad");
        jobSeeker.setGender("Male");
        jobSeeker.setPhone("2222");
        jobSeeker.setSin("1222");
        jobSeeker.setStatus("S");
        binding.setJobSeeker(jobSeeker);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setText("fdafdafafdas");

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                address = place.getName().toString();
            }

            @Override
            public void onError(Status status) {
            }
        });
    }
}
