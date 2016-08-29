package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tv_feedback;
    private EditText et_feedback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        findView();
    }

    private void findView() {
        tv_feedback = (TextView) findViewById(R.id.tv_feedback);
        et_feedback = (EditText) findViewById(R.id.et_feedback);
        et_feedback.addTextChangedListener(textchanglistener);
        tv_feedback.setOnClickListener(this);
    }

    private TextWatcher textchanglistener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tv_feedback.setTextColor(android.graphics.Color.parseColor("#6fc9e6"));
            if (et_feedback.getText().toString().length() == 0) {
                tv_feedback.setTextColor(android.graphics.Color.parseColor("#e0e0e0"));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_feedback:
                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/addadvise"+"?1")
                        .addParams("content",et_feedback.getText().toString())
                        .addParams("time","时间")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Toast.makeText(FeedbackActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                if(response.equals("0")){
                                    Toast.makeText(FeedbackActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                }else if(response.equals("1")){
                                    Toast.makeText(FeedbackActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
        }
    }
}
