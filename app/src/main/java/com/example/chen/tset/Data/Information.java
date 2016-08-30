package com.example.chen.tset.Data;

import java.lang.ref.PhantomReference;

/**
 * Created by Administrator on 2016/8/30 0030.
 */
public class Information {
    private int id;
    private String title;
    private String imgurl;

    public Information(int id, String title, String imgurl) {
        this.id = id;
        this.title = title;
        this.imgurl = imgurl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    @Override
    public String toString() {
        return "Information{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imgurl='" + imgurl + '\'' +
                '}';
    }
}
