package com.example.chen.tset.Data;

/**
 * Created by Administrator on 2016/9/7 0007.
 * 疾病详情
 */
public class Disease {
    private Integer id;// 疾病id

    private String name;// 疾病名

    private String bio;// 疾病简介

    private String userIcon;// 用户头像

    private String userName;// 用户名称

    private String userPutQuestion;// 用户的提问

    private String doctorIcon;// 医生头像

    private String doctorName;// 医生名称

    private String doctorAnswerQuestion;// 医生的回答

    private String symptom;// 症状

    private String cure;// 治疗

    private String prompt;// 温馨提示

    private String sectionName;// 科室id

    public Disease(Integer id, String name, String bio, String userIcon, String userName, String userPutQuestion, String doctorIcon, String doctorName, String doctorAnswerQuestion, String symptom, String cure, String prompt, String sectionName) {
        this.id = id;
        this.name = name;
        this.bio = bio;
        this.userIcon = userIcon;
        this.userName = userName;
        this.userPutQuestion = userPutQuestion;
        this.doctorIcon = doctorIcon;
        this.doctorName = doctorName;
        this.doctorAnswerQuestion = doctorAnswerQuestion;
        this.symptom = symptom;
        this.cure = cure;
        this.prompt = prompt;
        this.sectionName = sectionName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPutQuestion() {
        return userPutQuestion;
    }

    public void setUserPutQuestion(String userPutQuestion) {
        this.userPutQuestion = userPutQuestion;
    }

    public String getDoctorIcon() {
        return doctorIcon;
    }

    public void setDoctorIcon(String doctorIcon) {
        this.doctorIcon = doctorIcon;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorAnswerQuestion() {
        return doctorAnswerQuestion;
    }

    public void setDoctorAnswerQuestion(String doctorAnswerQuestion) {
        this.doctorAnswerQuestion = doctorAnswerQuestion;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getCure() {
        return cure;
    }

    public void setCure(String cure) {
        this.cure = cure;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
}
