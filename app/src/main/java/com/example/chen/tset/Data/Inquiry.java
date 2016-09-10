package com.example.chen.tset.Data;

/**
 * Created by Administrator on 2016/9/2 0002.
 */
public class Inquiry {
    private String icon;
    private String title;
    private String name;
    private String money;
    private String adept;
    private String section;
    private String hospital;
    private String id;

    public Inquiry(String icon, String title, String name, String money, String adept, String section, String hospital, String id) {
        this.icon = icon;
        this.title = title;
        this.name = name;
        this.money = money;
        this.adept = adept;
        this.section = section;
        this.hospital = hospital;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
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
}
