package com.example.chen.tset.View.activity;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
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

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.entity.Reservationlist;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.IListener;
import com.example.chen.tset.Utils.ListenerManager;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * 预约订单详情
 */
public class ReservationlistActivity extends MyBaseActivity implements View.OnClickListener, IListener {
    private LinearLayout ll_myreservationg, ll_order_remind;
    private ScrollView scrollView;
    private TextView tv_content, tv_title, tv_appointment_time, tv_valid_time, tv_address, tv_patient_name, tv_money, tv_patient_phone, tv_order_no, tv_hospital, tv_section;
    private CircleImageView iv_icon;
    private RelativeLayout rl_nonetwork, rl_loading;

    private Button btn_cancel;

    Gson gson = new Gson();
    private String reservationid = null;

    Reservationlist reservationlist;

    private Dialog setHeadDialog;
    private View dialogView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservationlist);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        ListenerManager.getInstance().registerListtener(this);
        scrollView.setVerticalScrollBarEnabled(false);
        findView();
        httpinit();
    }


    private void findView() {
        reservationid = getIntent().getStringExtra("ReservationID");
        ll_myreservationg = (LinearLayout) findViewById(R.id.ll_myreservationg);
        tv_content = (TextView) findViewById(R.id.tv_content);

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

        iv_icon = (CircleImageView) findViewById(R.id.iv_icon);
        rl_nonetwork = (RelativeLayout) findViewById(R.id.rl_nonetwork);
        rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);

        ll_order_remind = (LinearLayout) findViewById(R.id.ll_order_remind);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        ll_myreservationg.setOnClickListener(this);

    }


    private void httpinit() {

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


                    tv_title.setText("职称：" + reservationlist.getTitle());
                    tv_appointment_time.setText("日期：" + reservationlist.getReservationDate());
                    tv_valid_time.setText("有效期：" + reservationlist.getReservationDate());
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

                    /**
                     * 判断是否支付的状态，并使按钮显示不同的背景，文字，颜色
                     */
                    if (reservationlist.getOrderStatus().equals("已预约")) {
                        ll_order_remind.setVisibility(View.VISIBLE);
                        btn_cancel.setOnClickListener(listener);

                    } else if (reservationlist.getOrderStatus().equals("待支付")) {
                        btn_cancel.setText("去支付");
                        btn_cancel.setOnClickListener(paylistener);

                    } else if (reservationlist.getOrderStatus().equals("已取消")) {
                        btn_cancel.setBackgroundResource(R.drawable.reservationlist_btn_graycase);
                        btn_cancel.setText("已取消");
                        btn_cancel.setTextColor(android.graphics.Color.parseColor("#bbbbbb"));
//                        btn_cancel.setOnClickListener(deletelistener);
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
                    ListenerManager.getInstance().sendBroadCast("更新我的预约");
                    ListenerManager.getInstance().sendBroadCast("更新日历页面");
                    Toast.makeText(ReservationlistActivity.this, "取消成功", Toast.LENGTH_SHORT).show();
                    btn_cancel.setBackgroundResource(R.drawable.reservationlist_btn_graycase);
                    btn_cancel.setText("已取消");
                    btn_cancel.setTextColor(android.graphics.Color.parseColor("#bbbbbb"));
                    btn_cancel.setOnClickListener(null);
                    break;

            }
        }
    };


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancelindent();

        }
    };

    //取消订单
    public void cancelindent() {
        setHeadDialog = new Dialog(this, R.style.CustomDialog);
        setHeadDialog.show();
        dialogView = View.inflate(this, R.layout.inquiry_chat_dialog, null);
        setHeadDialog.getWindow().setContentView(dialogView);
        WindowManager.LayoutParams lp = setHeadDialog.getWindow()
                .getAttributes();
        setHeadDialog.getWindow().setAttributes(lp);

        RelativeLayout rl_confirm = (RelativeLayout) dialogView.findViewById(R.id.rl_confirm);
        RelativeLayout lr_cancel = (RelativeLayout) dialogView.findViewById(R.id.lr_cancel);
        TextView tv_whether = (TextView) dialogView.findViewById(R.id.tv_whether);
        tv_whether.setText("是否确认取消订单");


        lr_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
            }
        });


        rl_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setHeadDialog.dismiss();

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
        });
    }

    //删除订单
    private View.OnClickListener deletelistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            OkHttpUtils
                    .post()
                    .url(Http_data.http_data + "/DeleteRegistrationById")
                    .addParams("orderCode", reservationlist.getOrderCode())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Toast.makeText(ReservationlistActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            ListenerManager.getInstance().sendBroadCast("更新我的预约");
                            Toast.makeText(ReservationlistActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            finish();


                        }
                    });
        }
    };

    private View.OnClickListener paylistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(ReservationlistActivity.this, "支付", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void notifyAllActivity(String str) {

    }
}
