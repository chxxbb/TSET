package com.example.chen.tset.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.User;
import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.View.LogActivity;
import com.example.chen.tset.View.LoginActivity;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/28 0028.
 */
public class MyBaseActivity extends AppCompatActivity {
    SharedPsaveuser sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPsaveuser(MyBaseActivity.this);
        if(User_Http.user.getPhone()==null){

            Intent intent=new Intent(MyBaseActivity.this, LogActivity.class);
            startActivity(intent);
        }

    }




}
