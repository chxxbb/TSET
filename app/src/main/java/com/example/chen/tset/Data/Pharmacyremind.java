package com.example.chen.tset.Data;

/**
 * Created by Administrator on 2016/9/6 0006.
 */
public class Pharmacyremind {
    private String amcontent;
    private String nighttime;
    private String pmcontent;
    private String startendtime;
    private String nightcontent;
    private String pmtime;
    private String amtime;

    public Pharmacyremind(String amcontent, String nighttime, String pmcontent, String startendtime, String nightcontent, String pmtime, String amtime) {
        this.amcontent = amcontent;
        this.nighttime = nighttime;
        this.pmcontent = pmcontent;
        this.startendtime = startendtime;
        this.nightcontent = nightcontent;
        this.pmtime = pmtime;
        this.amtime = amtime;
    }

    public String getAmcontent() {
        return amcontent;
    }

    public void setAmcontent(String amcontent) {
        this.amcontent = amcontent;
    }

    public String getNighttime() {
        return nighttime;
    }

    public void setNighttime(String nighttime) {
        this.nighttime = nighttime;
    }

    public String getPmcontent() {
        return pmcontent;
    }

    public void setPmcontent(String pmcontent) {
        this.pmcontent = pmcontent;
    }

    public String getStartendtime() {
        return startendtime;
    }

    public void setStartendtime(String startendtime) {
        this.startendtime = startendtime;
    }

    public String getNightcontent() {
        return nightcontent;
    }

    public void setNightcontent(String nightcontent) {
        this.nightcontent = nightcontent;
    }

    public String getPmtime() {
        return pmtime;
    }

    public void setPmtime(String pmtime) {
        this.pmtime = pmtime;
    }

    public String getAmtime() {
        return amtime;
    }

    public void setAmtime(String amtime) {
        this.amtime = amtime;
    }
}
