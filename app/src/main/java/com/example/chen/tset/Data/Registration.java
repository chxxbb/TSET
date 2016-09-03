package com.example.chen.tset.Data;

/**
 * Created by Administrator on 2016/9/3 0003.
 */
public class Registration {
    private String id;
    private String name;

    public Registration(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
