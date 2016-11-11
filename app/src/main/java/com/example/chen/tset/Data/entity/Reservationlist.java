package com.example.chen.tset.Data.entity;

/**
 * Created by Administrator on 2016/9/7 0007.
 * 预约详情
 */
public class Reservationlist {
    private String city;
    private String section;
    private String title;
    private String reservationDate;
    private String name;
    private String phone;
    private String money;
    private String orderCode;
    private String orderStatus;

    public Reservationlist(String city, String section, String title, String reservationDate, String name, String phone, String money, String orderCode, String orderStatus) {
        this.city = city;
        this.section = section;
        this.title = title;
        this.reservationDate = reservationDate;
        this.name = name;
        this.phone = phone;
        this.money = money;
        this.orderCode = orderCode;
        this.orderStatus = orderStatus;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
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
}
