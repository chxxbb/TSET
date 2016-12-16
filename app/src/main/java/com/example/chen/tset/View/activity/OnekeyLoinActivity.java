package com.example.chen.tset.View.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.Data.entity.User;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.example.chen.tset.Utils.SmsObserver;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;

/**
 * 一键登录页面
 */
public class OnekeyLoinActivity extends AppCompatActivity {

    private LinearLayout ll_use_password_login;

    private EditText onekeylogin_phone_edittext, activity_onekey_Verification_code;

    private Button activity_onekeyr_Verification_code_button, login_button;

    private TimeCount time;

    Gson gson = new Gson();

    SmsObserver mObserver;

    public static int MSG = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onekey_loin);
        try {

            time = new TimeCount(60000, 1000);

            findView();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void findView() {
        ll_use_password_login = (LinearLayout) findViewById(R.id.ll_use_password_login);

        onekeylogin_phone_edittext = (EditText) findViewById(R.id.onekeylogin_phone_edittext);

        activity_onekey_Verification_code = (EditText) findViewById(R.id.activity_onekey_Verification_code);

        activity_onekeyr_Verification_code_button = (Button) findViewById(R.id.activity_onekeyr_Verification_code_button);


        login_button = (Button) findViewById(R.id.login_button);

        ll_use_password_login.setOnClickListener(listener);

        activity_onekeyr_Verification_code_button.setOnClickListener(listener);

        login_button.setOnClickListener(listener);


        try {
            mObserver = new SmsObserver(OnekeyLoinActivity.this, handler);
            Uri uri = Uri.parse("content://sms");
            //注册短信的监听
            getContentResolver().registerContentObserver(uri, true, mObserver);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(OnekeyLoinActivity.this, "自动获取验证码失败，请手动开启读取短信权限", Toast.LENGTH_SHORT).show();
        }


    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == MSG) {
                String c = (String) msg.obj;

                activity_onekey_Verification_code.setText(c);

            }
        }
    };


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_use_password_login:

                    Intent intent = new Intent(OnekeyLoinActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;


                case R.id.activity_onekeyr_Verification_code_button:
                    send();
                    break;

                case R.id.login_button:

                    login_button.setClickable(false);
                    login_button.setText("登录中，请稍等...");

                    login();
                    break;
            }
        }
    };

    private void login() {
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/OneLogin")
                .addParams("phone", onekeylogin_phone_edittext.getText().toString())
                .addParams("code", activity_onekey_Verification_code.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(OnekeyLoinActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        login_button.setClickable(true);
                        login_button.setText("登录");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("一键登录返回", response);

                        if (response.equals("2")) {

                            login_button.setClickable(true);
                            login_button.setText("登录");
                            Toast.makeText(OnekeyLoinActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("1")) {

                            login_button.setClickable(true);
                            login_button.setText("登录");
                            Toast.makeText(OnekeyLoinActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                        } else {

                            User user = gson.fromJson(response, User.class);

                            User_Http.user.setUser(user);

                            SharedPsaveuser sp = new SharedPsaveuser(OnekeyLoinActivity.this);


                            if (user.getName() != null && !"".equals(user.getName())) {
                                Intent intent = new Intent(OnekeyLoinActivity.this, HomeActivity.class);
                                startActivity(intent);

                            } else {
                                Intent intent = new Intent(OnekeyLoinActivity.this, SetdataActivity.class);
                                startActivity(intent);
                            }

                            finish();

                        }


                    }
                });
    }


    private void send() {
        if (TextUtils.isEmpty(onekeylogin_phone_edittext.getText())) {
            Toast.makeText(this, "请填写手机号", Toast.LENGTH_LONG).show();
        } else if (isMobileNO(onekeylogin_phone_edittext.getText().toString())) {

            OkHttpUtils
                    .post()
                    .url(Http_data.http_data + "/Send")
                    .addParams("phone", onekeylogin_phone_edittext.getText().toString())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OnekeyLoinActivity.this, "网络连接失败", Toast.LENGTH_LONG).show();
                                }
                            });
                        }


                        @Override
                        public void onResponse(String response, int id) {

                            if ("0".equals(response)) {
                                time.start();
                                Toast.makeText(OnekeyLoinActivity.this, "验证码已发送", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(OnekeyLoinActivity.this, "验证码发送失败", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        } else {
            Toast.makeText(OnekeyLoinActivity.this, "请填写正确的手机号", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }


    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            activity_onekeyr_Verification_code_button.setBackgroundResource(R.drawable.verification_btn_case);
            activity_onekeyr_Verification_code_button.setClickable(false);
            activity_onekeyr_Verification_code_button.setText(+millisUntilFinished / 1000 + "秒");

        }

        @Override
        public void onFinish() {
            activity_onekeyr_Verification_code_button.setText("获取验证码");
            activity_onekeyr_Verification_code_button.setClickable(true);
            activity_onekeyr_Verification_code_button.setBackgroundResource(R.drawable.verification_btn_case);

        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.BROADCAST_SMS}, 1);

        }
    }
}
