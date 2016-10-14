package com.example.chen.tset.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.Lecture;
import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;

import okhttp3.Call;
/**
 * 密码修改
 */
public class PasswordSettingActivity extends MyBaseActivity implements View.OnClickListener {
    private TextView tv_pas, tv_passet;
    private EditText et_newpassword, et_forpassword;
    private LinearLayout linearlayout;
    String forpas;
    String newpas;
    SharedPsaveuser sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_setting);
        sp = new SharedPsaveuser(this);
        findView();
    }


    private void findView() {
        tv_pas = (TextView) findViewById(R.id.tv_pas);
        et_newpassword = (EditText) findViewById(R.id.et_newpassword);
        et_forpassword = (EditText) findViewById(R.id.et_forpassword);
        linearlayout = (LinearLayout) findViewById(R.id.linearlayout);
        tv_passet = (TextView) findViewById(R.id.tv_passet);
        forpas = et_forpassword.getText().toString();
        newpas = et_newpassword.getText().toString();
        linearlayout.setOnClickListener(this);
        tv_passet.setOnClickListener(this);
        et_newpassword.addTextChangedListener(textchangelisterer);
        et_forpassword.addTextChangedListener(textchangelisterer);
    }



    private TextWatcher textchangelisterer = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (et_newpassword.getText().toString().length() != 0 && et_forpassword.getText().toString().length() != 0) {
                //如果已经输入字符则字体颜色改变，且textview可以点击
                tv_pas.setTextColor(android.graphics.Color.parseColor("#6fc9e6"));
                tv_pas.setOnClickListener(listener);
            } else {
                tv_pas.setTextColor(android.graphics.Color.parseColor("#e0e0e0"));
                //设置textview不能点击
                tv_pas.setOnClickListener(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            OkHttpUtils
                    .post()
                    .url(Http_data.http_data + "/ChangeP" )
                    .addParams("id",sp.getTag().getId()+"")
                    .addParams("oldPassword", et_forpassword.getText().toString())
                    .addParams("newPassword", et_newpassword.getText().toString())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Toast.makeText(PasswordSettingActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (response.equals("1")) {
                                Toast.makeText(PasswordSettingActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                            } else {
                                User_Http.user.setPhone(et_newpassword.getText().toString());
                                //清除本地用户信息
                                sp.clearinit();
                                Intent intent5 = new Intent(PasswordSettingActivity.this, LoginActivity.class);
                                startActivity(intent5);
                                Toast.makeText(PasswordSettingActivity.this, "密码修改成功,请重新登录", Toast.LENGTH_SHORT).show();
                                HomeActivity.text_homeactivity.finish();
                                finish();
                            }
                        }
                    });
        }
    };



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_passet:
                finish();
                break;

        }
    }
}
