package com.capstone.jobapplication.jobbridge.util;

import com.capstone.jobapplication.jobbridge.entity.Job;
import com.capstone.jobapplication.jobbridge.entity.JobType;

import java.util.HashMap;

/**
 * Created by Aicun on 6/11/2017.
 */

public class CacheData {

    private static HashMap<Integer,Job> jobsCache = new HashMap<Integer,Job>();
    private static HashMap<Integer,JobType> jobTypesCache = new HashMap<>();

    public static void reSetJobsCache() {
        jobsCache.clear();
    }

    public static void reSetJobTypesCache() {
        jobTypesCache.clear();
    }

    public static Job getJob(int key) {
        return jobsCache.get(key);
    }

    public static void addJob(int key, Job job) {
        jobsCache.put(key,job);
    }

    public static void addJobType(int key, JobType type) {
        jobTypesCache.put(key,type);
    }

    public static boolean isEmpty() {
        return jobsCache.isEmpty() || jobTypesCache.isEmpty();
    }
}
