package com.example.chen.tset.View.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Environment;
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
import com.example.chen.tset.Data.entity.Launch;
import com.example.chen.tset.Data.entity.User;
import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.Lauar;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.L;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;


public class LogActivity extends AppCompatActivity {
    SharedPsaveuser sp;

    int i = 0;

    ImageView iv_page, iv_log;


    File sdcardTempFile;
    File audioFile;

    Launch launch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.defaultTheme);
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        setContentView(R.layout.activity_log);

        sp = new SharedPsaveuser(LogActivity.this);

        iv_page = (ImageView) findViewById(R.id.iv_page);

        iv_log = (ImageView) findViewById(R.id.iv_log);

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String str1 = sdf.format(curDate);


            if (sp.setStartPage().getDate() == null || sp.setStartPage().getDate().equals("")) {
                iv_log.setBackgroundResource(R.drawable.app_log_page);
            } else if (getTimeStamp(sp.setStartPage().getDate(), "yyyy-MM-dd") >= getTimeStamp(str1, "yyyy-MM-dd")) {
                ImageLoader.getInstance().displayImage("file:///" + sp.setStartPage().getImg(), iv_log);
            } else {
                iv_log.setBackgroundResource(R.drawable.app_log_page);
            }
        } catch (Exception e) {
            e.printStackTrace();

            iv_log.setBackgroundResource(R.drawable.app_log_page);
        }


        try {
            //延迟2秒
            new Handler().postDelayed(new Runnable() {
                public void run() {


                    //判断是否是第一次登录，如果是则跳转到登录页面，如果不是则跳转到首页
                    judgeWhetherRegister();

                    pageInit();

                }
            }, 2000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long getTimeStamp(String timeStr, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = simpleDateFormat.parse(timeStr);
            long timeStamp = date.getTime();
            return timeStamp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
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

                            }

                            @Override
                            public void onResponse(String response, int id) {

                                Gson gson = new Gson();

                                launch = gson.fromJson(response, Launch.class);

                                Log.e("11", launch.toString());

                                if (sp.setStartPage().getDate() == null || sp.setStartPage().getDate().equals("")) {
                                    handler.sendEmptyMessage(0);
                                } else {

                                    if (!sp.setStartPage().getDate().equals(launch.getDate())) {
                                        handler.sendEmptyMessage(0);

                                    } else {

                                    }
                                }


                            }
                        });
            }
        }).start();


    }


    private void saveicon() {
        new Thread() {
            public void run() {
                try {
                    audioFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/text/launch/");
                    audioFile.mkdirs();//创建文件夹
                    sdcardTempFile = File.createTempFile(".launchPage", ".jpg", audioFile);
                    URL url = new URL(launch.getImg());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(6 * 1000);  // 设置时间不要超过10秒，避免被android系统回收
                    if (conn.getResponseCode() != 200) throw new RuntimeException("请求url失败");
                    InputStream inSream = conn.getInputStream();
                    //把图片保存到项目的根目录
                    readAsFile(inSream, new File(String.valueOf(sdcardTempFile)));
                    String launchImg = sdcardTempFile.getAbsolutePath();
                    //将更改过的头像保存在本地
                    sp.getStartPage(launchImg, launch.getDate());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    //将获取的头像转换成流
    public static void readAsFile(InputStream inSream, File file) throws Exception {
        FileOutputStream outStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inSream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inSream.close();

    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            saveicon();

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
