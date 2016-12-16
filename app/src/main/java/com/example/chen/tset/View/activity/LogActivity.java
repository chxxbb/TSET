package com.example.chen.tset.View.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.entity.User;
import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;


public class LogActivity extends AppCompatActivity {
    SharedPsaveuser sp;

    int i = 0;

    ImageView iv_page, iv_log;

    String startUrl;


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

        sp = new SharedPsaveuser(LogActivity.this);

        iv_page = (ImageView) findViewById(R.id.iv_page);

        iv_log = (ImageView) findViewById(R.id.iv_log);

        try {
            //延迟2秒
            new Handler().postDelayed(new Runnable() {
                public void run() {

//                    if (sp.setStartPage().equals("") || sp.setStartPage() == null) {
//                        iv_log.setBackgroundResource(R.drawable.app_log_page);
//                    } else {
//                        ImageLoader.getInstance().displayImage(sp.setStartPage(), iv_log);
//                    }
//                    pageInit();

                    //判断是否是第一次登录，如果是则跳转到登录页面，如果不是则跳转到首页
                    judgeWhetherRegister();

                }
            }, 2000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //判断是否登录过
    private void judgeWhetherRegister() {

        if ((sp.getTag().getPhone() != null && sp.getTag().getPassword() != null)) {

            Intent intent = new Intent(LogActivity.this, HomeActivity.class);

            startActivity(intent);

            finish();

            registerjudge();

        } else {
            Intent intent = new Intent(LogActivity.this, LoginSelectActivity.class);
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

                            Intent i = new Intent(HomeActivity.text_homeactivity, LoginSelectActivity.class);

                            startActivity(i);

                            HomeActivity.text_homeactivity.finish();

                            sp.clearinit();


                        } else if (response.equals("2")) {
                            Toast.makeText(HomeActivity.text_homeactivity, "登录异常，请重新登录", Toast.LENGTH_SHORT).show();

                            sp.clearinit();

                            Intent i = new Intent(HomeActivity.text_homeactivity, LoginSelectActivity.class);

                            startActivity(i);

                            HomeActivity.text_homeactivity.finish();
                        } else if (response.length() != 1) {
                            Gson gson = new Gson();

                            User user = gson.fromJson(response, User.class);

                            User_Http.user.setUser(user);

                        }

                    }
                });
    }


    public void pageInit() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/launch")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.e("失败", "失败");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                if (response.equals("") || response == null) {
                                    sp.getStartPage("");
                                } else {
                                    sp.getStartPage(response);

                                    startUrl = response;

                                    handler.sendEmptyMessage(0);

                                }
                            }
                        });
            }
        }).start();


    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ImageLoader.getInstance().displayImage(startUrl, iv_page);
        }
    };


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
