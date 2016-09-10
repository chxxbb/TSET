package com.example.chen.tset.Data;

/**
 * Created by Administrator on 2016/9/6 0006.
 */
public class Doctorcomment {
    private String name;
    private String content;
    private String usericon;
    private String time;

    public Doctorcomment(String name, String content, String usericon, String time) {
        this.name = name;
        this.content = content;
        this.usericon = usericon;
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

    public String getUsericon() {
        return usericon;
    }

    public void setUsericon(String usericon) {
        this.usericon = usericon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
