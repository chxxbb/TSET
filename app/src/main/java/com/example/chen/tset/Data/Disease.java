package com.example.chen.tset.Data;

/**
 * Created by Administrator on 2016/9/7 0007.
 * 疾病详情
 */
public class Disease {
    private String title;
    private String content;
    private String icon;
    private String uname;
    private String ucontent;
    private String dicon;
    private String dname;
    private String dcontent;
    private String acontent;
    private String bcontent;
    private String section;

    public Disease(String title, String content, String icon, String uname, String ucontent, String dicon, String dname, String dcontent, String acontent, String bcontent, String section) {
        this.title = title;
        this.content = content;
        this.icon = icon;
        this.uname = uname;
        this.ucontent = ucontent;
        this.dicon = dicon;
        this.dname = dname;
        this.dcontent = dcontent;
        this.acontent = acontent;
        this.bcontent = bcontent;
        this.section = section;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUcontent() {
        return ucontent;
    }

    public void setUcontent(String ucontent) {
        this.ucontent = ucontent;
    }

    public String getDicon() {
        return dicon;
    }

    public void setDicon(String dicon) {
        this.dicon = dicon;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDcontent() {
        return dcontent;
    }

    public void setDcontent(String dcontent) {
        this.dcontent = dcontent;
    }

    public String getAcontent() {
        return acontent;
    }

    public void setAcontent(String acontent) {
        this.acontent = acontent;
    }

    public String getBcontent() {
        return bcontent;
    }

    public void setBcontent(String bcontent) {
        this.bcontent = bcontent;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
