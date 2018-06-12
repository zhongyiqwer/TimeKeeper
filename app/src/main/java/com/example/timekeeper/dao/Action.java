package com.example.timekeeper.dao;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/15.
 */

public class Action implements Serializable{
    private String id;
    private String activityName;
    private String creater;
    private String participant;
    private String time;
    private String lastingTime;
    private String description;
    private String state;
    private String time_compute;
    private String start_time;

    public Action() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLastingTime() {
        return lastingTime;
    }

    public void setLastingTime(String lastingTime) {
        this.lastingTime = lastingTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime_campute() {
        return time_compute;
    }

    public void setTime_campute(String time_campute) {
        this.time_compute = time_campute;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    @Override
    public String toString() {
        return "Action{" +
                "id='" + id + '\'' +
                ", activityName='" + activityName + '\'' +
                ", creater='" + creater + '\'' +
                ", participant='" + participant + '\'' +
                ", time='" + time + '\'' +
                ", lastingTime='" + lastingTime + '\'' +
                ", description='" + description + '\'' +
                ", state='" + state + '\'' +
                ", time_campute='" + time_compute + '\'' +
                ", start_time='" + start_time + '\'' +
                '}';
    }
}
