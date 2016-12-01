package com.example.chen.tset.View.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * 修改昵称
 */
public class NamepageActivity extends MyBaseActivity {
    private EditText et_name_save;
    private TextView tv_pas;
    private LinearLayout ll_rutname;

    SharedPsaveuser sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_namepage);
        try {

            sp = new SharedPsaveuser(this);
            findView();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findView() {
        et_name_save = (EditText) findViewById(R.id.et_name_save);
        tv_pas = (TextView) findViewById(R.id.tv_pas);
        ll_rutname = (LinearLayout) findViewById(R.id.ll_rutname);


        if (User_Http.user.getName() == null || User_Http.user.getName().equals("")) {
            et_name_save.setHint(sp.getTag().getName());
        } else {
            et_name_save.setHint(User_Http.user.getName());
        }


        et_name_save.addTextChangedListener(textListener);
        ll_rutname.setOnClickListener(listener);
    }

    private TextWatcher textListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //判断输入框是否输入字符，如果输入则改变颜色
            if (et_name_save.length() == 0) {
                tv_pas.setTextColor(android.graphics.Color.parseColor("#e0e0e0"));
                tv_pas.setOnClickListener(null);
            } else {
                tv_pas.setTextColor(android.graphics.Color.parseColor("#6fc9e6"));
                tv_pas.setOnClickListener(listener);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_rutname:
                    finish();
                    break;
                case R.id.tv_pas:
                    SharedPsaveuser sp = new SharedPsaveuser(NamepageActivity.this);

                    if (et_name_save.getText().toString().trim().equals("") || et_name_save.getText().toString() == null) {
                        Toast.makeText(NamepageActivity.this, "昵称不能为空", Toast.LENGTH_SHORT).show();

                    } else {
                        OkHttpUtils
                                .post()
                                .url(Http_data.http_data + "/ChangeName")
                                .addParams("id", sp.getTag().getId() + "")
                                .addParams("name", et_name_save.getText().toString())
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        Toast.makeText(NamepageActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        if (response.equals("0")) {
                                            User_Http.user.setName(et_name_save.getText().toString());
                                            finish();
                                        } else {
                                            Toast.makeText(NamepageActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }

                    break;
            }
        }
    };
}
