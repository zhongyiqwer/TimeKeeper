package com.example.timekeeper.dao;

/**
 * Created by Administrator on 2018/5/15.
 */

public class User {
    private int id;
    private String name;
    private int level;
    private String email;
    private int [] actions;

    public User() {
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int[] getActions() {
        return actions;
    }

    public void setActions(int[] actions) {
        this.actions = actions;
    }

}
