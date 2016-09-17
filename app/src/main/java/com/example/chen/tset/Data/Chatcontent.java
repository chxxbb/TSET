package com.example.chen.tset.Data;

/**
 * Created by Administrator on 2016/9/17 0017.
 */
public class Chatcontent {
    private String content;
    private long time;

    public Chatcontent(String content, long time) {
        this.content = content;
        this.time = time;
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
}
