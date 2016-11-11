package com.example.chen.tset.View.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
 * 修改性别
 */
public class SexpageActivity extends MyBaseActivity {
    private LinearLayout ll_rutgender;
    private RadioButton rb_man, rb_nman;
    SharedPsaveuser sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genderpage);
        sp = new SharedPsaveuser(this);
        findView();
    }


    private void findView() {
        ll_rutgender = (LinearLayout) findViewById(R.id.ll_rutgender);
        rb_man = (RadioButton) findViewById(R.id.rb_man);
        rb_nman = (RadioButton) findViewById(R.id.rb_nman);
        ll_rutgender.setOnClickListener(listener);
        rb_man.setOnClickListener(listener);
        rb_nman.setOnClickListener(listener);
        if (User_Http.user.getGender().equals("男")) {
            rb_man.setChecked(true);
        } else {
            rb_nman.setChecked(true);
        }
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_rutgender:
                    finish();
                    break;
                case R.id.rb_man:
                    httpinit("男");
                    break;
                case R.id.rb_nman:
                    httpinit("女");
                    break;
            }
        }
    };

    private void httpinit(final String sex) {
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/ChangeGender")
                .addParams("id", sp.getTag().getId() + "")
                .addParams("gender", sex)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(SexpageActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response.equals("0")) {
                            User_Http.user.setGender(sex);
                            finish();
                        } else {
                            Toast.makeText(SexpageActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
