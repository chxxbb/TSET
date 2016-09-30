package com.example.chen.tset.Data;

import java.lang.ref.PhantomReference;

/**
 * Created by Administrator on 2016/8/30 0030.
 * 收藏
 */
public class Information {
    private String content;
    private String icon;
    private String title;
    private String time;
    private String cyclopediaId;
    private int id;

    public Information(String content, String icon, String title, String time, String cyclopediaId, int id) {
        this.content = content;
        this.icon = icon;
        this.title = title;
        this.time = time;
        this.cyclopediaId = cyclopediaId;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCyclopediaId() {
        return cyclopediaId;
    }

    public void setCyclopediaId(String cyclopediaId) {
        this.cyclopediaId = cyclopediaId;
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
