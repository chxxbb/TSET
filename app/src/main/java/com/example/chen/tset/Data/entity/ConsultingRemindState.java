package com.example.chen.tset.Data.entity;

/**
 * Created by Administrator on 2016/10/12 0012.
 */
public class ConsultingRemindState {
    private  int id;
    private String date;
    private int registrationId;
    private int remindId;
    private int healthId;
    private String userId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
    }

    public int getRemindId() {
        return remindId;
    }

    public void setRemindId(int remindId) {
        this.remindId = remindId;
    }

    public int getHealthId() {
        return healthId;
    }

    public void setHealthId(int healthId) {
        this.healthId = healthId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ConsultingRemindState(int id, String date, int registrationId, int remindId, int healthId, String userId) {

        this.id = id;
        this.date = date;
        this.registrationId = registrationId;
        this.remindId = remindId;
        this.healthId = healthId;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ConsultingRemindState{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", registrationId=" + registrationId +
                ", remindId=" + remindId +
                ", healthId=" + healthId +
                ", userId=" + userId +
                '}';
    }
}
