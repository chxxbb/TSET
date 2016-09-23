package com.example.chen.tset.View;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.Consult;
import com.example.chen.tset.Data.Consultparticulars;
import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.Reservationlist;
import com.example.chen.tset.Data.User;
import com.example.chen.tset.Data.User_Http;
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

/**
 * 资讯详情页面与收藏详情页面公用同一个接口
 */
public class ConsultPageActivity extends AppCompatActivity implements View.OnClickListener {
    private ScrollView scrollview;
    private LinearLayout ll_consult_return, ll_consult_collect;
    private TextView tv_title, tv_time, tv_content,tv_collsult,tv;
    private ImageView iv_icon,iv_collsult;
    private RelativeLayout rl_nonetwork, rl_loading;
    Gson gson = new Gson();
    String information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_page);
        findview();

    }

    private void findview() {
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        ll_consult_return = (LinearLayout) findViewById(R.id.ll_consult_return);
        ll_consult_collect = (LinearLayout) findViewById(R.id.ll_consult_collect);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        rl_nonetwork = (RelativeLayout) findViewById(R.id.rl_nonetwork);
        rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
        tv_collsult= (TextView) findViewById(R.id.tv_collsilt);
        iv_collsult= (ImageView) findViewById(R.id.iv_collsult);
        tv= (TextView) findViewById(R.id.tv);
        scrollview.setVerticalScrollBarEnabled(false);
        ll_consult_return.setOnClickListener(this);
        ll_consult_collect.setOnClickListener(this);
        String collect = getIntent().getStringExtra("collect");
        information = getIntent().getStringExtra("information");
        findCollectExistByUserIdAndCyclopediaId();
        //判断是否为收藏页面点击进入，如果是则隐藏下方收藏按钮
        if (collect.equals("1")) {
            ll_consult_collect.setVisibility(View.GONE);
        }
            informationinit();


    }
    //判断文章是否收藏过
    private void findCollectExistByUserIdAndCyclopediaId() {
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/FindCollectExistByUserIdAndCyclopediaId")
                .addParams("userId", User_Http.user.getId() + "")
                .addParams("cyclopediaId", information)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(ConsultPageActivity.this, "失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("13",response);
                        System.out.print(response);
                        if(response.equals("0")){
                            tv_collsult.setTextColor(android.graphics.Color.parseColor("#6fc9e6"));
                            iv_collsult.setBackgroundResource(R.drawable.consult_isonclickpraise);

                        }else {
                            tv_collsult.setTextColor(android.graphics.Color.parseColor("#999999"));
                            iv_collsult.setBackgroundResource(R.drawable.consult_unmeppraise);
                            tv_collsult.setText("已赞");
                            ll_consult_collect.setOnClickListener(null);


                        }
                    }
                });
    }



    //获取文章详情
    private void informationinit() {
        Log.e("文章ID",information);
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/FindCyclopediaById")
                .addParams("cyclopediaId", information)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        Toast.makeText(ConsultPageActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        rl_loading.setVisibility(View.GONE);
                        rl_nonetwork.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("咨询详情返回", response);
                        if (response.equals("[]") || response.equals("")) {

                            Toast.makeText(ConsultPageActivity.this, "没有这条新闻", Toast.LENGTH_SHORT).show();
                            rl_loading.setVisibility(View.GONE);
                            rl_nonetwork.setVisibility(View.VISIBLE);
                            ll_consult_collect.setVisibility(View.GONE);
                        } else {


                            Consultparticulars consultparticulars = gson.fromJson(response, Consultparticulars.class);
                            tv_title.setText(consultparticulars.getTitle());
                            tv_time.setText("天使资讯  " + consultparticulars.getTime());
                            tv_content.setText(consultparticulars.getContent());
                            ImageLoader.getInstance().displayImage(consultparticulars.getIcon(), iv_icon);
                            rl_loading.setVisibility(View.GONE);
                        }
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
                //收藏
                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/AddCollectByUserIdAndCyclopediaId")
                        .addParams("userId", User_Http.user.getId() + "")
                        .addParams("cyclopediaId", information)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Toast.makeText(ConsultPageActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.e("咨询详情收藏返回", response);
                                if (response.equals("0")) {
                                    Toast.makeText(ConsultPageActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                                    tv_collsult.setTextColor(android.graphics.Color.parseColor("#999999"));
                                    iv_collsult.setBackgroundResource(R.drawable.consult_unmeppraise);
                                    tv_collsult.setText("已赞");
                                    ll_consult_collect.setOnClickListener(null);
                                }

                            }
                        });
                break;


        }
    }
}
