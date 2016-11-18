package com.example.chen.tset.View.activity;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.entity.MyDoctor;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.example.chen.tset.page.adapter.MyDoctorAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;

/**
 * 我的医生
 */
public class MyDoctorActivity extends MyBaseActivity {
    private SwipeMenuListView lv_mydoctor;
    Gson gson;
    MyDoctorAdapter adapter;
    List<MyDoctor> list;
    private LinearLayout ll_rut;
    private RelativeLayout rl_nonetwork, rl_loading;
    SharedPsaveuser sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_doctor);
        findView();
        init();
        httpinit();
    }

    private void findView() {
        lv_mydoctor = (SwipeMenuListView) findViewById(R.id.lv_mydoctor);
        ll_rut = (LinearLayout) findViewById(R.id.ll_rut);
        rl_nonetwork = (RelativeLayout) findViewById(R.id.rl_nonetwork);
        rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
        ll_rut.setOnClickListener(listener);
        lv_mydoctor.setOnItemClickListener(lvlistener);
        lv_mydoctor.setVerticalScrollBarEnabled(false);

        DisplayMetrics dm = new DisplayMetrics();
        dm = this.getResources().getDisplayMetrics();
        final float density = dm.density;

        //listview的item向左滑动可出现删除收藏按钮
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                //设置删除收藏按钮长宽，颜色，字体
                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                openItem.setBackground(new ColorDrawable(Color.RED));
                openItem.setWidth((int) (100 * density));
                openItem.setTitle("删除");
                openItem.setTitleSize(17);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);
            }
        };

        lv_mydoctor.setMenuCreator(creator);
        lv_mydoctor.setOnMenuItemClickListener(onmentlistener);
    }

    private void init() {
        sp = new SharedPsaveuser(this);
        list = new ArrayList<>();
        adapter = new MyDoctorAdapter(MyDoctorActivity.this, list);
        lv_mydoctor.setAdapter(adapter);


    }


    //滑动删除我的医生
    private SwipeMenuListView.OnMenuItemClickListener onmentlistener = new SwipeMenuListView.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
            OkHttpUtils
                    .post()
                    .url(Http_data.http_data + "/DeleteMyDoctor")
                    .addParams("userId", sp.getTag().getId() + "")
                    .addParams("doctorId", list.get(position).getDoctorId() + "")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Toast.makeText(MyDoctorActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (response.equals("0")) {
                                Toast.makeText(MyDoctorActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                list.clear();
                                httpinit();
                            } else {
                                Toast.makeText(MyDoctorActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            return false;
        }
    };


    //我的医生数据
    private void httpinit() {

        gson = new Gson();
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/FindDoctorListByUserId")
                .addParams("userId", sp.getTag().getId() + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(MyDoctorActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        rl_loading.setVisibility(View.GONE);
                        rl_nonetwork.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        Type listtype = new TypeToken<LinkedList<MyDoctor>>() {
                        }.getType();
                        LinkedList<MyDoctor> leclist = gson.fromJson(response, listtype);
                        for (Iterator it = leclist.iterator(); it.hasNext(); ) {
                            MyDoctor inquiry = (MyDoctor) it.next();
                            list.add(inquiry);
                        }
                        adapter.notifyDataSetChanged();
                        rl_loading.setVisibility(View.GONE);
                    }
                });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:

                    break;
                case 1:

                    break;

            }
        }
    };

    private AdapterView.OnItemClickListener lvlistener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MyDoctorActivity.this, DoctorparticularsActivity.class);
            //医生ID，用于在医生详情页面获取医生详情
            intent.putExtra("doctot_id", list.get(position).getDoctorId() + "");
            startActivity(intent);
        }
    };


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}
