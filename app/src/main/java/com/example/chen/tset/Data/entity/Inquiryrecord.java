package com.example.chen.tset.Data.entity;

/**
 * Created by Administrator on 2016/10/7 0007.
 */
public class Inquiryrecord {
    private String username;
    private String doctorid;
    private String id;
    private String doctorname;
    private String doctoricon;
    private String time;
    private String state;

    public Inquiryrecord(String username, String doctorid, String id, String doctorname, String doctoricon, String time, String state) {
        this.username = username;
        this.doctorid = doctorid;
        this.id = id;
        this.doctorname = doctorname;
        this.doctoricon = doctoricon;
        this.time = time;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDoctorid() {
        return doctorid;
    }

    public void setDoctorid(String doctorid) {
        this.doctorid = doctorid;
    }

    public String getDoctorname() {
        return doctorname;
    }

    public void setDoctorname(String doctorname) {
        this.doctorname = doctorname;
    }

    public String getDoctoricon() {
        return doctoricon;
    }

    public void setDoctoricon(String doctoricon) {
        this.doctoricon = doctoricon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Inquiryrecord{" +
                "username='" + username + '\'' +
                ", doctorid='" + doctorid + '\'' +
                ", doctorname='" + doctorname + '\'' +
                ", doctoricon='" + doctoricon + '\'' +
                ", time='" + time + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
