package com.example.chen.tset.View.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
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


public class LogActivity extends AppCompatActivity {
    SharedPsaveuser sp;

    private RelativeLayout rl_log;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.defaultTheme);
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        setContentView(R.layout.activity_log);

        rl_log = (RelativeLayout) findViewById(R.id.rl_log);


        //初始化
        Animation alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
        //设置动画时间
        alphaAnimation.setDuration(300);

        rl_log.startAnimation(alphaAnimation);


        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //判断是否是第一次登录，如果是则跳转到登录页面，如果不是则跳转到首页

                judgeWhetherRegister();


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }


    //判断是否登录过
    private void judgeWhetherRegister() {
        sp = new SharedPsaveuser(LogActivity.this);
        if ((sp.getTag().getPhone() != null && sp.getTag().getPassword() != null)) {

            Intent intent = new Intent(LogActivity.this, HomeActivity.class);

            startActivity(intent);

            finish();

            registerjudge();


        } else {

            Intent intent = new Intent(LogActivity.this, OnekeyLoinActivity.class);
            startActivity(intent);
            finish();


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


                        if (response.equals("1")) {

                            Toast.makeText(HomeActivity.text_homeactivity, "密码被修改请重新登录", Toast.LENGTH_SHORT).show();


                            Intent i = new Intent(HomeActivity.text_homeactivity, OnekeyLoinActivity.class);

                            startActivity(i);


                            HomeActivity.text_homeactivity.finish();


                            sp.clearinit();


                        } else if (response.equals("2")) {
                            Toast.makeText(HomeActivity.text_homeactivity, "登录异常，请重新登录", Toast.LENGTH_SHORT).show();

                            sp.clearinit();

                            Intent i = new Intent(HomeActivity.text_homeactivity, OnekeyLoinActivity.class);

                            startActivity(i);

                            HomeActivity.text_homeactivity.finish();
                        } else if (response.length() != 1) {
                            Gson gson = new Gson();

                            User user = gson.fromJson(response, User.class);


                            User_Http.user.setUser(user);

                            Log.e("user", User_Http.user.toString());


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
