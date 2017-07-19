package com.capstone.jobapplication.jobbridge.entity;

/**
 * Created by Aicun on 7/17/2017.
 */

public class Topic {
    private int id;
    private String description;
    private String interalCode;
    private int topicGroupId;
    private TopicGroup topicGroup;

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

    public String getInteralCode() {
        return interalCode;
    }

    public void setInteralCode(String interalCode) {
        this.interalCode = interalCode;
    }

    public int getTopicGroupId() {
        return topicGroupId;
    }

    public void setTopicGroupId(int topicGroupId) {
        this.topicGroupId = topicGroupId;
    }

    public TopicGroup getTopicGroup() {
        return topicGroup;
    }

    public void setTopicGroup(TopicGroup topicGroup) {
        this.topicGroup = topicGroup;
    }
}
