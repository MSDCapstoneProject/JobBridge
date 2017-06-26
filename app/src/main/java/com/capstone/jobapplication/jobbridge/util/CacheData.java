package com.capstone.jobapplication.jobbridge.util;

import com.capstone.jobapplication.jobbridge.entity.Job;
import com.capstone.jobapplication.jobbridge.entity.JobApplication;
import com.capstone.jobapplication.jobbridge.entity.JobType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Aicun on 6/11/2017.
 */

public class CacheData {

    private static HashMap<Integer,Job> jobsCache = new HashMap<Integer,Job>();
    private static HashMap<Integer,JobType> jobTypesCache = new HashMap<>();
    private static HashMap<Integer,JobApplication> appliedJobsCache = new HashMap<>();

    public static void reSetJobsCache() {
        jobsCache.clear();
    }

    public static void reSetJobTypesCache() {
        jobTypesCache.clear();
    }

    public static void reSetAppliedJobsCache() {
        appliedJobsCache.clear();
    }

    public static Job getJob(int key) {
        return jobsCache.get(key);
    }

    public static void addJob(int key, Job job) {
        jobsCache.put(key,job);
    }

    public static JobApplication getJobApplication(int key) {
        return appliedJobsCache.get(key);
    }

    public static void addJobApplication(int key, JobApplication jobApplication) {
        appliedJobsCache.put(key,jobApplication);
    }

    public static JobType getJobType(int key) {
        return jobTypesCache.get(key);
    }

    public static void addJobType(int key, JobType type) {
        jobTypesCache.put(key,type);
    }

    public static boolean isEmpty() {
        return jobsCache.isEmpty() || jobTypesCache.isEmpty() || appliedJobsCache.isEmpty();
    }

    public static List<Job> cachedJobs () {
        return new ArrayList<Job>(jobsCache.values());
    }

    public static List<JobType> cachedJobTypes () {
        return new ArrayList<JobType>(jobTypesCache.values());
    }

    public static List<JobApplication> cachedJobApplications () {
        return new ArrayList<JobApplication>(appliedJobsCache.values());
    }
}
