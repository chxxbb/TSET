package com.example.chen.tset.Data;

/**
 * Created by Administrator on 2016/9/7 0007.
 * 预约详情
 */
public class Reservationlist {
    private String doctor_name;
    private String icon;
    private String patient_name;
    private String section;
    private String order_no;
    private String content;
    private String title;
    private String appointment_time;
    private String valid_time;
    private String address;
    private String patient_phone;
    private String money;
    private String hospital;

    public Reservationlist(String doctor_name, String icon, String patient_name, String section, String order_no, String content, String title, String appointment_time, String valid_time, String address, String patient_phone, String money, String hospital) {
        this.doctor_name = doctor_name;
        this.icon = icon;
        this.patient_name = patient_name;
        this.section = section;
        this.order_no = order_no;
        this.content = content;
        this.title = title;
        this.appointment_time = appointment_time;
        this.valid_time = valid_time;
        this.address = address;
        this.patient_phone = patient_phone;
        this.money = money;
        this.hospital = hospital;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAppointment_time() {
        return appointment_time;
    }

    public void setAppointment_time(String appointment_time) {
        this.appointment_time = appointment_time;
    }

    public String getValid_time() {
        return valid_time;
    }

    public void setValid_time(String valid_time) {
        this.valid_time = valid_time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPatient_phone() {
        return patient_phone;
    }

    public void setPatient_phone(String patient_phone) {
        this.patient_phone = patient_phone;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }
}
