package com.example.chen.tset.View.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.entity.Doctor;
import com.example.chen.tset.Data.entity.Doctorcomment;
import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.example.chen.tset.page.adapter.DoctorparticularsAdapter;
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

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * 医生详情页面，与我的医生详情，问诊详情共用同一个接口
 */
public class DoctorparticularsActivity extends MyBaseActivity {
    private ListView lv_docttorparticulas;
    private DoctorparticularsAdapter adapter;
    private LinearLayout ll_return;
    private View view;
    List<Doctorcomment> list;
    List<Doctor> list1;
    Gson gson = new Gson();
    private TextView tv_title, tv_name, tv_hospital, tv_bioo, tv_bis, tv_bit, tv_bif, tv_sum, tv_adept, tv_grade;

    private TextView btn_chatmoney, btn_callmoney;
    private CircleImageView iv_icon;
    private RelativeLayout rl_nonetwork, rl_loading;
    String doctor_id = null;
    Doctor doctor;

    private LinearLayout ll_chatmoney;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorparticulars);

        findView();
        httpinit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        comment();
    }

    private void findView() {
        doctor_id = getIntent().getStringExtra("doctot_id");
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_name = (TextView) findViewById(R.id.tv_name);
        btn_chatmoney = (TextView) findViewById(R.id.btn_chatmoney);
        btn_callmoney = (TextView) findViewById(R.id.btn_callmoney);
        tv_hospital = (TextView) findViewById(R.id.tv_hospital);
        iv_icon = (CircleImageView) findViewById(R.id.iv_icon);
        lv_docttorparticulas = (ListView) findViewById(R.id.lv_docttorparticulas);
        ll_return = (LinearLayout) findViewById(R.id.ll_return);
        //添加listview头部
        view = View.inflate(this, R.layout.doctorparticulars_listv_headerview, null);
        lv_docttorparticulas.addHeaderView(view);
        tv_bif = (TextView) view.findViewById(R.id.tv_bif);
        tv_bioo = (TextView) view.findViewById(R.id.tv_bioo);
        tv_bit = (TextView) view.findViewById(R.id.tv_bit);
        tv_bis = (TextView) view.findViewById(R.id.tv_bis);
        tv_sum = (TextView) view.findViewById(R.id.tv_sum);
        tv_grade = (TextView) view.findViewById(R.id.tv_grade);
        tv_adept = (TextView) view.findViewById(R.id.tv_adept);
        rl_nonetwork = (RelativeLayout) findViewById(R.id.rl_nonetwork);
        rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
        ll_chatmoney = (LinearLayout) findViewById(R.id.ll_chatmoney);
        lv_docttorparticulas.setVerticalScrollBarEnabled(false);
        list = new ArrayList<>();
        adapter = new DoctorparticularsAdapter(this, list);
        lv_docttorparticulas.setAdapter(adapter);
        ll_return.setOnClickListener(listener);
        ll_chatmoney.setOnClickListener(listener);


    }

    //医生信息
    private void httpinit() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/FindDoctorById")
                        .addParams("doctorId", doctor_id)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                handler.sendEmptyMessage(0);

                            }

                            @Override
                            public void onResponse(String response, int id) {

                                doctor = gson.fromJson(response, Doctor.class);

                                handler.sendEmptyMessage(1);

                            }
                        });
            }
        }).start();
        list1 = new ArrayList<>();

    }

    //医生评论
    private void comment() {
        new Thread(new Runnable() {
            @Override
            public void run() {


                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/FindDoctorCommentByDoctorId")
                        .addParams("doctorId", doctor_id)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                handler.sendEmptyMessage(2);

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Type listtype = new TypeToken<LinkedList<Doctorcomment>>() {
                                }.getType();
                                LinkedList<Doctorcomment> leclist = gson.fromJson(response, listtype);
                                for (Iterator it = leclist.iterator(); it.hasNext(); ) {
                                    Doctorcomment doctorcomment = (Doctorcomment) it.next();
                                    list.add(doctorcomment);
                                }

                                handler.sendEmptyMessage(3);

                            }
                        });
            }
        }).start();

    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {


                case R.id.ll_return:
                    finish();
                    break;
                case R.id.ll_chatmoney:
                    Intent intent = new Intent(DoctorparticularsActivity.this, ChatpageActivity.class);
                    intent.putExtra("name", doctor.getName());
                    intent.putExtra("icon", doctor.getIcon());
                    intent.putExtra("doctorID", doctor_id);
                    intent.putExtra("username", doctor.getUsername());
                    startActivity(intent);

                    break;


            }
        }
    };


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:

                    Toast.makeText(DoctorparticularsActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    rl_loading.setVisibility(View.GONE);
                    rl_nonetwork.setVisibility(View.VISIBLE);
                    view.setVisibility(View.GONE);
                    lv_docttorparticulas.setVisibility(View.GONE);
                    lv_docttorparticulas.setBackgroundColor(android.graphics.Color.parseColor("#ffffff"));
                    list = null;
                    break;

                case 1:

                    tv_name.setText(doctor.getName());
                    tv_title.setText(doctor.getTitle());
                    tv_hospital.setText(doctor.getHospital());
                    btn_callmoney.setText("￥" + doctor.getCallCost() + "/10分钟");
                    btn_chatmoney.setText("￥" + doctor.getChatCost() + "/次");
                    ImageLoader.getInstance().displayImage(doctor.getIcon(), iv_icon);
                    tv_bioo.setText(doctor.getSeniority1());
                    tv_bis.setText(doctor.getSeniority2());
                    tv_bit.setText(doctor.getSeniority3());
                    tv_bif.setText(doctor.getSeniority4());
                    tv_sum.setText("用户评论 （" + doctor.getCommentCount() + "人）");
                    tv_adept.setText(doctor.getAdept());
                    rl_loading.setVisibility(View.GONE);
                    break;

                case 2:

                    lv_docttorparticulas.setBackgroundColor(android.graphics.Color.parseColor("#ffffff"));
                    break;
                case 3:

                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    /**
     * 华丽的分割线
     */

//    TextView descriptionView;
//    View expandView;
//    int maxDescripLine = 1;
//    RelativeLayout rl_look_full_introduce;
//    TextView tv_doctor_look_all;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
//
//        setContentView(R.layout.activity_doctorparticulars);
//
//        findView();
//
//        lookfullintroduce();
//    }
//
//
//    private void findView() {
//        descriptionView = (TextView) findViewById(R.id.description_view);
//        rl_look_full_introduce = (RelativeLayout) findViewById(R.id.rl_look_full_introduce);
//        tv_doctor_look_all = (TextView) findViewById(R.id.tv_doctor_look_all);
//        descriptionView.setText("三甲医院主任医生 \n四甲医院主任医生  \n五甲医院主任医生 \n六甲医院主任医生 \n七甲医院主任医生");
//        expandView = findViewById(R.id.expand_view);
//        descriptionView.setHeight(descriptionView.getLineHeight() * maxDescripLine);
//    }
//
//    private void lookfullintroduce() {
//        descriptionView.post(new Runnable() {
//
//            @Override
//            public void run() {
//                expandView.setVisibility(descriptionView.getLineCount() > maxDescripLine ? View.VISIBLE : View.GONE);
//
//            }
//        });
//        rl_look_full_introduce.setOnClickListener(new View.OnClickListener() {
//            boolean isExpand;
//
//            @Override
//            public void onClick(View v) {
//                isExpand = !isExpand;
//                descriptionView.clearAnimation();
//                final int deltaValue;
//                final int startValue = descriptionView.getHeight();
//                int durationMillis = 350;
//                if (isExpand) {
//                    deltaValue = descriptionView.getLineHeight() * descriptionView.getLineCount() - startValue;
//                    RotateAnimation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                    animation.setDuration(durationMillis);
//                    animation.setFillAfter(true);
//                    expandView.startAnimation(animation);
//                    tv_doctor_look_all.setText("收回");
//
//                } else {
//
//                    deltaValue = descriptionView.getLineHeight() * maxDescripLine - startValue;
//                    RotateAnimation animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                    animation.setDuration(durationMillis);
//                    animation.setFillAfter(true);
//                    expandView.startAnimation(animation);
//                    tv_doctor_look_all.setText("完整介绍");
//                }
//                Animation animation = new Animation() {
//                    protected void applyTransformation(float interpolatedTime, Transformation t) {
//                        descriptionView.setHeight((int) (startValue + deltaValue * interpolatedTime));
//                    }
//
//                };
//                animation.setDuration(durationMillis);
//                descriptionView.startAnimation(animation);
//                descriptionView.setAutoLinkMask(1);
//
//            }
//        });
//
//    }


}
