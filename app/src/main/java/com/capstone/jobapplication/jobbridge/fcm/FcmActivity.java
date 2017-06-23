package com.capstone.jobapplication.jobbridge.fcm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;

import com.capstone.jobapplication.jobbridge.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by Sarah on 2017-06-15.
 */

public class FcmActivity extends AppCompatActivity {

    private static final String TAG = "FCMActivity";
    private static final String USERSETTING_PREF = "PushSetting";
    private static final String PUSHSWITCH = "PUSHSWITCH";
    private static final String TOPICJOBS = "TOPICJOBS";
    private static final String TOPICSTATUS = "TOPICSTATUS";

    Switch pushSW;
    CheckBox newJobPush, applyStatusPush;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    boolean switchP = false;
    boolean chjobsP, chstatusP = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_setting);

        pushSW = (Switch) findViewById(R.id.switchPush);
        newJobPush = (CheckBox) findViewById(R.id.newjob);
        applyStatusPush = (CheckBox) findViewById(R.id.applystatus);

        pref = getApplicationContext().getSharedPreferences(USERSETTING_PREF, MODE_PRIVATE);
        editor = pref.edit();
        FirebaseInstanceId.getInstance().getToken();


        /**
         * Load sharedpreferences of each element and set its values start
         *
         */

        switchP = pref.getBoolean("PUSHSWITCH", false);
        chjobsP = pref.getBoolean("TOPICJOBS", false);
        chstatusP = pref.getBoolean("TOPICSTATUS", false);

        if (switchP) {
            pushSW.setChecked(true);

        } else {
            pushSW.setChecked(false);

        }
        if (chjobsP) {
            newJobPush.setChecked(true);
        } else {
            newJobPush.setChecked(false);
        }

        if (chstatusP) {
            applyStatusPush.setChecked(true);
        } else {
            applyStatusPush.setChecked(false);
        }

    }

    /**
     * onSwitchChanged
     * <p>
     * when user allow push notifications, user can choose options.
     */

    public void onSwitchChanged(View view) {
        boolean on = ((Switch) view).isChecked();
        if (on) {
            setPreference(PUSHSWITCH, true);
            newJobPush.setEnabled(true);
            applyStatusPush.setEnabled(true);

            // TODO: add token and topics into mysql database

        } else {
            newJobPush.setChecked(false);
            applyStatusPush.setChecked(false);
            newJobPush.setEnabled(false);
            applyStatusPush.setEnabled(false);
            setPreference(PUSHSWITCH, false);
            setPreference(TOPICJOBS, false);
            setPreference(TOPICSTATUS, false);

            FirebaseMessaging.getInstance().unsubscribeFromTopic("jobs");
            FirebaseMessaging.getInstance().unsubscribeFromTopic("applystatus");

            // TODO: delete token and topics from mysql database


        }

    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.newjob:
                if (checked) {
                    setPreference(TOPICJOBS, true);
                    newJobPush.setChecked(true);
                    FirebaseMessaging.getInstance().subscribeToTopic("jobs");
                    FirebaseInstanceId.getInstance().getToken();

                    //TODO:add topic in mysql
                } else {
                    setPreference(TOPICJOBS, false);
                    newJobPush.setChecked(false);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("jobs");
                    //TODO:delete topic in mysql
                }
                break;
            case R.id.applystatus:
                if (checked) {
                    applyStatusPush.setChecked(true);
                    setPreference(TOPICSTATUS, true);
                    FirebaseMessaging.getInstance().subscribeToTopic("applystatus");
                    FirebaseInstanceId.getInstance().getToken();
                    //TODO:add topic in mysql

                } else {
                    applyStatusPush.setChecked(false);
                    setPreference(TOPICSTATUS, false);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("applystatus");
                    //TODO:delete topic in mysql
                }
                break;
        }
    }

    private void setPreference(String key, boolean chstatus) {

        editor.putBoolean(key, chstatus);
        editor.commit();
    }

    private void saveInSp(String key, boolean value) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("USERSETTING", android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}