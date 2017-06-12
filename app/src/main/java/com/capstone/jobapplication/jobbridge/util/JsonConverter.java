package com.capstone.jobapplication.jobbridge.util;

import android.util.Log;

import com.capstone.jobapplication.jobbridge.entity.Job;
import com.capstone.jobapplication.jobbridge.entity.JobSeeker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aicun on 6/6/2017.
 */

public class JsonConverter {

    private static Gson gson = new Gson();

    public static <T>  T convertFromJson (String json, Class clazz) {
        json = formatJson(json);
        Log.i("JSON DATA",json);
        T instance = (T) gson.fromJson(json,clazz);
        return instance;
    }

    public static String convertFromObject(Object o) {
        return gson.toJson(o);
    }

    public static List<Job> convertFromJsonList(String json) {
        json = formatJsonList(json);
        Type listType = new TypeToken<ArrayList<Job>>(){}.getType();
        List<Job> yourClassList = gson.fromJson(json, listType);
        return yourClassList;
    }

    private static String formatJsonList(String json) {
        int start = json.indexOf("[");
        int end = json.lastIndexOf("]");
        return json.substring(start,end+1);
    }

    private static String formatJson(String json) {
        int start = json.indexOf("{");
        int end = json.lastIndexOf("}");
        return json.substring(start,end+1);
    }
}
