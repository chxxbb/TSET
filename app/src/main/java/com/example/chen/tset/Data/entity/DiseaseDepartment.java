package com.example.chen.tset.Data.entity;

/**
 * Created by Administrator on 2016/8/31 0031.
 * 疾病列表
 */
public class DiseaseDepartment {
    private String icon1;
    private String name;
    private String icon2;

    public DiseaseDepartment(String icon1, String name, String icon2) {
        this.icon1 = icon1;
        this.name = name;
        this.icon2 = icon2;
    }

    public String getIcon1() {
        return icon1;
    }

    public void setIcon1(String icon1) {
        this.icon1 = icon1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon2() {
        return icon2;
    }

    public void setIcon2(String icon2) {
        this.icon2 = icon2;
    }
}
