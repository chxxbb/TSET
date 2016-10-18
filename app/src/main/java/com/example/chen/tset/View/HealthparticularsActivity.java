package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chen.tset.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HealthparticularsActivity extends AppCompatActivity {
    private String time;
    private String tag;
    private String content;
    private TextView tv_content, tv_time;
    private LinearLayout ll_health_return, ll_tag1, ll_tag2, ll_tag3, ll_tag4, ll_tag5, ll_tag6, ll_tag7, ll_tag8, ll_tag9, ll_tag10, ll_tag11, ll_tag12, ll_tag13, ll_tag14;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthparticulars);
        findView();
        init();
    }


    private void findView() {
        time = getIntent().getStringExtra("time");
        tag = getIntent().getStringExtra("tag");
        content = getIntent().getStringExtra("content");
        ll_health_return = (LinearLayout) findViewById(R.id.ll_health_return);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_time = (TextView) findViewById(R.id.tv_time);
        ll_tag1 = (LinearLayout) findViewById(R.id.ll_tag1);
        ll_tag2 = (LinearLayout) findViewById(R.id.ll_tag2);
        ll_tag3 = (LinearLayout) findViewById(R.id.ll_tag3);
        ll_tag4 = (LinearLayout) findViewById(R.id.ll_tag4);
        ll_tag5 = (LinearLayout) findViewById(R.id.ll_tag5);
        ll_tag6 = (LinearLayout) findViewById(R.id.ll_tag6);
        ll_tag7 = (LinearLayout) findViewById(R.id.ll_tag7);
        ll_tag8 = (LinearLayout) findViewById(R.id.ll_tag8);
        ll_tag9 = (LinearLayout) findViewById(R.id.ll_tag9);
        ll_tag10 = (LinearLayout) findViewById(R.id.ll_tag10);
        ll_tag11 = (LinearLayout) findViewById(R.id.ll_tag11);
        ll_tag12 = (LinearLayout) findViewById(R.id.ll_tag12);
        ll_tag13 = (LinearLayout) findViewById(R.id.ll_tag13);
        ll_tag14 = (LinearLayout) findViewById(R.id.ll_tag14);
        tv_time.setText(time);
        tv_content.setText(content);
        ll_health_return.setOnClickListener(listner);


    }

    private void init() {
        String[] strArray = null;
        strArray = tag.split(",");
        List<String> wordList = Arrays.asList(strArray);


        for (int i = 0; i < wordList.size(); i++) {
            if (wordList.get(i).trim().equals("流涕")) {
                ll_tag1.setVisibility(View.VISIBLE);
            } else if (wordList.get(i).trim().equals("咳嗽")) {
                ll_tag2.setVisibility(View.VISIBLE);
            } else if (wordList.get(i).trim().equals("头疼")) {
                ll_tag3.setVisibility(View.VISIBLE);
            } else if (wordList.get(i).trim().equals("发热")) {
                ll_tag4.setVisibility(View.VISIBLE);
            } else if (wordList.get(i).trim().equals("口腔溃疡")) {
                ll_tag5.setVisibility(View.VISIBLE);
            } else if (wordList.get(i).trim().equals("口干口渴")) {
                ll_tag6.setVisibility(View.VISIBLE);
            } else if (wordList.get(i).trim().equals("鼻塞")) {
                ll_tag7.setVisibility(View.VISIBLE);
            } else if (wordList.get(i).trim().equals("咽疼")) {
                ll_tag8.setVisibility(View.VISIBLE);
            } else if (wordList.get(i).trim().equals("出疹")) {
                ll_tag9.setVisibility(View.VISIBLE);
            } else if (wordList.get(i).trim().equals("睡眠问题")) {
                ll_tag10.setVisibility(View.VISIBLE);
            } else if (wordList.get(i).trim().equals("腹泻")) {
                ll_tag11.setVisibility(View.VISIBLE);
            } else if (wordList.get(i).trim().equals("腹疼")) {
                ll_tag12.setVisibility(View.VISIBLE);
            } else if (wordList.get(i).trim().equals("腹胀")) {
                ll_tag13.setVisibility(View.VISIBLE);
            } else if (wordList.get(i).trim().equals("呕吐")) {
                ll_tag14.setVisibility(View.VISIBLE);
            }
        }

    }

    private View.OnClickListener listner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}
