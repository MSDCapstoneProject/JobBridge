package com.capstone.jobapplication.jobbridge;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.jobapplication.jobbridge.entity.Job;
import com.capstone.jobapplication.jobbridge.entity.Subscription;
import com.capstone.jobapplication.jobbridge.entity.Topic;
import com.capstone.jobapplication.jobbridge.entity.TopicGroup;
import com.capstone.jobapplication.jobbridge.util.HttpClientGet;
import com.capstone.jobapplication.jobbridge.util.HttpClientPost;
import com.capstone.jobapplication.jobbridge.util.JsonConverter;
import com.capstone.jobapplication.jobbridge.util.StringUtil;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SubscriptionActivity extends AppCompatActivity {

    List<CheckBox> checkBoxes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        LinearLayout layout = (LinearLayout) findViewById(R.id.TopicGroups);

        List<TopicGroup> topicGroups = getTopicGroups();
        List<String> subscribedTopics = getSubscribedTopics();

        for(TopicGroup topicGroup : topicGroups) {
            TextView textView = new TextView(this);
            textView.setText(topicGroup.getDescription());
            textView.setTextSize(28);
            layout.addView(textView);
            for(Topic topic: topicGroup.getTopics()) {
                CheckBox cb = new CheckBox(this);
                cb.setId(topic.getId());
                cb.setText(topic.getDescription());
                cb.setTextSize(18);
                cb.setLeft(20);
                if(subscribedTopics.contains(String.valueOf(topic.getId()))) {
                    cb.setChecked(true);
                }
                checkBoxes.add(cb);
                layout.addView(cb);
            }
            View dividor = new View(this);
            dividor.setBackgroundColor(getResources().getColor(R.color.separator_line));
        }
    }

    private List<TopicGroup> getTopicGroups() {
        String jsonData = null;
        try {
            HttpClientGet client = new HttpClientGet("/topicGroups");
            AsyncTask task = client.execute();
            jsonData = (String) task.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<TopicGroup> topicGroups = new ArrayList<TopicGroup>();

        if(jsonData != null) {
            Type listType = new TypeToken<ArrayList<TopicGroup>>() {}.getType();
            topicGroups = JsonConverter.convertFromJsonList(jsonData, listType);
        }

        return topicGroups;
    }

    private List<String> getSubscribedTopics() {
        String jsonData = null;
        try {
            //// TODO: 7/17/2017 change to real jobseeker id
            HttpClientGet client = new HttpClientGet("/jobSeekerSubscriptions?jobSeekerId=3");
            AsyncTask task = client.execute();
            jsonData = (String) task.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> topicIds = new ArrayList<>();
        if(jsonData != null) {
            Type listType = new TypeToken<ArrayList<Subscription>>() {}.getType();
            List<Subscription> subscriptions = JsonConverter.convertFromJsonList(jsonData, listType);
            for (Subscription sub: subscriptions) {
                topicIds.add(String.valueOf(sub.getTopicId()));
            }
        }

        return topicIds;
    }

    public void subscribe(View view) {
        List<String> topicIds = new ArrayList<>();
        for(CheckBox checkBox : checkBoxes) {
            if(checkBox.isChecked()) {
                topicIds.add(String.valueOf(checkBox.getId()));
            }
        }

        String ids = JsonConverter.convertFromObject(topicIds);

        Map<String, String> keyValue = new HashMap<>();
        //// TODO: 6/25/2017 should be changed into real job server's id
        keyValue.put("jobSeekerId", "3");
        keyValue.put("topicId", ids);

        HttpClientPost post = new HttpClientPost("/jobSeekerSubscriptions/add");
        try {
            String retrunValue = post.doPost(keyValue);
            System.out.println(retrunValue);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Toast.makeText(this,"Successfully Subscribed!",Toast.LENGTH_SHORT).show();
    }
}
