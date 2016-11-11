package com.example.chen.tset.Data.entity;

/**
 * Created by Administrator on 2016/11/3 0003.
 */
public class FindAllHot {
    private String site;
    private String id;
    private String title;

    public FindAllHot(String site, String id, String title) {
        this.site = site;
        this.id = id;
        this.title = title;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "FindAllHot{" +
                "site='" + site + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
