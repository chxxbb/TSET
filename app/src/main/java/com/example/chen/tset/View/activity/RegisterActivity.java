package com.example.chen.tset.View.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.entity.User;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.example.chen.tset.Utils.SmsObserver;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;

/**
 * 注册页面
 */
public class RegisterActivity extends MyBaseActivity {

    EditText activity_register_phone = null, activity_register_password = null, activity_register_password2 = null, activity_register_Verification_code = null;
    Button activity_register_Verification_code_button = null, activity_register_submit = null;
    LinearLayout activity_register_exit = null;

    Activity activity = this;
    Gson gson = new Gson();

    User user = new User();

    private TimeCount time;

    SmsObserver mObserver;

    public static int RMSG = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        try {

            initView();

            time = new TimeCount(60000, 1000);
            initOnclick();

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            mObserver = new SmsObserver(RegisterActivity.this, handler);
            Uri uri = Uri.parse("content://sms");
            //注册短信的监听
            getContentResolver().registerContentObserver(uri, true, mObserver);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(RegisterActivity.this, "自动获取验证码失败，请手动开启读取短信权限", Toast.LENGTH_SHORT).show();
        }


    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == RMSG) {
                String c = (String) msg.obj;

                activity_register_Verification_code.setText(c);

            }
        }
    };

    private void initOnclick() {
        activity_register_Verification_code_button.setOnClickListener(new View.OnClickListener() {      //点击发送验证码后触发
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(activity_register_phone.getText())) {
                    Toast.makeText(activity, "请填写手机号", Toast.LENGTH_LONG).show();
                } else if (isMobileNO(activity_register_phone.getText().toString())) {

                    OkHttpUtils
                            .post()
                            .url(Http_data.http_data + "/Send")
                            .addParams("phone", activity_register_phone.getText().toString())
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(activity, "网络连接失败", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(String response, int id) {

                                    if ("0".equals(response)) {
                                        time.start();
                                        Toast.makeText(activity, "验证码已发送", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(activity, "验证码发送失败", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                } else {
                    Toast.makeText(activity, "请填写正确的手机号", Toast.LENGTH_LONG).show();
                }
            }
        });


        activity_register_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(activity_register_phone.getText().toString())) {
                    Toast.makeText(activity, "请填写手机号", Toast.LENGTH_LONG).show();
                } else if (!isMobileNO(activity_register_phone.getText().toString())) {
                    Toast.makeText(activity, "请填写正确的手机号", Toast.LENGTH_LONG).show();
                } else if (!isPasswordNo(activity_register_password.getText().toString())) {
                    Toast.makeText(activity, "请填写数字或字母组成的密码", Toast.LENGTH_LONG).show();
                } else if (!activity_register_password.getText().toString().equals(activity_register_password2.getText().toString())) {
                    Toast.makeText(activity, "两次密码输入不一致", Toast.LENGTH_LONG).show();
                } else if (!(activity_register_Verification_code.getText().toString().length() == 6)) {
                    Toast.makeText(activity, "验证码错误", Toast.LENGTH_LONG).show();
                } else {
                    user.setPhone(activity_register_phone.getText().toString());
                    user.setPassword(activity_register_password.getText().toString());
                    OkHttpUtils
                            .post()
                            .url(Http_data.http_data + "/register")
                            .addParams("user", gson.toJson(user))
                            .addParams("code", activity_register_Verification_code.getText().toString())
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(activity, "网络连接失败", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(String response, int id) {

                                    if ("1".equals(response)) {
                                        Toast.makeText(activity, "用户已注册", Toast.LENGTH_LONG).show();
                                    } else if ("2".equals(response)) {
                                        Toast.makeText(activity, "请发送验证码", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(activity, "注册成功", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }

            }
        });

    }

    private void initView() {
        activity_register_phone = (EditText) findViewById(R.id.activity_register_phone);
        activity_register_password = (EditText) findViewById(R.id.activity_register_password);
        activity_register_password2 = (EditText) findViewById(R.id.activity_register_password2);
        activity_register_Verification_code = (EditText) findViewById(R.id.activity_register_Verification_code);
        activity_register_Verification_code_button = (Button) findViewById(R.id.activity_register_Verification_code_button);
        activity_register_submit = (Button) findViewById(R.id.activity_register_submit);
        activity_register_exit = (LinearLayout) findViewById(R.id.activity_register_exit);
        activity_register_exit.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public boolean isPasswordNo(String mobiles) {
        Pattern p = Pattern.compile("^[0-9a-zA-Z]{6,16}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            activity_register_Verification_code_button.setBackgroundResource(R.drawable.verification_btn_case);
            activity_register_Verification_code_button.setClickable(false);
            activity_register_Verification_code_button.setText(+millisUntilFinished / 1000 + "秒");

        }

        @Override
        public void onFinish() {
            activity_register_Verification_code_button.setText("获取验证码");
            activity_register_Verification_code_button.setClickable(true);
            activity_register_Verification_code_button.setBackgroundResource(R.drawable.verification_btn_case);

        }
    }

}
