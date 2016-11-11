package com.example.chen.tset.View.activity;

import android.app.Activity;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Response;
/**
 * 修改密码
 */
public class FindpawActivity extends MyBaseActivity {

    EditText activity_find_password_phone = null, activity_find_password_code = null, activity_find_password_password = null, activity_find_password_password2 = null;
    Button activity_find_password_getcode_button = null, activity_find_password_button = null;

    Activity activity = this;
    private LinearLayout iv_ret;

    SharedPsaveuser sp;

    private TimeCount time;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pawnumber);

        sp=new SharedPsaveuser(this);

        initView();
        //设定再次获取验证码需要的时间
        time = new TimeCount(60000, 1000);
        initOnClick();

    }

    private void initOnClick() {



        activity_find_password_getcode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(activity_find_password_phone.getText())) {
                    Toast.makeText(activity, "请填写手机号", Toast.LENGTH_LONG).show();
                } else if (isMobileNO(activity_find_password_phone.getText().toString())) {

                    OkHttpUtils
                            .post()
                            .url(Http_data.http_data + "/Send")
                            .addParams("phone", activity_find_password_phone.getText().toString())
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
                                    System.out.println(response);
                                    if ("1".equals(response)) {

                                        Toast.makeText(activity, "验证码发送失败", Toast.LENGTH_LONG).show();
                                    } else {
                                        time.start();
                                        Toast.makeText(activity, "验证码已发送", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                } else {
                    Toast.makeText(activity, "请填写正确的手机号", Toast.LENGTH_LONG).show();
                }
            }
        });

        activity_find_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("-----------nnnn--------");

                if (TextUtils.isEmpty(activity_find_password_phone.getText().toString())) {
                    Toast.makeText(activity, "请填写手机号", Toast.LENGTH_LONG).show();
                } else if (!isMobileNO(activity_find_password_phone.getText().toString())) {
                    Toast.makeText(activity, "请填写正确的手机号", Toast.LENGTH_LONG).show();
                } else if (!isPasswordNo(activity_find_password_password.getText().toString())) {
                    Toast.makeText(activity, "请填写数字或字母组成的密码", Toast.LENGTH_LONG).show();
                } else if (!activity_find_password_password.getText().toString().equals(activity_find_password_password2.getText().toString())) {
                    Toast.makeText(activity, "两次密码输入不一致", Toast.LENGTH_LONG).show();
                } else if (!(activity_find_password_code.getText().toString().length() == 6)) {
                    Toast.makeText(activity, "验证码错误", Toast.LENGTH_LONG).show();
                } else {
                    OkHttpUtils
                            .post()
                            .url(Http_data.http_data + "/ChangeP")
                            .addParams("phone", activity_find_password_phone.getText().toString())
                            .addParams("password", activity_find_password_password.getText().toString())
                            .addParams("code", activity_find_password_code.getText().toString())
                            .build()
                            .execute(new Callback() {
                                @Override
                                public Object parseNetworkResponse(Response response, int id) throws Exception {
                                    byte[] str = response.body().bytes();
                                    int i = (int) str[0];
                                    return i;
                                }

                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Toast.makeText(activity, "连接失败", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onResponse(Object response, int id) {
                                    int t = (int) response;

                                    if (ASCII(t) == 1) {
                                        Toast.makeText(activity, "修改失败", Toast.LENGTH_LONG).show();
                                    } else if (ASCII(t) == 2) {
                                        Toast.makeText(activity, "请先发送验证码", Toast.LENGTH_LONG).show();
                                    } else if (ASCII(t) == 0) {
                                        Toast.makeText(activity, "修改成功", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        Toast.makeText(activity, "未知错误", Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                }

            }
        });

    }

    private void initView() {
        activity_find_password_phone = (EditText) findViewById(R.id.find_password_phone);
        activity_find_password_code = (EditText) findViewById(R.id.find_password_code);
        activity_find_password_password = (EditText) findViewById(R.id.find_password_password);
        activity_find_password_password2 = (EditText) findViewById(R.id.find_password_password2);

        activity_find_password_getcode_button = (Button) findViewById(R.id.find_password_getcode_button);
        activity_find_password_button = (Button) findViewById(R.id.find_password_button);
        iv_ret= (LinearLayout) findViewById(R.id.iv_ret);
        iv_ret.setOnClickListener(listener);

    }
    private View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };




    //再次获取验证码
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            activity_find_password_getcode_button.setBackgroundResource(R.drawable.verification_btn_case);
            activity_find_password_getcode_button.setClickable(false);
            activity_find_password_getcode_button.setText(millisUntilFinished / 1000 +"秒");

        }

        @Override
        public void onFinish() {
            activity_find_password_getcode_button.setText("获取验证码");
            activity_find_password_getcode_button.setClickable(true);
            activity_find_password_getcode_button.setBackgroundResource(R.drawable.verification_btn_case);

        }
    }

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

    public int ASCII(int i) {
        int t = i - 48;
        return t;
    }

}
