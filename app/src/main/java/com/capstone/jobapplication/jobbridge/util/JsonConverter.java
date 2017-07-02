package com.capstone.jobapplication.jobbridge.util;

import android.util.Log;

import com.capstone.jobapplication.jobbridge.entity.Job;
import com.capstone.jobapplication.jobbridge.entity.JobSeeker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Aicun on 6/6/2017.
 */

public class JsonConverter {

    private static Gson gson = new Gson();
    private static Gson gonWithNulls = new GsonBuilder().serializeNulls().create();

    /**
     *
     * @param json json String to be converted
     * @param clazz the target class to convert into
     * @param <T> return type
     * @return target class instance
     */
    public static <T>  T convertFromJson (String json, Class clazz) {
        try {
            json = formatJson(json);
            T instance = (T) gson.fromJson(json,clazz);
            return instance;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param o instance to be converted into json
     * @return json string
     */
    public static String convertFromObject(Object o) {
        return gson.toJson(o);
    }

    /**
     *
     * @param json json string
     * @param type target List type
     * @return
     */
    public static List convertFromJsonList(String json,Type type) {
        try {
            json = formatJsonList(json);
            List yourClassList = gson.fromJson(json, type);
            return yourClassList;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param keyValue map to be converted into json
     * @param type type of map
     * @return json string
     */
    public static String convertFromMap(Map keyValue, Type type) {
       return gson.toJson(keyValue,type);
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
