package com.example.chen.tset.Data.entity;

/**
 * Created by Administrator on 2016/10/29 0029.
 */
public class DiseaseBanner {
    private String site;
    private int id;
    private String cover;
    private String cyclopediaId;

    public DiseaseBanner(String site, int id, String cover, String cyclopediaId) {
        this.site = site;
        this.id = id;
        this.cover = cover;
        this.cyclopediaId = cyclopediaId;
    }


    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
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

    public String getCyclopediaId() {
        return cyclopediaId;
    }

    public void setCyclopediaId(String cyclopediaId) {
        this.cyclopediaId = cyclopediaId;
    }


    @Override
    public String toString() {
        return "DiseaseBanner{" +
                "site='" + site + '\'' +
                ", id=" + id +
                ", cover='" + cover + '\'' +
                ", cyclopediaId='" + cyclopediaId + '\'' +
                '}';
    }
}
