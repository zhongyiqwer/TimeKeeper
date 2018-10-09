package com.example.timekeeper.dao;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/15.
 */

public class Action implements Serializable{
    private String id;
    //活动名称
    private String activityName;
    //活动创建者
    private String creator;
    private String participant;
    private String time;
    //活动持续时间
    private String lastingTime;
    //活动描述
    private String description;
    //活动状态
    private String state;
    //活动完成时间
    private String time_compute;
    //活动开始时间
    private String start_time;
    //活动类型
    private String activityType;
    //活动规模
    private String activityNum;
    //活动地点
    private String activityPlace;
    //活动选择日期
    private String activitySelectDate;

    @Override
    public String toString() {
        return "Action{" +
                "id='" + id + '\'' +
                ", activityName='" + activityName + '\'' +
                ", creater='" + creator + '\'' +
                ", participant='" + participant + '\'' +
                ", time='" + time + '\'' +
                ", lastingTime='" + lastingTime + '\'' +
                ", description='" + description + '\'' +
                ", state='" + state + '\'' +
                ", time_compute='" + time_compute + '\'' +
                ", start_time='" + start_time + '\'' +
                ", activityType='" + activityType + '\'' +
                ", activityNum='" + activityNum + '\'' +
                ", activityPlace='" + activityPlace + '\'' +
                ", activitySelectDate='" + activitySelectDate + '\'' +
                '}';
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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

    public String getTime_compute() {
        return time_compute;
    }

    public void setTime_compute(String time_compute) {
        this.time_compute = time_compute;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityNum() {
        return activityNum;
    }

    public void setActivityNum(String activityNum) {
        this.activityNum = activityNum;
    }

    public String getActivityPlace() {
        return activityPlace;
    }

    public void setActivityPlace(String activityPlace) {
        this.activityPlace = activityPlace;
    }

    public String getActivitySelectDate() {
        return activitySelectDate;
    }

    public void setActivitySelectDate(String activitySelectDate) {
        this.activitySelectDate = activitySelectDate;
    }
}
