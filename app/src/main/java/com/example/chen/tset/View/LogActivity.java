package com.example.chen.tset.View;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.User;
import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

public class LogActivity extends AppCompatActivity {
    SharedPsaveuser sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        //设置引导页面延时
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    handler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        sp = new SharedPsaveuser(LogActivity.this);





    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    judgeWhetherRegister();
                    break;
            }
        }
    };

    //判断是否登录过
    private void judgeWhetherRegister() {
        if (sp.getTag().getPhone() != null && sp.getTag().getPassword() != null) {
            registerjudge();
            Intent intent = new Intent(LogActivity.this, HomeActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(LogActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    //如果登录过则从后台获取用户信息
    private void registerjudge() {
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/login")
                .addParams("phone", sp.getTag().getPhone())
                .addParams("password", sp.getTag().getPassword())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("返回", response);

                        if (response.equals("1")) {
                            Toast.makeText(LogActivity.this, "密码被修改", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(LogActivity.this, LoginActivity.class);
                            startActivity(i);
                        } else {
                            Gson gson = new Gson();
                            User user = gson.fromJson(response, User.class);
                            Log.e("user", user.toString());
                            User_Http.user.setUser(user);
                        }

                    }
                });
    }
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
}
