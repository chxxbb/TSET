package com.example.chen.tset.View.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.IListener;
import com.example.chen.tset.Utils.ListenerManager;
import com.example.chen.tset.page.view.WordWrapView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Arrays;
import java.util.List;

import okhttp3.Call;

public class HealthparticularsActivity extends AppCompatActivity implements IListener {
    private String time;
    private String tag;
    private String content;
    private String healthId;
    private TextView tv_content, tv_time;
    private LinearLayout ll_health_return, ll_healthcondition_delete;
    private WordWrapView wordWrapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthparticulars);
        //注册通知广播，用于用户保存了今天的健康状况后通知诊疗页面刷新
        ListenerManager.getInstance().registerListtener(this);
        findView();
        init();
    }


    private void findView() {
        time = getIntent().getStringExtra("time");
        tag = getIntent().getStringExtra("tag");
        content = getIntent().getStringExtra("content");
        healthId = getIntent().getStringExtra("healthId");
        ll_health_return = (LinearLayout) findViewById(R.id.ll_health_return);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_time = (TextView) findViewById(R.id.tv_time);
        wordWrapView = (WordWrapView) findViewById(R.id.wordWrapView);
        ll_healthcondition_delete = (LinearLayout) findViewById(R.id.ll_healthcondition_delete);
        tv_time.setText(time);
        tv_content.setText(content);
        ll_health_return.setOnClickListener(listner);
        ll_healthcondition_delete.setOnClickListener(deltetlistener);


    }

    private void init() {

        String[] strArray = null;
        //去除逗号
        strArray = tag.split(",");
        List<String> wordList = Arrays.asList(strArray);


        //判断接收到的标签标记，并且显示对应的标签
        for (int i = 0; i < wordList.size(); i++) {
            View view = View.inflate(this, R.layout.health_page_disease_item, null);
            LinearLayout ll_heath_disease_case = (LinearLayout) view.findViewById(R.id.ll_heath_disease_case);
            TextView tv_heath_disease_case = (TextView) view.findViewById(R.id.tv_heath_disease_case);

            //.trim用于去除空格
            if (wordList.get(i).trim().equals("流涕") || wordList.get(i).trim().equals("咳嗽") || wordList.get(i).trim().equals("头疼")) {
                ll_heath_disease_case.setBackgroundResource(R.drawable.healthcondition_rbtn_unmep_greencase);
                tv_heath_disease_case.setText(wordList.get(i));
                tv_heath_disease_case.setTextColor(android.graphics.Color.parseColor("#a6cd57"));

            } else if (wordList.get(i).trim().equals("发热") || wordList.get(i).trim().equals("口腔溃疡") || wordList.get(i).trim().equals("口干口渴") || wordList.get(i).trim().equals("鼻塞")) {
                ll_heath_disease_case.setBackgroundResource(R.drawable.healthcondition_rbtn_unmep_yellowcase);
                tv_heath_disease_case.setText(wordList.get(i));
                tv_heath_disease_case.setTextColor(android.graphics.Color.parseColor("#ebc668"));


            } else {
                ll_heath_disease_case.setBackgroundResource(R.drawable.healthcondition_rbtn_unmep_redcase);
                tv_heath_disease_case.setText(wordList.get(i));
                tv_heath_disease_case.setTextColor(android.graphics.Color.parseColor("#eb686c"));

            }

            wordWrapView.addView(view);
        }
    }

    private View.OnClickListener listner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };


    private View.OnClickListener deltetlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            OkHttpUtils
                    .post()
                    .url(Http_data.http_data + "/DeleteHealthById")
                    .addParams("healthId", healthId)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Toast.makeText(HealthparticularsActivity.this, "网络连接失败", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (response.equals("0")) {
                                //发送广播通知诊疗页面更新
                                ListenerManager.getInstance().sendBroadCast("更新日历页面");
                                Toast.makeText(HealthparticularsActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(HealthparticularsActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    };


    @Override
    public void notifyAllActivity(String str) {

    }
}
