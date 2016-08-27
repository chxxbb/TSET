package com.example.chen.tset.Data;

/**
 * Created by Administrator on 2016/8/27 0027.
 */
public class Lecture {
    private int id;
    private String cover;
    private String title;

    public Lecture(int id, String cover, String title) {
        this.id = id;
        this.cover = cover;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Lecture{" +
                "id=" + id +
                ", cover='" + cover + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
