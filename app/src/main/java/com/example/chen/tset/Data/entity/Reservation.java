package com.example.chen.tset.Data.entity;

/**
 * Created by Administrator on 2016/9/6 0006.
 * 预约列表
 */
public class Reservation {
    private String reservationDate;
    private String orderCode;
    private String orderStatus;
    private String money;
    private int id;

    public Reservation(String reservationDate, String orderCode, String orderStatus, String money, int id) {
        this.reservationDate = reservationDate;
        this.orderCode = orderCode;
        this.orderStatus = orderStatus;
        this.money = money;
        this.id = id;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
