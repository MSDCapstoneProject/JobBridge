package com.capstone.jobapplication.jobbridge.entity;

import com.capstone.jobapplication.jobbridge.util.StringUtil;

/**
 * Created by Aicun on 6/9/2017.
 */

public class Job {
    private int id;
    private String title;
    private transient String jobLocation;
    private transient String jobAddress;
    private String street;
    private String city;
    private String province;
    private String country;
    private String postalCode;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String wage;
    private String description;
    private String postDate;
    private String expiryDate;
    private String status;
    private int employerId;
    private int jobTypeId;
    private int jobCategoryId;

    private Employer employer;
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


    public String getJobAddress() {
        return jobAddress;
    }

    public void setJobAddress() {
        this.jobAddress = StringUtil.formatAddress(street,city,province,country,postalCode);
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
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
        return employerId;
    }

    public void setEmployerId(int employerId) {
        this.employerId = employerId;
    }

    public int getJobTypeId() {
        return jobTypeId;
    }

    public void setJobTypeId(int jobTypeId) {
        this.jobTypeId = jobTypeId;
    }

    public int getJobCategoryId() {
        return jobCategoryId;
    }

    public void setJobCategoryId(int jobCategoryId) {
        this.jobCategoryId = jobCategoryId;
    }

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
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
