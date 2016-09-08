package com.example.chen.tset.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.Consult;
import com.example.chen.tset.Data.Consultparticulars;
import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.Reservationlist;
import com.example.chen.tset.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;

public class ConsultPageActivity extends AppCompatActivity implements View.OnClickListener {
    private ScrollView scrollview;
    private LinearLayout ll_consult_return, ll_consult_collect;
    private TextView tv_title, tv_time, tv_content;
    private ImageView iv_icon;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_page);
        findview();
        httpinit();
    }

    private void findview() {
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        ll_consult_return = (LinearLayout) findViewById(R.id.ll_consult_return);
        ll_consult_collect = (LinearLayout) findViewById(R.id.ll_consult_collect);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        scrollview.setVerticalScrollBarEnabled(false);
        ll_consult_return.setOnClickListener(this);
        ll_consult_collect.setOnClickListener(this);
        String collect = getIntent().getStringExtra("collect");
        if (collect.equals("1")) {
            ll_consult_collect.setVisibility(View.GONE);
        }
    }

    private void httpinit() {
        gson = new Gson();
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/findCollect")
                .addParams("id", "15")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(ConsultPageActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("咨询详情返回", response);
                        Type listtype = new TypeToken<LinkedList<Consultparticulars>>() {
                        }.getType();
                        LinkedList<Consultparticulars> leclist = gson.fromJson(response, listtype);
                        Iterator it = leclist.iterator();
                        Consultparticulars consultparticulars = (Consultparticulars) it.next();
                        tv_title.setText(consultparticulars.getTitle());
                        tv_time.setText("天使资讯  " + consultparticulars.getTime());
                        tv_content.setText(consultparticulars.getContent());
                        ImageLoader.getInstance().displayImage(consultparticulars.getIcon(), iv_icon);
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_consult_return:
                finish();
                break;
            case R.id.ll_consult_collect:
                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/AddCollect")
                        .addParams("userId", "1")
                        .addParams("cyclopediaId", "1")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Toast.makeText(ConsultPageActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.e("咨询详情收藏返回", response);
                                if (response.equals("1")) {
                                    Toast.makeText(ConsultPageActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;


        }
    }
}
