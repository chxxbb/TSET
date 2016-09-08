package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class EvaluatepageActivity extends AppCompatActivity {
    private TextView tv_evaluate;
    private EditText et_evaluate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluatepage);
        findView();
    }

    private void findView() {
        tv_evaluate= (TextView) findViewById(R.id.tv_evaluate);
        et_evaluate= (EditText) findViewById(R.id.et_evaluate);
        et_evaluate.addTextChangedListener(textlistener);
        tv_evaluate.setOnClickListener(listener);
    }
    private TextWatcher textlistener=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (et_evaluate.getText().toString().length() != 0) {
                tv_evaluate.setTextColor(android.graphics.Color.parseColor("#6fc9e6"));
            } else {
                tv_evaluate.setTextColor(android.graphics.Color.parseColor("#e0e0e0"));
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
                    .url(Http_data.http_data + "/addappraise")
                    .addParams("user_id","3")
                    .addParams("doctor_id","3")
                    .addParams("content",et_evaluate.getText().toString())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Toast.makeText(EvaluatepageActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.e("评价返回",response);
                        }
                    });
        }
    };
}
