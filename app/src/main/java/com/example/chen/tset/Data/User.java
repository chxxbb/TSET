package com.example.chen.tset.Data;

import android.graphics.Bitmap;

public class User {

    private Integer id = null;//用户id

    private String phone = null;//手机号

    private String name = null;//名称

    private String password = null;//密码

    private String sex = null;//性别

    private Integer age = null;//年龄

    private String icon = null;//头像

    private String qq = null;//QQ号

    private String weibo = null;//微博

    private String wechat = null;//微信

    private String email = null;//邮箱

    private Integer role = null;//角色

    private Bitmap bitmap_icon = null;//头像

    @Override
    public String toString() {
        return "User [id=" + id + ", phone=" + phone + ", name=" + name + ", password=" + password + ", sex=" + sex
                + ", age=" + age + ", icon=" + icon + ", qq=" + qq + ", weibo=" + weibo + ", wechat=" + wechat
                + ", email=" + email + ", role=" + role + "]";
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Bitmap getBitmap_icon() {
        return bitmap_icon;
    }

    public void setBitmap_icon(Bitmap bitmap_icon) {
        this.bitmap_icon = bitmap_icon;
    }

    public void setUser(User user) {
        id = user.getId();
        phone = user.getPhone();
        name = user.getName();
        password = user.getPassword();
        sex = user.getSex();
        age = user.getAge();
        icon = user.getIcon();
        qq = user.getQq();
        weibo = user.getWeibo();
        wechat = user.getWechat();
        email = user.getEmail();
        role = user.getRole();
        bitmap_icon = user.getBitmap_icon();
    }

    public User(){

    }

    public User(User user){
        id = user.getId();
        phone = user.getPhone();
        name = user.getName();
        password = user.getPassword();
        sex = user.getSex();
        age = user.getAge();
        icon = user.getIcon();
        qq = user.getQq();
        weibo = user.getWeibo();
        wechat = user.getWechat();
        email = user.getEmail();
        role = user.getRole();
        bitmap_icon = user.getBitmap_icon();
    }

}
