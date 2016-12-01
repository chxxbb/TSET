package com.example.chen.tset.Data.entity;

/**
 * Created by Administrator on 2016/9/7 0007.
 * 资讯详情
 */
public class Consultparticulars {
    private String content;
    private String cover;
    private String title;
    private String time;

    public Consultparticulars(String content, String cover, String title, String time) {
        this.content = content;
        this.cover = cover;
        this.title = title;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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


    @Override
    public String toString() {
        return "Consultparticulars{" +
                "content='" + content + '\'' +
                ", cover='" + cover + '\'' +
                ", title='" + title + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
