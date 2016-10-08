package com.example.chen.tset.Data;

/**
 * Created by Administrator on 2016/9/6 0006.
 * 用药提醒
 */
public class Pharmacyremind {
    private String username;
    private String startdate;
    private String overdate;
    private String time1;
    private String content1;
    private String time2;
    private String content2;
    private String time3;
    private String content3;


    public Pharmacyremind(String username, String startdate, String overdate, String time1, String content1, String time2, String content2, String time3, String content3) {
        this.username = username;
        this.startdate = startdate;
        this.overdate = overdate;
        this.time1 = time1;
        this.content1 = content1;
        this.time2 = time2;
        this.content2 = content2;
        this.time3 = time3;
        this.content3 = content3;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getOverdate() {
        return overdate;
    }

    public void setOverdate(String overdate) {
        this.overdate = overdate;
    }

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getContent1() {
        return content1;
    }

    public void setContent1(String content1) {
        this.content1 = content1;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
    }

    public String getTime3() {
        return time3;
    }

    public void setTime3(String time3) {
        this.time3 = time3;
    }

    public String getContent3() {
        return content3;
    }

    public void setContent3(String content3) {
        this.content3 = content3;
    }

    @Override
    public String toString() {
        return "Pharmacyremind{" +
                "username='" + username + '\'' +
                ", startdate='" + startdate + '\'' +
                ", overdate='" + overdate + '\'' +
                ", time1='" + time1 + '\'' +
                ", content1='" + content1 + '\'' +
                ", time2='" + time2 + '\'' +
                ", content2='" + content2 + '\'' +
                ", time3='" + time3 + '\'' +
                ", content3='" + content3 + '\'' +
                '}';
    }
}
