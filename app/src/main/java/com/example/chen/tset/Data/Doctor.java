package com.example.chen.tset.Data;

/**
 * Created by Administrator on 2016/9/6 0006.
 */
public class Doctor {
    private String bis;
    private String icon;
    private String bit;
    private String bioo;
    private String title;
    private String name;
    private String chatmoney;
    private String callmoney;
    private String adept;
    private String hospital;
    private String section;
    private String bif;
    private int sum;

    public Doctor(String bis, String icon, String bit, String bioo, String title, String name, String chatmoney, String callmoney, String adept, String hospital, String section, String bif, int sum) {
        this.bis = bis;
        this.icon = icon;
        this.bit = bit;
        this.bioo = bioo;
        this.title = title;
        this.name = name;
        this.chatmoney = chatmoney;
        this.callmoney = callmoney;
        this.adept = adept;
        this.hospital = hospital;
        this.section = section;
        this.bif = bif;
        this.sum = sum;
    }

    public String getBis() {
        return bis;
    }

    public void setBis(String bis) {
        this.bis = bis;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBit() {
        return bit;
    }

    public void setBit(String bit) {
        this.bit = bit;
    }

    public String getBioo() {
        return bioo;
    }

    public void setBioo(String bioo) {
        this.bioo = bioo;
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

    public String getChatmoney() {
        return chatmoney;
    }

    public void setChatmoney(String chatmoney) {
        this.chatmoney = chatmoney;
    }

    public String getCallmoney() {
        return callmoney;
    }

    public void setCallmoney(String callmoney) {
        this.callmoney = callmoney;
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

    public String getBif() {
        return bif;
    }

    public void setBif(String bif) {
        this.bif = bif;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
