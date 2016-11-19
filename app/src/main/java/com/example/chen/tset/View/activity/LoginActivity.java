package com.example.chen.tset.View.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.entity.User;
import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

public class LoginActivity extends AppCompatActivity {

    EditText login_phone_edittext = null, login_password_edittext = null;
    Button login_button = null;
    TextView login_new_user = null, login_find_password = null;


    Gson gson = new Gson();
    Activity activity = this;
    SharedPsaveuser sp;

    LinearLayout activity_login_exit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);


        initview();

        initOnclick();


        sp = new SharedPsaveuser(LoginActivity.this);


    }

    private void initOnclick() {

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {           //点击登录后的触发
                login_button.setClickable(false);
                login_button.setText("登录中，请稍等...");
                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/login")
                        .addParams("phone", login_phone_edittext.getText().toString())
                        .addParams("password", login_password_edittext.getText().toString())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Toast.makeText(activity, "网络连接失败", Toast.LENGTH_LONG).show();
                                login_button.setClickable(true);
                                login_button.setText("登录");
                            }

                            @Override
                            public void onResponse(String response, int id) {


                                if (response.equals("1")) {

                                    login_button.setClickable(true);
                                    login_button.setText("登录");
                                    Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                                } else if (response.equals("2")) {
                                    login_button.setClickable(true);
                                    login_button.setText("登录");
                                    Toast.makeText(LoginActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                                } else {
                                    User user = gson.fromJson(response, User.class);

                                    User_Http.user.setUser(user);


                                    //第一次登陆保存用户密码
                                    SharedPsaveuser sp = new SharedPsaveuser(LoginActivity.this);
                                    sp.setUserpassworde(login_password_edittext.getText().toString());


                                    if (user.getName() != null && !"".equals(user.getName())) {
                                        Intent intent = new Intent(activity, HomeActivity.class);
                                        startActivity(intent);

                                    } else {
                                        Intent intent = new Intent(LoginActivity.this, SetdataActivity.class);
                                        startActivity(intent);
                                    }
                                    finish();
                                }

                            }
                        });
            }
        });

        login_new_user.setOnClickListener(new View.OnClickListener() {          //点击新用户后的触发
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        login_find_password.setOnClickListener(new View.OnClickListener() {     //点击忘记密码后的交互
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FindpawActivity.class);
                startActivity(intent);
            }
        });

        //回退到一键登录页面
        activity_login_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, OnekeyLoinActivity.class);

                startActivity(intent);

                finish();
            }
        });


    }

    private void initview() {
        login_phone_edittext = (EditText) findViewById(R.id.login_phone_edittext);
        login_password_edittext = (EditText) findViewById(R.id.login_password_edittext);
        login_button = (Button) findViewById(R.id.login_button);

        login_new_user = (TextView) findViewById(R.id.login_new_user);
        login_find_password = (TextView) findViewById(R.id.login_find_password);

        activity_login_exit = (LinearLayout) findViewById(R.id.activity_login_exit);

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
