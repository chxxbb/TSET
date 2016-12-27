package com.example.chen.tset.Data.entity;

/**
 * Created by Administrator on 2016/12/27 0027.
 */
public class Launch {
    private String img;

    private String date;

    public Launch(String img, String date) {
        this.img = img;
        this.date = date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Launch{" +
                "img='" + img + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
