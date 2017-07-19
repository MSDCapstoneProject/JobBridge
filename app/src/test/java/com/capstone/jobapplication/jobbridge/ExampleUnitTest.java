package com.capstone.jobapplication.jobbridge;

import android.os.AsyncTask;
import android.util.Log;

import com.capstone.jobapplication.jobbridge.entity.TopicGroup;
import com.capstone.jobapplication.jobbridge.util.HttpClientGet;
import com.capstone.jobapplication.jobbridge.util.JsonConverter;
import com.capstone.jobapplication.jobbridge.util.StringUtil;
import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        /*String[] objs = {"16 cedarwoods","Kitchener","ON","CA","N2C 2L4"};
        System.out.println(String.format("%s, %s, %s, %s, %s",objs));*/

        /*String jsonData = null;

        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("https://jobbridge.herokuapp.com/topicGroups");
            urlConnection = (HttpURLConnection) url.openConnection();
            BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
            byte[] contents = new byte[1024];

            int bytesRead;
            while ((bytesRead = in.read(contents)) != -1) {
                jsonData += new String(contents, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        List<TopicGroup> topicGroups = new ArrayList<TopicGroup>();

        if(jsonData != null) {
            Type listType = new TypeToken<ArrayList<TopicGroup>>() {}.getType();
            topicGroups = JsonConverter.convertFromJsonList(jsonData, listType);
        }

        System.out.println(topicGroups.size());*/

        List<String> ids = new ArrayList<>();
        ids.add("1");
        ids.add("2");
        ids.add("3");
        String idsStr = JsonConverter.convertFromObject(ids);
        System.out.println(idsStr);
    }
}