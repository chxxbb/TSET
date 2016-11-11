package com.example.chen.tset.Data.entity;

/**
 * Created by Administrator on 2016/9/6 0006.
 * 医生评论
 */
public class Doctorcomment {
    private String userName;
    private String content;
    private String userIcon;
    private String time;
    private int id;

    public Doctorcomment(String userName, String content, String userIcon, String time, int id) {
        this.userName = userName;
        this.content = content;
        this.userIcon = userIcon;
        this.time = time;
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
