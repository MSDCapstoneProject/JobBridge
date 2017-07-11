package com.capstone.jobapplication.jobbridge.util;

import com.capstone.jobapplication.jobbridge.entity.Job;
import com.capstone.jobapplication.jobbridge.entity.JobApplication;
import com.capstone.jobapplication.jobbridge.entity.JobRating;
import com.capstone.jobapplication.jobbridge.entity.JobType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Aicun on 6/11/2017.
 */

public class CacheData {

    private static HashMap<Integer, Job> jobsCache = new HashMap<Integer, Job>();
    private static HashMap<Integer, JobType> jobTypesCache = new HashMap<>();
    private static HashMap<Integer, JobApplication> appliedJobsCache = new HashMap<>();
    private static HashMap<Integer, JobRating> jobRatingCache = new HashMap<>();

    public static void reSetJobsCache() {
        jobsCache.clear();
    }

    public static void reSetJobTypesCache() {
        jobTypesCache.clear();
    }

    public static void reSetAppliedJobsCache() {
        appliedJobsCache.clear();
    }

    public static Job getJob(int jobId) {
        return jobsCache.get(jobId);
    }

    public static void addJob(int jobId, Job job) {
        jobsCache.put(jobId, job);
    }

    public static JobApplication getJobApplication(int jobApplicationId) {
        return appliedJobsCache.get(jobApplicationId);
    }

    public static void addJobApplication(int jobApplicationId, JobApplication jobApplication) {
        appliedJobsCache.put(jobApplicationId, jobApplication);
    }

    public static JobType getJobType(int jobTypeId) {
        return jobTypesCache.get(jobTypeId);
    }

    public static void addJobType(int jobTypeId, JobType type) {
        jobTypesCache.put(jobTypeId, type);
    }

    public static boolean isEmpty() {
        return jobsCache.isEmpty() || jobTypesCache.isEmpty() || appliedJobsCache.isEmpty();
    }

    public static List<Job> cachedJobs() {
        return new ArrayList<Job>(jobsCache.values());
    }

    public static List<JobType> cachedJobTypes() {
        return new ArrayList<JobType>(jobTypesCache.values());
    }

    public static List<JobApplication> cachedJobApplications() {
        return new ArrayList<JobApplication>(appliedJobsCache.values());
    }

    public static void addJobRating(int key, JobRating jobRating) {
        jobRatingCache.put(key, jobRating);
    }

    public static JobRating getJobRating(int jobId) {
        return jobRatingCache.get(jobId);
    }

    public static void updateOrAddRating(int jobId, int status,int jobSeekerId) {
        JobRating rating = getJobRating(jobId);
        if(rating != null) {
            rating.setStatus(status);
            addJobRating(jobId, rating);
        }
        else {
            rating = new JobRating();
            rating.setJobId(jobId);
            rating.setStatus(status);
            rating.setJobSeekerId(jobSeekerId);
            addJobRating(jobId,rating);
        }
    }

    public static boolean jobIsApplied(int jobId) {
        for(Map.Entry set : appliedJobsCache.entrySet()) {
            JobApplication app = (JobApplication) set.getValue();
            if(app.getJobId() == jobId) {
                return true;
            }
        }
        return false;
    }
}
