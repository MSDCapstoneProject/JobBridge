package com.capstone.jobapplication.jobbridge.util;

import com.capstone.jobapplication.jobbridge.entity.Job;

import java.util.HashMap;

/**
 * Created by Aicun on 6/11/2017.
 */

public class JobsCache {

    private static HashMap<Integer,Job> jobsCache = new HashMap<Integer,Job>();

    public static void reSet() {
        jobsCache.clear();
    }

    public static Job getJob(int key) {
        return jobsCache.get(key);
    }

    public static void add(int key, Job job) {
        jobsCache.put(key,job);
    }
}
