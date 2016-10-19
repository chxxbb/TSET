package com.example.chen.tset.Data;

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
    private String seniority1;//资历1

    private String seniority2;//资历2

    private String seniority3;//资历3

    private String seniority4;//资历4

    private String username;

    public Doctor(String icon, String title, String name, String chatCost, String callCost, String adept, String hospital, String section, int commentCount, String seniority1, String seniority2, String seniority3, String seniority4, String username) {
        this.icon = icon;
        this.title = title;
        this.name = name;
        this.chatCost = chatCost;
        this.callCost = callCost;
        this.adept = adept;
        this.hospital = hospital;
        this.section = section;
        this.commentCount = commentCount;
        this.seniority1 = seniority1;
        this.seniority2 = seniority2;
        this.seniority3 = seniority3;
        this.seniority4 = seniority4;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getSeniority1() {
        return seniority1;
    }

    public void setSeniority1(String seniority1) {
        this.seniority1 = seniority1;
    }

    public String getSeniority2() {
        return seniority2;
    }

    public void setSeniority2(String seniority2) {
        this.seniority2 = seniority2;
    }

    public String getSeniority3() {
        return seniority3;
    }

    public void setSeniority3(String seniority3) {
        this.seniority3 = seniority3;
    }

    public String getSeniority4() {
        return seniority4;
    }

    public void setSeniority4(String seniority4) {
        this.seniority4 = seniority4;
    }
}
