package com.example.chen.tset.Utils;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.entity.User;
import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.View.activity.LogActivity;
import com.example.chen.tset.View.activity.LoginActivity;
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

//        sp=new SharedPsaveuser(MyBaseActivity.this);
//
//        if(User_Http.user.getPhone()==null&&User_Http.user.getName()==null){
//
//            registerjudge();
//        }


        if (null != savedInstanceState) {

            //activity由系统加载的时候savedInstanceState不为空
            Intent intent = new Intent(MyBaseActivity.this, LogActivity.class);
            startActivity(intent);
        }



    }


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
                            Toast.makeText(MyBaseActivity.this, "密码被修改", Toast.LENGTH_SHORT).show();
                            sp.clearinit();
                            Intent i = new Intent(MyBaseActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Gson gson = new Gson();
                            User user = gson.fromJson(response, User.class);
                            Log.e("user", user.toString());
                            User_Http.user.setUser(user);
                        }

                    }
                });
    }


}
