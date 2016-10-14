package com.example.chen.tset.Data;

/**
 * Created by Administrator on 2016/10/14 0014.
 */
public class Pharmacyremindinit {
    private String remindDate;
    private String time1;
    private String time2;
    private String time3;
    private String content1;
    private String content2;
    private String content3;
    private String timestamp;

    public Pharmacyremindinit(String remindDate, String time1, String time2, String time3, String content1, String content2, String content3, String timestamp) {
        this.remindDate = remindDate;
        this.time1 = time1;
        this.time2 = time2;
        this.time3 = time3;
        this.content1 = content1;
        this.content2 = content2;
        this.content3 = content3;
        this.timestamp = timestamp;
    }

    public String getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(String remindDate) {
        this.remindDate = remindDate;
    }

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    public String getTime3() {
        return time3;
    }

    public void setTime3(String time3) {
        this.time3 = time3;
    }

    public String getContent1() {
        return content1;
    }

    public void setContent1(String content1) {
        this.content1 = content1;
    }

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
    }

    public String getContent3() {
        return content3;
    }

    public void setContent3(String content3) {
        this.content3 = content3;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Pharmacyremindinit{" +
                "remindDate='" + remindDate + '\'' +
                ", time1='" + time1 + '\'' +
                ", time2='" + time2 + '\'' +
                ", time3='" + time3 + '\'' +
                ", content1='" + content1 + '\'' +
                ", content2='" + content2 + '\'' +
                ", content3='" + content3 + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
