package com.example.chen.tset.Data;

/**
 * Created by Administrator on 2016/9/2 0002.
 */
public class Inquiry {
    private String icon;
    private String title;
    private String name;
    private String money;
    private String intro;
    private String section;

    public Inquiry(String icon, String title, String name, String money, String intro, String section) {
        this.icon = icon;
        this.title = title;
        this.name = name;
        this.money = money;
        this.intro = intro;
        this.section = section;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
