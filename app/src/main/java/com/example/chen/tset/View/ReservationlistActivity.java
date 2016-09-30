package com.example.chen.tset.View;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.Reservation;
import com.example.chen.tset.Data.Reservationlist;
import com.example.chen.tset.Data.User;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
 * 预约订单详情
 */
public class ReservationlistActivity extends MyBaseActivity implements View.OnClickListener {
    private LinearLayout ll_myreservationg;
    private ScrollView scrollView;
    private TextView tv_content, tv_doctor_name, tv_title, tv_appointment_time, tv_valid_time, tv_address, tv_patient_name, tv_money, tv_patient_phone, tv_order_no, tv_hospital, tv_section, tv_doctor_section;
    private CircleImageView iv_icon;
    private RelativeLayout rl_nonetwork, rl_loading;

    private Button btn_cancel;

    Gson gson = new Gson();
    private String reservationid = null;

    Reservationlist reservationlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservationlist);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.setVerticalScrollBarEnabled(false);
        findView();
        httpinit();
    }


    private void findView() {
        reservationid = getIntent().getStringExtra("ReservationID");
        ll_myreservationg = (LinearLayout) findViewById(R.id.ll_myreservationg);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_doctor_name = (TextView) findViewById(R.id.tv_doctor_name);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_appointment_time = (TextView) findViewById(R.id.tv_appointment_time);
        tv_valid_time = (TextView) findViewById(R.id.tv_valid_time);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_patient_name = (TextView) findViewById(R.id.tv_patient_name);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_patient_phone = (TextView) findViewById(R.id.tv_patient_phone);
        tv_order_no = (TextView) findViewById(R.id.tv_order_no);
        tv_hospital = (TextView) findViewById(R.id.tv_hospital);
        tv_section = (TextView) findViewById(R.id.tv_section);
        tv_doctor_section = (TextView) findViewById(R.id.tv_doctor_section);
        iv_icon = (CircleImageView) findViewById(R.id.iv_icon);
        rl_nonetwork = (RelativeLayout) findViewById(R.id.rl_nonetwork);
        rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        ll_myreservationg.setOnClickListener(this);

    }


    private void httpinit() {
        Log.e("预约ID", reservationid);
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/FindRegistrationById")
                .addParams("id", reservationid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        handler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("预约详情返回", response);
                        reservationlist = gson.fromJson(response, Reservationlist.class);
                        handler.sendEmptyMessage(1);


                    }
                });
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(ReservationlistActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    rl_loading.setVisibility(View.GONE);
                    rl_nonetwork.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.GONE);
                    break;
                case 1:

                    tv_doctor_name.setText(reservationlist.getName());
                    tv_title.setText("职称：" + reservationlist.getTitle());
                    tv_appointment_time.setText("日期：" + reservationlist.getReservationDate());
                    tv_valid_time.setText("有效期：" + reservationlist.getReservationDate() );
                    if (reservationlist.getCity().equals("成都")) {
                        tv_address.setText("地址：" + "成都地址");
                    } else {
                        tv_address.setText("地址：" + "深圳地址");
                    }

                    tv_patient_name.setText("姓名：" + reservationlist.getName());
                    tv_money.setText("价格：￥" + reservationlist.getMoney());
                    tv_patient_phone.setText("电话号码：" + reservationlist.getPhone());
                    tv_order_no.setText("订单号：" + reservationlist.getOrderCode());

                    if (reservationlist.getCity().equals("成都")) {
                        tv_hospital.setText("成都天使儿童医院");
                    } else {
                        tv_hospital.setText("深证天使儿童医院");
                    }
                    tv_section.setText("科室：" + reservationlist.getSection());
                    tv_doctor_section.setText(reservationlist.getSection());

                    if (reservationlist.getOrderStatus().equals("待支付") || reservationlist.getOrderStatus().equals("已预约")) {
                        btn_cancel.setOnClickListener(listener);
                    } else if (reservationlist.getOrderStatus().equals("已取消")) {
                        btn_cancel.setBackgroundResource(R.drawable.reservationlist_btn_graycase);
                        btn_cancel.setText("已取消");
                        btn_cancel.setTextColor(android.graphics.Color.parseColor("#bbbbbb"));
                        btn_cancel.setOnClickListener(null);
                    } else if (reservationlist.getOrderStatus().equals("已完成")) {
                        btn_cancel.setBackgroundResource(R.drawable.reservationlist_btn_graycase);
                        btn_cancel.setText("已完成");
                        btn_cancel.setTextColor(android.graphics.Color.parseColor("#bbbbbb"));
                        btn_cancel.setOnClickListener(null);
                    } else {
                        btn_cancel.setBackgroundResource(R.drawable.reservationlist_btn_graycase);
                        btn_cancel.setText("已过期");
                        btn_cancel.setTextColor(android.graphics.Color.parseColor("#bbbbbb"));
                        btn_cancel.setOnClickListener(null);
                    }
                    rl_loading.setVisibility(View.GONE);
                    break;
                case 2:
                    Toast.makeText(ReservationlistActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    finish();
                    Toast.makeText(ReservationlistActivity.this, "取消成功", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                }
            }).start();
            OkHttpUtils
                    .post()
                    .url(Http_data.http_data + "/ChangOrderStatusByOrderCode")
                    .addParams("orderCode", reservationlist.getOrderCode())
                    .addParams("status", "已取消")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            handler.sendEmptyMessage(2);
                        }

                        @Override
                        public void onResponse(String response, int id) {

                            if (response.equals("0")) {
                                handler.sendEmptyMessage(3);
                            }
                        }
                    });
        }
    };

    @Override
    public void onClick(View v) {
        finish();
    }
}
