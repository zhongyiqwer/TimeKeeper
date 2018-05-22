package com.example.timekeeper.dao;

/**
 * Created by Administrator on 2018/5/15.
 */

public class Action {
    private int id;
    private String name;
    private int timeLen;
    private String des;
    private int cretor;
    private String [] times;
    private int suit;
    private int [] partPerson;

    public Action() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeLen() {
        return timeLen;
    }

    public void setTimeLen(int timeLen) {
        this.timeLen = timeLen;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getCretor() {
        return cretor;
    }

    public void setCretor(int cretor) {
        this.cretor = cretor;
    }

    public String[] getTimes() {
        return times;
    }

    public void setTimes(String[] times) {
        this.times = times;
    }

    public int getSuit() {
        return suit;
    }

    public void setSuit(int suit) {
        this.suit = suit;
    }

    public int[] getPartPerson() {
        return partPerson;
    }

    public void setPartPerson(int[] partPerson) {
        this.partPerson = partPerson;
    }
}
