package com.capstone.jobapplication.jobbridge.entity;

/**
 * Created by Aicun on 6/9/2017.
 */

public class Job {
    private int id;
    private String title;
    private String jobLocation;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String wage;
    private String description;
    private String postDate;
    private String expiryDate;
    private String status;
    private int EmployerId;
    private int JobTypeId;
    private int JobCategoryId;

    private Employer Employer;
    private String jobType;
    private String jobCategory;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String location) {
        this.jobLocation = location;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getWage() {
        return wage;
    }

    public void setWage(String wage) {
        this.wage = wage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getEmployerId() {
        return EmployerId;
    }

    public void setEmployerId(int employerId) {
        this.EmployerId = employerId;
    }

    public int getJobTypeId() {
        return JobTypeId;
    }

    public void setJobTypeId(int jobTypeId) {
        this.JobTypeId = jobTypeId;
    }

    public int getJobCategoryId() {
        return JobCategoryId;
    }

    public void setJobCategoryId(int jobCategoryId) {
        this.JobCategoryId = jobCategoryId;
    }

    public Employer getEmployer() {
        return Employer;
    }

    public void setEmployer(Employer employer) {
        this.Employer = employer;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobCategory() {
        return jobCategory;
    }

    public void setJobCategory(String category) {
        this.jobCategory = category;
    }
}
