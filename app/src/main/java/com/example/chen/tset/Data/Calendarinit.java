package com.example.chen.tset.Data;

/**
 * Created by Administrator on 2016/10/13 0013.
 */
public class Calendarinit {
    private String content;
    private String time3;
    private String time2;
    private String time1;
    private int registrationId;
    private String tag;
    private int remindId;
    private String health_id;

    public Calendarinit(String content, String time3, String time2, String time1, int registrationId, String tag, int remindId, String health_id) {
        this.content = content;
        this.time3 = time3;
        this.time2 = time2;
        this.time1 = time1;
        this.registrationId = registrationId;
        this.tag = tag;
        this.remindId = remindId;
        this.health_id = health_id;
    }

    public String getHealth_id() {
        return health_id;
    }

    public void setHealth_id(String health_id) {
        this.health_id = health_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime3() {
        return time3;
    }

    public void setTime3(String time3) {
        this.time3 = time3;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getRemindId() {
        return remindId;
    }

    public void setRemindId(int remindId) {
        this.remindId = remindId;
    }

    @Override
    public String toString() {
        return "Calendarinit{" +
                "content='" + content + '\'' +
                ", time1='" + time1 + '\'' +
                ", time2='" + time2 + '\'' +
                ", time3='" + time3 + '\'' +
                ", tag='" + tag + '\'' +
                ", registrationId=" + registrationId +
                ", remindId=" + remindId +
                '}';
    }
}
