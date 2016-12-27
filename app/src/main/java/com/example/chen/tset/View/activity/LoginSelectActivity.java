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
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.Data.entity.User;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.example.chen.tset.Utils.SmsObserver;
import com.example.chen.tset.page.adapter.LeadAdapter;
import com.example.chen.tset.page.view.MyViewPager;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

public class LoginSelectActivity extends AppCompatActivity {
    ViewPager vp, vp_top;
    LeadAdapter adapter;
    LeadAdapter topAdapter;
    Button btn_onekey_login, btn_password_login;
    LinearLayout ll_login_return, ll_login_return2;
    LinearLayout ll_pass_lgoin_register;
    LinearLayout ll_pass_lgoin_forget;
    EditText et_onekey_login, et_et_onekey_login_code;
    Button btn_gain_code, btn_confirm_onekey_login;
    SmsObserver mObserver;
    public static int MSG = 1;
    private OneKeyTimeCount time;
    Gson gson = new Gson();
    EditText et_login_phone, et_login_password;
    Button btn_login_pass_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_select);

        try {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.BROADCAST_SMS}, 1);

            }

            time = new OneKeyTimeCount(60000, 1000);

            findView();

            init();
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            mObserver = new SmsObserver(LoginSelectActivity.this, handler);
            Uri uri = Uri.parse("content://sms");
            //注册短信的监听
            getContentResolver().registerContentObserver(uri, true, mObserver);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(LoginSelectActivity.this, "自动获取验证码失败，请手动开启读取短信权限", Toast.LENGTH_SHORT).show();
        }

    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == MSG) {
                String c = (String) msg.obj;

                et_et_onekey_login_code.setText(c);

            }
        }
    };


    private void findView() {
        vp = (MyViewPager) findViewById(R.id.vp);
        vp_top = (MyViewPager) findViewById(R.id.vp_top);
    }

    private void init() {
        adapter = new LeadAdapter();
        topAdapter = new LeadAdapter();
        vp.setAdapter(adapter);
        vp_top.setAdapter(topAdapter);

        /**
         * 顶部viewpage
         */
        View topView = View.inflate(this, R.layout.login_select_top_item, null);
        topAdapter.addim(topView);
        ll_login_return = (LinearLayout) topView.findViewById(R.id.ll_login_return);

        View topView1 = View.inflate(this, R.layout.login_select_top_item1, null);
        topAdapter.addim(topView1);

        View topView2 = View.inflate(this, R.layout.login_select_top_item2, null);
        topAdapter.addim(topView2);
        ll_login_return2 = (LinearLayout) topView2.findViewById(R.id.ll_login_return2);


        /***
         * 底部一键登录
         */
        View view = View.inflate(this, R.layout.login_select_onekey_login_item, null);
        adapter.addim(view);

        et_onekey_login = (EditText) view.findViewById(R.id.et_onekey_login);
        et_et_onekey_login_code = (EditText) view.findViewById(R.id.et_et_onekey_login_code);
        btn_gain_code = (Button) view.findViewById(R.id.btn_gain_code);
        btn_confirm_onekey_login = (Button) view.findViewById(R.id.btn_confirm_onekey_login);
        oneKeyLoginonlick();


        /**
         * 默认界面
         */
        View view1 = View.inflate(this, R.layout.login_select_item1, null);
        adapter.addim(view1);
        btn_password_login = (Button) view1.findViewById(R.id.btn_password_login);
        btn_onekey_login = (Button) view1.findViewById(R.id.btn_onekey_login);
        loginSelectonclick();


        /**
         * 密码登录
         */
        View view2 = View.inflate(this, R.layout.login_select_pass_login_item2, null);
        adapter.addim(view2);
        ll_pass_lgoin_register = (LinearLayout) view2.findViewById(R.id.ll_pass_lgoin_register);
        ll_pass_lgoin_forget = (LinearLayout) view2.findViewById(R.id.ll_pass_lgoin_forget);

        et_login_phone = (EditText) view2.findViewById(R.id.et_login_phone);
        et_login_password = (EditText) view2.findViewById(R.id.et_login_password);
        btn_login_pass_ok = (Button) view2.findViewById(R.id.btn_login_pass_ok);

        passLoginonlick();


        /***
         * 默认显示刷新
         */
        vp.setCurrentItem(1, true);
        vp_top.setCurrentItem(1, true);


        topAdapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }

    private void loginSelectonclick() {
        btn_onekey_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp.setCurrentItem(0, true);
                vp_top.setCurrentItem(0, true);
            }
        });


        btn_password_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp.setCurrentItem(2, true);
                vp_top.setCurrentItem(2, true);
            }
        });
    }


    private void oneKeyLoginonlick() {
        ll_login_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp.setCurrentItem(1, true);
                vp_top.setCurrentItem(1, true);
            }
        });

        //获取验证码
        btn_gain_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

        //登录
        btn_confirm_onekey_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(et_onekey_login.getText().toString().equals("")||et_onekey_login.getText().toString()==null||et_et_onekey_login_code.getText().toString().equals("")||et_et_onekey_login_code.getText().toString()==null){
                    Toast.makeText(LoginSelectActivity.this, "账号或验证码不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    btn_confirm_onekey_login.setClickable(false);
                    btn_confirm_onekey_login.setText("登录中，请稍等...");
                    login();
                }
               
            }
        });

    }


    private void passLoginonlick() {
        ll_login_return2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp.setCurrentItem(1, true);
                vp_top.setCurrentItem(1, true);
            }
        });


        ll_pass_lgoin_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginSelectActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        ll_pass_lgoin_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginSelectActivity.this, FindpawActivity.class);
                startActivity(intent);
            }
        });


        btn_login_pass_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_login_phone.getText().toString().equals("")||et_login_phone.getText().toString()==null||et_login_password.getText().toString().equals("")||et_login_password.getText().toString()==null){
                    Toast.makeText(LoginSelectActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    passLogin();
                }

            }
        });
    }


    class OneKeyTimeCount extends CountDownTimer {

        public OneKeyTimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }


        @Override
        public void onTick(long millisUntilFinished) {
            btn_gain_code.setBackgroundResource(R.drawable.btn_login_case);
            btn_gain_code.setClickable(false);
            btn_gain_code.setText(+millisUntilFinished / 1000 + "秒");

        }

        @Override
        public void onFinish() {
            btn_gain_code.setText("获取验证码");
            btn_gain_code.setClickable(true);
            btn_gain_code.setBackgroundResource(R.drawable.btn_login_case);

        }
    }

    //获取验证码
    private void send() {
        if (TextUtils.isEmpty(et_onekey_login.getText())) {
            Toast.makeText(this, "请填写手机号", Toast.LENGTH_LONG).show();
        } else if (isMobileNO(et_onekey_login.getText().toString())) {

            OkHttpUtils
                    .post()
                    .url(Http_data.http_data + "/Send")
                    .addParams("phone", et_onekey_login.getText().toString())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginSelectActivity.this, "网络连接失败", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(String response, int id) {

                            if ("0".equals(response)) {
                                time.start();
                                Toast.makeText(LoginSelectActivity.this, "验证码已发送", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(LoginSelectActivity.this, "验证码发送失败", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        } else {
            Toast.makeText(LoginSelectActivity.this, "请填写正确的手机号", Toast.LENGTH_LONG).show();
        }
    }


    //登录
    private void login() {
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/OneLogin")
                .addParams("phone", et_onekey_login.getText().toString())
                .addParams("code", et_et_onekey_login_code.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(LoginSelectActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        btn_confirm_onekey_login.setClickable(true);
                        btn_confirm_onekey_login.setText("登录");
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        if (response.equals("2")) {

                            btn_confirm_onekey_login.setClickable(true);
                            btn_confirm_onekey_login.setText("登录");
                            Toast.makeText(LoginSelectActivity.this, "登录失败", Toast.LENGTH_SHORT).show();

                        } else if (response.equals("1")) {

                            btn_confirm_onekey_login.setClickable(true);
                            btn_confirm_onekey_login.setText("登录");
                            Toast.makeText(LoginSelectActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();

                        } else {

                            User user = gson.fromJson(response, User.class);

                            User_Http.user.setUser(user);

                            if (user.getName() != null && !"".equals(user.getName())) {

                                Intent intent = new Intent(LoginSelectActivity.this, HomeActivity.class);
                                startActivity(intent);

                            } else {

                                Intent intent = new Intent(LoginSelectActivity.this, SetdataActivity.class);
                                startActivity(intent);

                            }

                            finish();
                        }

                    }
                });
    }


    private void passLogin() {
        btn_login_pass_ok.setClickable(false);
        btn_login_pass_ok.setText("登录中，请稍等...");
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/login")
                .addParams("phone", et_login_phone.getText().toString())
                .addParams("password", et_login_password.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(LoginSelectActivity.this, "网络连接失败", Toast.LENGTH_LONG).show();
                        btn_login_pass_ok.setClickable(true);
                        btn_login_pass_ok.setText("登录");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response.equals("1")) {
                            btn_login_pass_ok.setClickable(true);
                            btn_login_pass_ok.setText("登录");
                            Toast.makeText(LoginSelectActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("2")) {
                            btn_login_pass_ok.setClickable(true);
                            btn_login_pass_ok.setText("登录");
                            Toast.makeText(LoginSelectActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                        } else if (response.length() != 1) {
                            User user = gson.fromJson(response, User.class);

                            User_Http.user.setUser(user);

                            //第一次登陆保存用户密码
                            SharedPsaveuser sp = new SharedPsaveuser(LoginSelectActivity.this);
                            sp.setUserpassworde(et_login_password.getText().toString());

                            if (user.getName() != null && !"".equals(user.getName())) {
                                Intent intent = new Intent(LoginSelectActivity.this, HomeActivity.class);
                                startActivity(intent);

                            } else {
                                Intent intent = new Intent(LoginSelectActivity.this, SetdataActivity.class);
                                startActivity(intent);
                            }
                            finish();
                        }
                    }
                });
    }

    public boolean isMobileNO(String mobiles) {
        Pattern p=Pattern.compile("[1][3458]\\d{9}");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }


    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

}
