package com.example.chen.tset.Data.entity;

/**
 * Created by Administrator on 2016/9/6 0006.
 * 医生列表
 */
public class Doctor {
    private String icon;
    private String title;
    private String name;
    private String chatCost;
    private String callCost;
    private String adept;
    private String hospital;
    private String section;
    private int commentCount;
    private String seniority;
    private String username;

    public Doctor(String icon, String title, String name, String chatCost, String callCost, String adept, String hospital, String section, int commentCount, String seniority, String username) {
        this.icon = icon;
        this.title = title;
        this.name = name;
        this.chatCost = chatCost;
        this.callCost = callCost;
        this.adept = adept;
        this.hospital = hospital;
        this.section = section;
        this.commentCount = commentCount;
        this.seniority = seniority;
        this.username = username;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChatCost() {
        return chatCost;
    }

    public void setChatCost(String chatCost) {
        this.chatCost = chatCost;
    }

    public String getCallCost() {
        return callCost;
    }

    public void setCallCost(String callCost) {
        this.callCost = callCost;
    }

    public String getAdept() {
        return adept;
    }

    public void setAdept(String adept) {
        this.adept = adept;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getSeniority() {
        return seniority;
    }

    public void setSeniority(String seniority) {
        this.seniority = seniority;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
