package com.example.chen.tset.Data;

/**
 * Created by Administrator on 2016/9/7 0007.
 * 资讯详情
 */
public class Consultparticulars {
    private String content;
    private String icon;
    private String title;
    private String time;

    public Consultparticulars(String content, String icon, String title, String time) {
        this.content = content;
        this.icon = icon;
        this.title = title;
        this.time = time;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
