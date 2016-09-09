package com.example.chen.tset.View;

import android.content.Intent;
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
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;

import okhttp3.Call;

public class PasswordSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_pas, tv_passet;
    private EditText et_newpassword, et_forpassword;
    private LinearLayout linearlayout;
    String forpas;
    String newpas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_setting);
        findView();
        init();
    }


    private void findView() {
        tv_pas = (TextView) findViewById(R.id.tv_pas);
        et_newpassword = (EditText) findViewById(R.id.et_newpassword);
        et_forpassword = (EditText) findViewById(R.id.et_forpassword);
        linearlayout = (LinearLayout) findViewById(R.id.linearlayout);
        tv_passet = (TextView) findViewById(R.id.tv_passet);
        forpas = et_forpassword.getText().toString();
        newpas = et_newpassword.getText().toString();
        tv_pas.setOnClickListener(this);
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
                tv_pas.setTextColor(android.graphics.Color.parseColor("#6fc9e6"));
            } else {
                tv_pas.setTextColor(android.graphics.Color.parseColor("#e0e0e0"));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void init() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pas:
                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/changeP" + "?" + User_Http.user.getId())
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
                                    Toast.makeText(PasswordSettingActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });


                break;
            case R.id.tv_passet:
                finish();
                break;

        }
    }
}
