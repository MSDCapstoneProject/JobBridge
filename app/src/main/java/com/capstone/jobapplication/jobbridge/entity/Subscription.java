package com.capstone.jobapplication.jobbridge.entity;

/**
 * Created by Aicun on 7/17/2017.
 */

public class Subscription {
    private int id;
    private int topicId;
    private Topic topic;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}
