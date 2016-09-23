package com.example.chen.tset.Data;

/**
 * Created by Administrator on 2016/9/23 0023.
 * 保存在本地用户信息
 */
public class Userinfo {
    Integer id;
    String phone;
    String name;
    String password;
    String gender;
    String icon;

    public Userinfo(Integer id, String phone, String name, String password, String gender, String icon) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.password = password;
        this.gender = gender;
        this.icon = icon;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Userinfo{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
