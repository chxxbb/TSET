package com.example.chen.tset.Data;

/**
 * Created by Administrator on 2016/9/26 0026.
 */
public class CalendarSign {
    private String time;
    private int state;

    public CalendarSign(String time, int state) {
        this.time = time;
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
