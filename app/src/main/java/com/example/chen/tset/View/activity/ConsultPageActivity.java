package com.example.chen.tset.View.activity;

import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.entity.Consultparticulars;
import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.IListener;
import com.example.chen.tset.Utils.ListenerManager;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * 资讯详情页面与收藏详情页面公用同一个接口
 */
public class ConsultPageActivity extends MyBaseActivity implements View.OnClickListener, IListener {
    private ScrollView scrollview;
    private LinearLayout ll_consult_return, ll_consult_collect;
    private TextView tv_title, tv_time, tv_content, tv_collsult, tv;
    private ImageView iv_icon, iv_collsult;
    private RelativeLayout rl_nonetwork, rl_loading;
    Gson gson = new Gson();
    String information;
    Consultparticulars consultparticulars;
    String collect;

    SharedPsaveuser sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_page);
        sp = new SharedPsaveuser(ConsultPageActivity.this);

        //注册广播接口，点用户点击取消点赞时用于刷新 我的收藏页面
        ListenerManager.getInstance().registerListtener(this);
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
        tv_collsult = (TextView) findViewById(R.id.tv_collsilt);
        iv_collsult = (ImageView) findViewById(R.id.iv_collsult);
        tv = (TextView) findViewById(R.id.tv);
        scrollview.setVerticalScrollBarEnabled(false);
        ll_consult_return.setOnClickListener(this);
        ll_consult_collect.setOnClickListener(collectlistener);
        collect = getIntent().getStringExtra("collect");
        information = getIntent().getStringExtra("information");
        findCollectExistByUserIdAndCyclopediaId();


        informationinit();


    }

    //判断文章是否收藏过,已更改为无论是否收藏过都显示点赞按钮
    private void findCollectExistByUserIdAndCyclopediaId() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/FindCollectExistByUserIdAndCyclopediaId")
                        .addParams("userId", sp.getTag().getId() + "")
                        .addParams("cyclopediaId", information)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                handler.sendEmptyMessage(0);
                            }

                            @Override
                            public void onResponse(String response, int id) {


                                if (response.equals("0")) {
                                    handler.sendEmptyMessage(1);


                                } else {
                                    handler.sendEmptyMessage(2);


                                }
                            }
                        });
            }
        }).start();

    }


    //获取文章详情
    private void informationinit() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/FindCyclopediaById")
                        .addParams("cyclopediaId", information)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                handler.sendEmptyMessage(3);

                            }

                            @Override
                            public void onResponse(String response, int id) {

                                if (response.equals("[]") || response.equals("")) {
                                    handler.sendEmptyMessage(4);

                                } else {
                                    consultparticulars = gson.fromJson(response, Consultparticulars.class);
                                    handler.sendEmptyMessage(5);

                                }
                            }
                        });

            }
        }).start();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_consult_return:
                finish();
                break;


        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    ll_consult_collect.setVisibility(View.GONE);
                    Toast.makeText(ConsultPageActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case 1:

                    tv_collsult.setTextColor(android.graphics.Color.parseColor("#6fc9e6"));
                    iv_collsult.setBackgroundResource(R.drawable.consult_isonclickpraise);
                    break;
                case 2:

                    tv_collsult.setTextColor(android.graphics.Color.parseColor("#999999"));
                    iv_collsult.setBackgroundResource(R.drawable.consult_unmeppraise);
                    tv_collsult.setText("已赞");
                    ll_consult_collect.setOnClickListener(unfavoritelistener);
                    break;
                case 3:

                    rl_loading.setVisibility(View.GONE);
                    rl_nonetwork.setVisibility(View.VISIBLE);
                    break;
                case 4:

                    Toast.makeText(ConsultPageActivity.this, "没有这条新闻", Toast.LENGTH_SHORT).show();
                    rl_loading.setVisibility(View.GONE);
                    rl_nonetwork.setVisibility(View.VISIBLE);
                    ll_consult_collect.setVisibility(View.GONE);
                    break;
                case 5:

                    tv_title.setText(consultparticulars.getTitle());
                    tv_time.setText("天使资讯  " + consultparticulars.getTime());
                    tv_content.setText("        " + consultparticulars.getContent() + " \n" + " \n" + "\n");
                    ImageLoader.getInstance().displayImage(consultparticulars.getIcon(), iv_icon);
                    rl_loading.setVisibility(View.GONE);

                    break;

                case 6:
                    Toast.makeText(ConsultPageActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                    tv_collsult.setTextColor(android.graphics.Color.parseColor("#999999"));
                    iv_collsult.setBackgroundResource(R.drawable.consult_unmeppraise);
                    tv_collsult.setText("已赞");
                    ll_consult_collect.setOnClickListener(unfavoritelistener);
                    break;


            }
        }
    };

    //点击点赞收藏资讯
    private View.OnClickListener collectlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //收藏
                    OkHttpUtils
                            .post()
                            .url(Http_data.http_data + "/AddCollectByUserIdAndCyclopediaId")
                            .addParams("userId", sp.getTag().getId() + "")
                            .addParams("cyclopediaId", information)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    handler.sendEmptyMessage(0);
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    if (response.equals("0")) {
                                        handler.sendEmptyMessage(6);

                                    }

                                }
                            });
                }
            }).start();

        }
    };

    //如果以收藏则显示“已赞”,再次点击则取消收藏
    private View.OnClickListener unfavoritelistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            OkHttpUtils
                    .post()
                    .url(Http_data.http_data + "/DeleteCollectByUserIdAndCyclopediaId")
                    .addParams("cyclopediaId", information)
                    .addParams("userId", sp.getTag().getId() + "")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Toast.makeText(ConsultPageActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (response.equals("0")) {
                                //取消收藏后发生广播通知我的收藏更新数据，“点赞”按钮改变
                                ListenerManager.getInstance().sendBroadCast("更新我的收藏");
                                Toast.makeText(ConsultPageActivity.this, "删除成功", Toast.LENGTH_SHORT).show();

                                tv_collsult.setTextColor(android.graphics.Color.parseColor("#6fc9e6"));

                                iv_collsult.setBackgroundResource(R.drawable.consult_isonclickpraise);

                                tv_collsult.setText("赞");
                                ll_consult_collect.setOnClickListener(collectlistener);

                            }
                        }
                    });

        }
    };


    @Override
    public void notifyAllActivity(String str) {

    }
}
