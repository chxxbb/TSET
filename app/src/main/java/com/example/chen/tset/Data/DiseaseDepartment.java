package com.example.chen.tset.Data;

/**
 * Created by Administrator on 2016/8/31 0031.
 */
public class DiseaseDepartment {
    private String icon;
    private String name;

    public DiseaseDepartment(String icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DiseaseDepartment{" +
                "icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}