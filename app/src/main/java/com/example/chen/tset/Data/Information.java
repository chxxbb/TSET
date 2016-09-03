package com.example.chen.tset.Data;

import java.lang.ref.PhantomReference;

/**
 * Created by Administrator on 2016/8/30 0030.
 */
public class Information {
    private String content;
    private String icon;
    private String title;
    private String time;

    public Information(String content, String icon, String title, String time) {
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
