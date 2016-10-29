package com.example.chen.tset.Data;

/**
 * Created by Administrator on 2016/10/29 0029.
 */
public class DiseaseBanner {
    private String site;
    private int id;
    private String cover;

    public DiseaseBanner(String site, int id, String cover) {
        this.site = site;
        this.id = id;
        this.cover = cover;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Override
    public String toString() {
        return "DiseaseBanner{" +
                "site='" + site + '\'' +
                ", id=" + id +
                ", cover='" + cover + '\'' +
                '}';
    }
}
