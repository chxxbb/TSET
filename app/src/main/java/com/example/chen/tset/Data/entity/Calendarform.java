package com.example.chen.tset.Data.entity;

/**
 * Created by Administrator on 2016/10/5 0005.
 */
public class Calendarform {
    private int id;
    private String mark;
    private String date;

    public Calendarform(int id, String mark, String date) {
        this.id = id;
        this.mark = mark;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Calendarform{" +
                "id=" + id +
                ", mark='" + mark + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
