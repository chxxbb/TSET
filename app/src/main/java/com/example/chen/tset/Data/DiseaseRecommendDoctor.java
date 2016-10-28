package com.example.chen.tset.Data;

/**
 * Created by Administrator on 2016/10/28 0028.
 *
 * 推荐医生实体类
 */
public class DiseaseRecommendDoctor {
    private String icon;
    private String title;
    private String name;
    private String chatCost;
    private String adept;
    private String section;
    private String hospital;
    private int id;
    private String username;

    public DiseaseRecommendDoctor(String icon, String title, String name, String chatCost, String adept, String section, String hospital, int id, String username) {
        this.icon = icon;
        this.title = title;
        this.name = name;
        this.chatCost = chatCost;
        this.adept = adept;
        this.section = section;
        this.hospital = hospital;
        this.id = id;
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

    public String getAdept() {
        return adept;
    }

    public void setAdept(String adept) {
        this.adept = adept;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "DiseaseRecommendDoctor{" +
                "icon='" + icon + '\'' +
                ", title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", chatCost='" + chatCost + '\'' +
                ", adept='" + adept + '\'' +
                ", section='" + section + '\'' +
                ", hospital='" + hospital + '\'' +
                ", id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
