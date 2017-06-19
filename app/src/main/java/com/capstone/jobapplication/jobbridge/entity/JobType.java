package com.capstone.jobapplication.jobbridge.entity;

/**
 * Created by Aicun on 6/9/2017.
 */

public class JobType {
    private int id;
    private String description;
    private String internalCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInternalCode() {
        return internalCode;
    }

    public void setInternalCode(String internalCode) {
        this.internalCode = internalCode;
    }
}
