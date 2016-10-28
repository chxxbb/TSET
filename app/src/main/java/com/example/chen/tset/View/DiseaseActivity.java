package com.example.chen.tset.View;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.Consultparticulars;
import com.example.chen.tset.Data.Disease;
import com.example.chen.tset.Data.DiseaseRecommendDoctor;
import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.Inquiry;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
 * 疾病详情页面
 */
public class DiseaseActivity extends MyBaseActivity {
    private LinearLayout ll_return;
    private ScrollView scrollView;
    private TextView tv_content, tv_acontent, tv_acontent1, tv_title, tv_title1, tv_bcontent, tv_dcontent, tv_dname, tv_uname, tv_section, tv_ucontent, tv_name, tv_dactor_title, tv_dactor_section, tv_intro;
    private CircleImageView iv_icon, iv_dicon;
    private RelativeLayout rl_nonetwork, rl_loading;
    Gson gson = new Gson();
    Disease disease;
    String disease1;
    DiseaseRecommendDoctor inquiry;
    List<Object> lis;
    CircleImageView iv_dactor_icon;
    private LinearLayout ll_doctor_recommend, doctor_recommend;
    Button btn_money;
    View view;
    private Dialog setHeadDialog;
    private View dialogView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease);
        findView();
        httpinit();
    }

    private void findView() {
        ll_return = (LinearLayout) findViewById(R.id.ll_return);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_acontent = (TextView) findViewById(R.id.tv_acontent);
        tv_acontent1 = (TextView) findViewById(R.id.tv_acontent1);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title1 = (TextView) findViewById(R.id.tv_title1);
        tv_bcontent = (TextView) findViewById(R.id.tv_bcontent);
        tv_dcontent = (TextView) findViewById(R.id.tv_dcontent);
        tv_dname = (TextView) findViewById(R.id.tv_dname);
        tv_uname = (TextView) findViewById(R.id.tv_uname);
        tv_section = (TextView) findViewById(R.id.tv_section);
        tv_ucontent = (TextView) findViewById(R.id.tv_ucontent);
        iv_icon = (CircleImageView) findViewById(R.id.iv_icon);
        iv_dicon = (CircleImageView) findViewById(R.id.iv_dicon);
        rl_nonetwork = (RelativeLayout) findViewById(R.id.rl_nonetwork);
        rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_dactor_title = (TextView) findViewById(R.id.tv_dactor_title);
        tv_dactor_section = (TextView) findViewById(R.id.tv_dactor_section);
        btn_money = (Button) findViewById(R.id.btn_money);
        tv_intro = (TextView) findViewById(R.id.tv_intro);
        iv_dactor_icon = (CircleImageView) findViewById(R.id.iv_dactor_icon);
        ll_doctor_recommend = (LinearLayout) findViewById(R.id.ll_doctor_recommend);
        doctor_recommend = (LinearLayout) findViewById(R.id.doctor_recommend);
        view = findViewById(R.id.view);
        //屏蔽listview滚动条
        scrollView.setVerticalScrollBarEnabled(false);
        ll_doctor_recommend.setOnClickListener(listener);
        ll_return.setOnClickListener(listener);
        btn_money.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.ll_return:
                    finish();
                    break;
                case R.id.ll_doctor_recommend:
                    //跳转至医生详情页面
                    Intent intent = new Intent(DiseaseActivity.this, DoctorparticularsActivity.class);
                    intent.putExtra("doctot_id", inquiry.getId() + "");
                    startActivity(intent);
                    break;
                case R.id.btn_money:
                    //设置dialog主题
                    setHeadDialog = new Dialog(DiseaseActivity.this, R.style.CustomDialog);

                    setHeadDialog.show();

                    //dialog显示布局
                    dialogView = View.inflate(DiseaseActivity.this, R.layout.inquiry_chat_dialog, null);

                    setHeadDialog.getWindow().setContentView(dialogView);

                    WindowManager.LayoutParams lp = setHeadDialog.getWindow().getAttributes();

                    setHeadDialog.getWindow().setAttributes(lp);

                    //确定进入聊天页面
                    RelativeLayout rl_confirm = (RelativeLayout) dialogView.findViewById(R.id.rl_confirm);


                    //取消进入聊天页面
                    RelativeLayout lr_cancel = (RelativeLayout) dialogView.findViewById(R.id.lr_cancel);


                    lr_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setHeadDialog.dismiss();
                        }
                    });

                    //跳转至聊天页面
                    rl_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(DiseaseActivity.this, ChatpageActivity.class);
                            //医生姓名
                            intent.putExtra("name", inquiry.getName());
                            //医生头像
                            intent.putExtra("icon", inquiry.getIcon());
                            //医生ID
                            intent.putExtra("doctorID", inquiry.getId()+"");
                            //医生聊天账号
                            intent.putExtra("username", inquiry.getUsername());
                            startActivity(intent);
                            setHeadDialog.dismiss();
                        }
                    });
                    break;

            }
        }
    };


    //疾病详情，推荐医生数据
    private void httpinit() {
        disease1 = getIntent().getStringExtra("disease");
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/FindDiseaseByName")
                        .addParams("diseaseName", disease1)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                                handler.sendEmptyMessage(0);

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.e("疾病详情返回", response);
                                if (response.equals("[]") || response.equals("") || response == null) {
                                    handler.sendEmptyMessage(1);
                                } else {
                                    Type type = new TypeToken<List<Object>>() {
                                    }.getType();
                                    lis = gson.fromJson(response, type);

                                    String disease_str = gson.toJson(lis.get(0));

                                    String doctor_str = gson.toJson(lis.get(1));


                                    //疾病数据
                                    disease = gson.fromJson(disease_str, Disease.class);

                                    //推荐医生数据
                                    inquiry = gson.fromJson(doctor_str, DiseaseRecommendDoctor.class);

                                    Log.e("11", inquiry.toString());

                                    handler.sendEmptyMessage(2);


                                }
                            }
                        });
            }
        }).start();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(DiseaseActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    rl_loading.setVisibility(View.GONE);
                    rl_nonetwork.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    Toast.makeText(DiseaseActivity.this, "没有这个疾病", Toast.LENGTH_SHORT).show();
                    rl_loading.setVisibility(View.GONE);
                    rl_nonetwork.setVisibility(View.VISIBLE);
                    break;

                case 2:

                    tv_content.setText("        " + disease.getBio());
                    tv_acontent.setText("        " + disease.getCure());
                    if (disease.getPrompt() == null) {
                        tv_acontent1.setText("无");
                    } else {
                        tv_acontent1.setText("        " + disease.getPrompt());
                    }
                    tv_title.setText(disease1);
                    tv_title1.setText(disease1);
                    tv_bcontent.setText("        " + disease.getSymptom());
                    tv_dcontent.setText("        " + disease.getDoctorAnswerQuestion());
                    tv_dname.setText(disease.getDoctorName());
                    tv_uname.setText(disease.getUserName());
                    tv_section.setText(disease.getSectionName());
                    tv_ucontent.setText("        " + disease.getUserPutQuestion());


                    //如果ID为0，则表示没有医生推荐，则隐藏医生推荐栏
                    if (inquiry.getId() == 0) {
                        ll_doctor_recommend.setVisibility(View.GONE);
                        doctor_recommend.setVisibility(View.GONE);
                        view.setVisibility(View.GONE);
                    } else {
                        tv_name.setText(inquiry.getName());
                        tv_dactor_title.setText(inquiry.getTitle());
                        tv_dactor_section.setText(inquiry.getSection());
                        btn_money.setText("￥" + inquiry.getChatCost());
                        tv_intro.setText("擅长:" + inquiry.getAdept());
                        ImageLoader.getInstance().displayImage(inquiry.getIcon(), iv_dactor_icon);
                    }


                    ImageLoader.getInstance().displayImage(disease.getUserIcon(), iv_icon);
                    ImageLoader.getInstance().displayImage(disease.getDoctorIcon(), iv_dicon);

                    rl_loading.setVisibility(View.GONE);
                    break;


            }
        }
    };
}
