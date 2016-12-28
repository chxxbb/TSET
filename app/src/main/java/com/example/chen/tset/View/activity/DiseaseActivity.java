package com.example.chen.tset.View.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.entity.Disease;
import com.example.chen.tset.Data.entity.DiseaseRecommendDoctor;
import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.IListener;
import com.example.chen.tset.Utils.ListenerManager;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.example.chen.tset.Utils.MyBaseActivity1;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * 疾病详情页面
 */
public class DiseaseActivity extends MyBaseActivity1 {
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


    RadioButton rb_wenx;

    RadioButton rb_zhifb;

    LinearLayout ll_cancel;

    Button btn_confirm_payment;

    ProgressBar progressBar;

    TextView tv_cash_coupons_stater;

    RelativeLayout rl_use_cash_coupons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease);

        try {

            findView();
            httpinit();


        } catch (Exception e) {
            e.printStackTrace();
        }
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

                            setHeadDialog.dismiss();

                            payDialog();
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
                    if (disease.getBio() == null) {
                        tv_content.setText("无");
                    } else {
                        tv_content.setText("        " + disease.getBio());
                    }

                    if (disease.getCure() == null) {
                        tv_acontent.setText("无");
                    } else {
                        tv_acontent.setText("        " + disease.getCure());
                    }


                    if (disease.getPrompt() == null) {
                        tv_acontent1.setText("无");
                    } else {
                        tv_acontent1.setText("        " + disease.getPrompt());
                    }


                    tv_title.setText(disease1);
                    tv_title1.setText(disease1);

                    if (disease.getSymptom() == null) {
                        tv_bcontent.setText("无");
                    } else {
                        tv_bcontent.setText("        " + disease.getSymptom());
                    }


                    if (disease.getDoctorAnswerQuestion() == null) {
                        tv_dcontent.setText("无");
                    } else {
                        tv_dcontent.setText("" + disease.getDoctorAnswerQuestion());
                    }

                    if (disease.getDoctorName() == null) {
                        tv_dname.setText("无");
                    } else {
                        tv_dname.setText(disease.getDoctorName());
                    }


                    if (disease.getUserName() == null) {
                        tv_uname.setText("无");
                    } else {
                        tv_uname.setText(disease.getUserName());
                    }


                    if (disease.getSectionName() == null) {
                        tv_section.setText("无");
                    } else {
                        tv_section.setText(disease.getSectionName());
                    }


                    if (disease.getUserPutQuestion() == null) {
                        tv_ucontent.setText("无");
                    } else {
                        tv_ucontent.setText("" + disease.getUserPutQuestion());
                    }


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

                        if (inquiry.getAdept() == null || inquiry.getAdept().equals("")) {
                            tv_intro.setText("");
                        } else {
                            tv_intro.setText("擅长：" + inquiry.getAdept());
                        }

                        if (inquiry.getIcon() == null || inquiry.getIcon().equals("")) {
                            iv_dactor_icon.setImageResource(R.drawable.default_icon);
                        } else {
                            ImageLoader.getInstance().displayImage(inquiry.getIcon(), iv_dactor_icon);
                        }

                    }


                    ImageLoader.getInstance().displayImage(disease.getUserIcon(), iv_icon);
                    ImageLoader.getInstance().displayImage(disease.getDoctorIcon(), iv_dicon);

                    rl_loading.setVisibility(View.GONE);
                    break;


            }
        }
    };


    //支付弹出框
    private void payDialog() {

        setHeadDialog = new Dialog(this, R.style.CustomDialog);
        setHeadDialog.show();
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        dialogView = View.inflate(this, R.layout.payment_dialog, null);


        rb_wenx = (RadioButton) dialogView.findViewById(R.id.rb_wenx);
        rb_zhifb = (RadioButton) dialogView.findViewById(R.id.rb_zhifb);
        ll_cancel = (LinearLayout) dialogView.findViewById(R.id.ll_cancel);
        tv_cash_coupons_stater = (TextView) dialogView.findViewById(R.id.tv_cash_coupons_stater);

        rl_use_cash_coupons = (RelativeLayout) dialogView.findViewById(R.id.rl_use_cash_coupons);


        //确认支付
        btn_confirm_payment = (Button) dialogView.findViewById(R.id.btn_confirm_payment);

        tv_cash_coupons_stater.setText("快速问诊劵 ￥25");
        btn_confirm_payment.setText("确认支付 ￥0");

        rb_wenx.setChecked(true);
        progressBar = (ProgressBar) dialogView.findViewById(R.id.progressBar);
        setHeadDialog.getWindow().setContentView(dialogView);
        WindowManager.LayoutParams lp = setHeadDialog.getWindow().getAttributes();
        lp.width = display.getWidth();
        setHeadDialog.getWindow().setAttributes(lp);

        //设置支付时间1800后未支付则关闭
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int progressBarMax = progressBar.getMax();
                try {
                    //设置progressBar时间
                    while (progressBarMax != progressBar.getProgress()) {
                        int stepProgress = progressBarMax / 1000;
                        int currentprogress = progressBar.getProgress();
                        progressBar.setProgress(currentprogress + stepProgress);
                        Thread.sleep(180);
                    }
                    setHeadDialog.dismiss();

                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }
        });
        thread.start();

        paydialogonclick();

    }


    //支付点击事件
    private void paydialogonclick() {
        rb_zhifb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_wenx.setChecked(false);
            }
        });
        rb_wenx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_zhifb.setChecked(false);
            }
        });
        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
            }
        });


        //确认支付
        btn_confirm_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_confirm_payment.getText().toString().equals("确认支付 ￥25")) {
                    Toast.makeText(DiseaseActivity.this, "请支付", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(DiseaseActivity.this, ChatpageActivity.class);
                    //医生姓名
                    intent.putExtra("name", inquiry.getName());
                    //医生头像
                    intent.putExtra("icon", inquiry.getIcon());
                    //医生ID
                    intent.putExtra("doctorID", inquiry.getId() + "");
                    //医生聊天账号
                    intent.putExtra("username", inquiry.getUsername());

                    intent.putExtra("page", "2");
                    startActivity(intent);
                    setHeadDialog.dismiss();
                }
            }
        });


        rl_use_cash_coupons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiseaseActivity.this, MyCashCouponsActivity.class);
                intent.putExtra("type", "pay");
                startActivity(intent);
            }
        });

    }


    //从现金卷页面收到的广播，如果使用了现金卷则修改现金卷item和去人按钮
//    @Override
//    public void notifyAllActivity(String str) {
//        if (str.equals("更新疾病详情问诊支付弹出框")) {
//            try {
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                dialogView = View.inflate(this, R.layout.payment_dialog, null);
//                tv_cash_coupons_stater = (TextView) dialogView.findViewById(R.id.tv_cash_coupons_stater);
//                btn_confirm_payment = (Button) dialogView.findViewById(R.id.btn_confirm_payment);
//                tv_cash_coupons_stater.setText("快速问诊劵 ￥25");
//                btn_confirm_payment.setText("确认支付 ￥0");
//            }
//
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}
