package com.example.chen.tset.Data.entity;

/**
 * Created by Administrator on 2016/8/30 0030.
 * 资讯
 */
public class Consult {
    private int id;
    private String icon;
    private String title;
    private String content;
    private String time;
    private int categoryId;

    public Consult(int id, String icon, String title, String content, String time, int categoryId) {
        this.id = id;
        this.icon = icon;
        this.title = title;
        this.content = content;
        this.time = time;
        this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "Consult{" +
                "id=" + id +
                ", icon='" + icon + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", categoryId=" + categoryId +
                '}';
    }
}
