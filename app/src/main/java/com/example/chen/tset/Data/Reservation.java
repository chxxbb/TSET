package com.example.chen.tset.Data;

/**
 * Created by Administrator on 2016/9/6 0006.
 */
public class Reservation {
    private String time;
    private String order;
    private String status;
    private String money;
    private int id;

    public Reservation(String time, String order, String status, String money, int id) {
        this.time = time;
        this.order = order;
        this.status = status;
        this.money = money;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
