package com.capstone.jobapplication.jobbridge.util;

import android.util.Log;

import com.capstone.jobapplication.jobbridge.entity.JobSeeker;
import com.google.gson.Gson;

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

    private static String formatJson(String json) {
        int start = json.indexOf("{");
        int end = json.indexOf("}");
        return json.substring(start,end+1);
    }
}
