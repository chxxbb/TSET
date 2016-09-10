package com.example.chen.tset.View;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class ReservationlistActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout ll_myreservationg;
    private ScrollView scrollView;
    private TextView tv_content, tv_doctor_name, tv_title, tv_appointment_time, tv_valid_time, tv_address, tv_patient_name, tv_money, tv_patient_phone, tv_order_no, tv_hospital, tv_section, tv_doctor_section;
    private CircleImageView iv_icon;
    private RelativeLayout rl_nonetwork, rl_loading;
    Gson gson = new Gson();
    private String reservationid=null;

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
        reservationid=getIntent().getStringExtra("ReservationID");
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
        ll_myreservationg.setOnClickListener(this);

    }


    private void httpinit() {
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/findBookingDetails")
                .addParams("id", reservationid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(ReservationlistActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        rl_loading.setVisibility(View.GONE);
                        rl_nonetwork.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("预约详情返回", response);
                        Type listtype = new TypeToken<LinkedList<Reservationlist>>() {
                        }.getType();
                        LinkedList<Reservationlist> leclist = gson.fromJson(response, listtype);
                        Iterator it = leclist.iterator();
                        Reservationlist reservationlist = (Reservationlist) it.next();
                        tv_content.setText(reservationlist.getContent());
                        tv_doctor_name.setText(reservationlist.getDoctor_name());
                        tv_title.setText("职称：" + reservationlist.getTitle());
                        tv_appointment_time.setText("日期：" + reservationlist.getAppointment_time());
                        tv_valid_time.setText("有效期：" + reservationlist.getValid_time());
                        tv_address.setText("地址：" + reservationlist.getAddress());
                        tv_patient_name.setText("姓名：" + reservationlist.getPatient_name());
                        tv_money.setText("价格：￥" + reservationlist.getMoney());
                        tv_patient_phone.setText("电话号码：" + reservationlist.getPatient_phone());
                        tv_order_no.setText("订单号：" + reservationlist.getOrder_no());
                        tv_hospital.setText(reservationlist.getHospital());
                        tv_section.setText("科室：" + reservationlist.getSection());
                        tv_doctor_section.setText(reservationlist.getSection());
                        rl_loading.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
