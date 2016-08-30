package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class GenderpageActivity extends AppCompatActivity {
    private LinearLayout ll_rutgender;
    private RadioButton rb_man, rb_nman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genderpage);
        findView();
    }

    private void findView() {
        ll_rutgender = (LinearLayout) findViewById(R.id.ll_rutgender);
        rb_man = (RadioButton) findViewById(R.id.rb_man);
        rb_nman = (RadioButton) findViewById(R.id.rb_nman);
        ll_rutgender.setOnClickListener(listener);
        rb_man.setOnClickListener(listener);
        rb_nman.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_rutgender:
                    finish();
                    break;
                case R.id.rb_man:
                    OkHttpUtils
                            .post()
                            .url(Http_data.http_data + "/changeSex")
                            .addParams("id", "2")
                            .addParams("sex", "男")
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Toast.makeText(GenderpageActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    Log.e("性别修改返回", response);
                                }
                            });
                    break;
                case R.id.rb_nman:
                    break;
            }
        }
    };
}
