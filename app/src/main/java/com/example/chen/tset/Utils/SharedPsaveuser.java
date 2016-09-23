package com.example.chen.tset.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.example.chen.tset.Data.User;
import com.example.chen.tset.Data.Userinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/9/23 0023.
 * 用户本地存取
 */
public class SharedPsaveuser {
    Context context;

    public SharedPsaveuser(Context context) {
        this.context = context;
    }
    //设置用户基本信息
    public void setspUser(Integer id,String phone,String name,String gender) {
        SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("id",id);
        editor.putString("phone",phone);
        editor.putString("name",name);
        editor.putString("gender",gender);
        editor.commit();
    }

    //设置用户头像
    public void setUsericon(String icon){
        SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("icon",icon);
        editor.commit();
    }

    //设置用户密码
    public void setUserpassworde(String password){
        SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("password",password);
        editor.commit();
    }


    //获取用户信息
    public Userinfo getTag() {
        SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Integer id = sp.getInt("id",0);
        String phone=sp.getString("phone",null);
        String name=sp.getString("name",null);
        String password=sp.getString("password",null);
        String gender=sp.getString("gender",null);
        String icon=sp.getString("icon",null);
        Userinfo userinfo=new Userinfo(id,phone,name,password,gender,icon);

        return userinfo;
    }
}
