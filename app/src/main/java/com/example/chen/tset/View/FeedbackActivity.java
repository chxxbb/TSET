package com.example.chen.tset.View;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
 * APP评价
 */
public class FeedbackActivity extends MyBaseActivity implements View.OnClickListener {
    private TextView tv_feedback, tv_cancel;
    private EditText et_feedback;
    private LinearLayout ll_et_feedback;

    SharedPsaveuser sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        sp=new SharedPsaveuser(this);
        findView();
    }

    private void findView() {
        tv_feedback = (TextView) findViewById(R.id.tv_feedback);
        et_feedback = (EditText) findViewById(R.id.et_feedback);
        ll_et_feedback= (LinearLayout) findViewById(R.id.ll_et_feedback);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        et_feedback.addTextChangedListener(textchanglistener);

        tv_cancel.setOnClickListener(this);
        ll_et_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_feedback.requestFocus();
                InputMethodManager imm = (InputMethodManager)FeedbackActivity .this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    private TextWatcher textchanglistener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (et_feedback.getText().toString().length() == 0) {
                tv_feedback.setTextColor(android.graphics.Color.parseColor("#e0e0e0"));
                tv_feedback.setOnClickListener(null);
            }else {
                tv_feedback.setOnClickListener(listener);
                tv_feedback.setTextColor(android.graphics.Color.parseColor("#6fc9e6"));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (et_feedback.getText().toString().length() < 2) {
                Toast.makeText(FeedbackActivity.this, "必须大于2个字", Toast.LENGTH_SHORT).show();
            } else {
                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/AddFeedback")
                        .addParams("userId", sp.getTag().getId()+"")
                        .addParams("content", et_feedback.getText().toString())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Toast.makeText(FeedbackActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onResponse(String response, int id) {
                                Log.e("反馈",response);
                                if (response.equals("0")) {
                                    Toast.makeText(FeedbackActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(FeedbackActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                finish();
                break;


        }
    }
}
