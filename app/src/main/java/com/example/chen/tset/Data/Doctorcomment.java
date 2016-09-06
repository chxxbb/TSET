package com.example.chen.tset.Data;

/**
 * Created by Administrator on 2016/9/6 0006.
 */
public class Doctorcomment {
    private String name;
    private String content;
    private String icon;
    private String time;

    public Doctorcomment(String name, String content, String icon, String time) {
        this.name = name;
        this.content = content;
        this.icon = icon;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
