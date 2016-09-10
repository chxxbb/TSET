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
    private String id;

    public Information(String content, String icon, String title, String time, String id) {
        this.content = content;
        this.icon = icon;
        this.title = title;
        this.time = time;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
