package com.example.chen.tset.Data;

/**
 * Created by Administrator on 2016/9/27 0027.
 */
public class MyDoctor {
    private int doctorId;
    private String doctorIcon;
    private String chatCost;
    private String doctorName;
    private String adept;
    private String doctorSection;
    private String doctorTitle;
    private String doctorUserName;

    public MyDoctor(int doctorId, String doctorIcon, String chatCost, String doctorName, String adept, String doctorSection, String doctorTitle, String doctorUserName) {
        this.doctorId = doctorId;
        this.doctorIcon = doctorIcon;
        this.chatCost = chatCost;
        this.doctorName = doctorName;
        this.adept = adept;
        this.doctorSection = doctorSection;
        this.doctorTitle = doctorTitle;
        this.doctorUserName = doctorUserName;
    }

    public String getDoctorUserName() {
        return doctorUserName;
    }

    public void setDoctorUserName(String doctorUserName) {
        this.doctorUserName = doctorUserName;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorIcon() {
        return doctorIcon;
    }

    public void setDoctorIcon(String doctorIcon) {
        this.doctorIcon = doctorIcon;
    }

    public String getChatCost() {
        return chatCost;
    }

    public void setChatCost(String chatCost) {
        this.chatCost = chatCost;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getAdept() {
        return adept;
    }

    public void setAdept(String adept) {
        this.adept = adept;
    }

    public String getDoctorSection() {
        return doctorSection;
    }

    public void setDoctorSection(String doctorSection) {
        this.doctorSection = doctorSection;
    }

    public String getDoctorTitle() {
        return doctorTitle;
    }

    public void setDoctorTitle(String doctorTitle) {
        this.doctorTitle = doctorTitle;
    }
}
