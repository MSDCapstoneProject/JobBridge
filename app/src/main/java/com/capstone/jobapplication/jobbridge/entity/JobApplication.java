package com.capstone.jobapplication.jobbridge.entity;

/**
 * Created by Aicun on 6/18/2017.
 */

public class JobApplication {
    private int id;
    private String applicationStatus;
    private String appliedOn;
    private int employerId;
    private int jobSeekerId;
    private int jobId;
    private Employer Employer;
    private Job Job;
    private JobSeeker JobSeeker;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public String getAppliedOn() {
        return appliedOn;
    }

    public void setAppliedOn(String appliedOn) {
        this.appliedOn = appliedOn;
    }

    public int getEmployerId() {
        return employerId;
    }

    public void setEmployerId(int employerId) {
        this.employerId = employerId;
    }

    public int getJobSeekerId() {
        return jobSeekerId;
    }

    public void setJobSeekerId(int jobSeekerId) {
        this.jobSeekerId = jobSeekerId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public com.capstone.jobapplication.jobbridge.entity.Employer getEmployer() {
        return Employer;
    }

    public void setEmployer(com.capstone.jobapplication.jobbridge.entity.Employer employer) {
        Employer = employer;
    }

    public com.capstone.jobapplication.jobbridge.entity.Job getJob() {
        return Job;
    }

    public void setJob(com.capstone.jobapplication.jobbridge.entity.Job job) {
        Job = job;
    }

    public com.capstone.jobapplication.jobbridge.entity.JobSeeker getJobSeeker() {
        return JobSeeker;
    }

    public void setJobSeeker(com.capstone.jobapplication.jobbridge.entity.JobSeeker jobSeeker) {
        JobSeeker = jobSeeker;
    }
}
