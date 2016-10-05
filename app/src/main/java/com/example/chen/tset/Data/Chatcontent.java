package com.example.chen.tset.Data;

import java.io.File;

/**
 * Created by Administrator on 2016/9/17 0017.
 */
public class Chatcontent {
    private String content;
    private long time;
    private String file;
    private String masterfile;
    private String username;
    private String myname;

    public Chatcontent(String content, long time, String file, String masterfile, String username, String myname) {
        this.content = content;
        this.time = time;
        this.file = file;
        this.masterfile = masterfile;
        this.username = username;
        this.myname = myname;
    }

    public String getMyname() {
        return myname;
    }

    public void setMyname(String myname) {
        this.myname = myname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMasterfile() {
        return masterfile;
    }

    public void setMasterfile(String masterfile) {
        this.masterfile = masterfile;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Chatcontent{" +
                "content='" + content + '\'' +
                ", time=" + time +
                ", file='" + file + '\'' +
                ", masterfile='" + masterfile + '\'' +
                ", username='" + username + '\'' +
                ", myname='" + myname + '\'' +
                '}';
    }
}
