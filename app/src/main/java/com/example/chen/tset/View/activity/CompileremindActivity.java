package com.example.chen.tset.View.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.entity.Pharmacyremind;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.IListener;
import com.example.chen.tset.Utils.ListenerManager;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.example.chen.tset.Utils.db.PharmacyDao;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;

/**
 * 添加提醒页面
 */
public class CompileremindActivity extends MyBaseActivity implements IListener {
    private RelativeLayout rl_starttime, rl_endtiem;
    private TextView tv_starttime, tv_endtiem, tv_time_complete, tv_remind_set;
    int myear, mmonth, mday;
    Calendar c;
    DatePickerDialog dialog;
    private String date;
    private TimePicker timepicker, timepicker1, timepicker3;
    String aMPM = null;
    private LinearLayout ll_add_remind1, ll_rutname;
    private EditText et_pharmacy_compile, et_pharmacy_compile1, et_pharmacy_compile3;

    private TextView tv_time1, tv_content1, tv_time_complete1, tv_time2, tv_content2, tv_time3, tv_content3, tv_time_complete3, tv_pas;

    private LinearLayout ll_contnt1, ll_compile, ll_compile1, ll_contnt2, ll_contnt3, ll_compile3, ll_add_remind2;

    private ScrollView scrollView;

    PharmacyDao db;

    Pharmacyremind pharmacyrmind;

    String startdate;
    String overdate;
    String time1;
    String content1;
    String time2;
    String content2;
    String time3;
    String content3;
    SharedPsaveuser sp;

    private LinearLayout ll_cursor_blue_style, ll_pharmacy_compile, ll_cursor_blue_style2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compileremind);
        findView();
        sp = new SharedPsaveuser(CompileremindActivity.this);
        db = new PharmacyDao(this);

    }

    private void findView() {

        //选择结束时间
        rl_endtiem = (RelativeLayout) findViewById(R.id.rl_endtime);
        //开始时间
        rl_starttime = (RelativeLayout) findViewById(R.id.rl_starttime);
        tv_endtiem = (TextView) findViewById(R.id.tv_endtime);
        tv_starttime = (TextView) findViewById(R.id.tv_starttime);
        //时间选择
        timepicker = (TimePicker) findViewById(R.id.timepicker);
        //设置时间完成
        tv_time_complete = (TextView) findViewById(R.id.tv_time_complete);
        //内容设置
        tv_remind_set = (TextView) findViewById(R.id.tv_remind_set);
        ll_add_remind1 = (LinearLayout) findViewById(R.id.ll_add_remind1);

        scrollView = (ScrollView) findViewById(R.id.scrollView);

        scrollView.setVerticalScrollBarEnabled(false);

        tv_pas = (TextView) findViewById(R.id.tv_pas);


        ll_rutname = (LinearLayout) findViewById(R.id.ll_rutname);

        et_pharmacy_compile = (EditText) findViewById(R.id.et_pharmacy_compile);

        tv_content1 = (TextView) findViewById(R.id.tv_content1);

        tv_time1 = (TextView) findViewById(R.id.tv_time1);

        ll_contnt1 = (LinearLayout) findViewById(R.id.ll_contnt1);

        ll_compile = (LinearLayout) findViewById(R.id.ll_compile);

        ll_compile1 = (LinearLayout) findViewById(R.id.ll_compile1);

        tv_time_complete1 = (TextView) findViewById(R.id.tv_time_complete1);

        timepicker1 = (TimePicker) findViewById(R.id.timepicker1);

        et_pharmacy_compile1 = (EditText) findViewById(R.id.et_pharmacy_compile1);

        tv_time2 = (TextView) findViewById(R.id.tv_time2);

        tv_content2 = (TextView) findViewById(R.id.tv_content2);

        ll_contnt2 = (LinearLayout) findViewById(R.id.ll_contnt2);

        ll_contnt3 = (LinearLayout) findViewById(R.id.ll_contnt3);

        tv_time3 = (TextView) findViewById(R.id.tv_time3);

        tv_content3 = (TextView) findViewById(R.id.tv_content3);

        ll_compile3 = (LinearLayout) findViewById(R.id.ll_compile3);

        ll_add_remind2 = (LinearLayout) findViewById(R.id.ll_add_remind2);

        ll_compile3 = (LinearLayout) findViewById(R.id.ll_compile3);

        tv_time_complete3 = (TextView) findViewById(R.id.tv_time_complete3);

        timepicker3 = (TimePicker) findViewById(R.id.timepicker3);

        et_pharmacy_compile3 = (EditText) findViewById(R.id.et_pharmacy_compile3);


        ll_cursor_blue_style = (LinearLayout) findViewById(R.id.ll_cursor_blue_style);

        ll_pharmacy_compile = (LinearLayout) findViewById(R.id.ll_pharmacy_compile);

        ll_cursor_blue_style2 = (LinearLayout) findViewById(R.id.ll_cursor_blue_style2);

        ll_cursor_blue_style.setOnClickListener(lllistener);

        ll_pharmacy_compile.setOnClickListener(lllistener);

        ll_cursor_blue_style2.setOnClickListener(lllistener);

        tv_time_complete1.setOnClickListener(listener);

        ll_add_remind2.setOnClickListener(listener);

        tv_time_complete3.setOnClickListener(listener);

        //获取当前时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());
        tv_starttime.setText(formatter.format(curDate));
        tv_endtiem.setText(formatter.format(curDate));

        rl_starttime.setOnClickListener(listener);

        rl_endtiem.setOnClickListener(listener);

        tv_time_complete.setOnClickListener(listener);

        ll_add_remind1.setOnClickListener(listener);

        ll_rutname.setOnClickListener(listener);

        tv_pas.setOnClickListener(listener);

        c = Calendar.getInstance();

        myear = c.get(Calendar.YEAR);

        mmonth = c.get(Calendar.MONTH);

        mday = c.get(Calendar.DAY_OF_MONTH);


    }

    //点击输入区域edittexr获取焦点，弹出键盘
    private View.OnClickListener lllistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.ll_cursor_blue_style:
                    et_pharmacy_compile1.requestFocus();
                    InputMethodManager imm = (InputMethodManager) CompileremindActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    break;
                case R.id.ll_pharmacy_compile:
                    et_pharmacy_compile.requestFocus();
                    InputMethodManager imm1 = (InputMethodManager) CompileremindActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm1.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    break;
                case R.id.ll_cursor_blue_style2:
                    et_pharmacy_compile3.requestFocus();
                    InputMethodManager imm2 = (InputMethodManager) CompileremindActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm2.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    break;
            }

        }
    };


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_starttime:
                    //选择用药开始的日期
                    acquisitionstatrtime();
                    break;
                case R.id.rl_endtime:
                    //选择用药结束的日期
                    acquisitionendtime();
                    break;


                case R.id.tv_time_complete:
                    timepicker.setIs24HourView(true);
                    //判断所选时间为上午还是下午
                    if (timepicker.getCurrentHour() > 11 && timepicker.getCurrentHour() < 24) {
                        aMPM = "下午";
                    } else {
                        aMPM = "上午";
                    }
                    timepicker.setIs24HourView(false);
                    if (et_pharmacy_compile.getText().length() == 0) {
                        Toast.makeText(CompileremindActivity.this, "请输入用药", Toast.LENGTH_SHORT).show();
                    } else {
                        //将所选时间和所填用药记录显示
                        tv_time1.setText(timepicker.getCurrentHour() + "" + ":" + timepicker.getCurrentMinute() + "");
                        tv_content1.setText(et_pharmacy_compile.getText().toString());
                        //隐藏设置时间，输入用药
                        ll_contnt1.setVisibility(View.VISIBLE);
                        ll_compile.setVisibility(View.GONE);


                    }
                    break;


                case R.id.ll_add_remind1:
                    ll_add_remind1.setVisibility(View.GONE);
                    ll_compile1.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //使ScrollView一直显示到底部
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });

                    break;


                case R.id.tv_time_complete1:
                    timepicker1.setIs24HourView(true);
                    //判断所选时间为上午还是下午
                    if (timepicker1.getCurrentHour() > 11 && timepicker1.getCurrentHour() < 24) {
                        aMPM = "下午";
                    } else {
                        aMPM = "上午";
                    }
                    timepicker1.setIs24HourView(false);
                    if (et_pharmacy_compile1.getText().length() == 0) {
                        Toast.makeText(CompileremindActivity.this, "请输入用药", Toast.LENGTH_SHORT).show();
                    } else {
                        //将所选时间和所填用药记录显示
                        tv_time2.setText(timepicker1.getCurrentHour() + "" + ":" + timepicker1.getCurrentMinute() + "");
                        tv_content2.setText(et_pharmacy_compile1.getText().toString());
                        ll_compile1.setVisibility(View.GONE);
                        ll_contnt2.setVisibility(View.VISIBLE);

                    }

                    break;


                case R.id.ll_add_remind2:
                    ll_add_remind2.setVisibility(View.GONE);
                    ll_compile3.setVisibility(View.VISIBLE);
                    Handler handle1 = new Handler();
                    handle1.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });

                    break;


                case R.id.tv_time_complete3:
                    timepicker3.setIs24HourView(true);
                    //判断所选时间为上午还是下午
                    if (timepicker3.getCurrentHour() > 11 && timepicker3.getCurrentHour() < 24) {
                        aMPM = "下午";
                    } else {
                        aMPM = "上午";
                    }
                    timepicker3.setIs24HourView(false);
                    if (et_pharmacy_compile3.getText().length() == 0) {
                        Toast.makeText(CompileremindActivity.this, "请输入用药", Toast.LENGTH_SHORT).show();
                    } else {
                        //将所选时间和所填用药记录显示
                        tv_time3.setText(timepicker3.getCurrentHour() + "" + ":" + timepicker3.getCurrentMinute() + "");
                        tv_content3.setText(et_pharmacy_compile3.getText().toString());
                        ll_compile3.setVisibility(View.GONE);
                        ll_contnt3.setVisibility(View.VISIBLE);

                    }

                    break;


                case R.id.tv_pas:

                    startdate = tv_starttime.getText().toString();
                    overdate = tv_endtiem.getText().toString();


                    time1 = tv_time1.getText().toString();

                    Log.e("11", time1);
                    content1 = tv_content1.getText().toString();
                    time2 = tv_time2.getText().toString();
                    content2 = tv_content2.getText().toString();
                    time3 = tv_time3.getText().toString();
                    content3 = tv_content3.getText().toString();

                    if (!tv_time1.getText().toString().equals("") && !tv_content1.getText().toString().equals("")) {

                        OkHttpUtils
                                .post()
                                .url(Http_data.http_data + "/AddRemind")
                                .addParams("userId", sp.getTag().getId() + "")
                                .addParams("startDay", startdate)
                                .addParams("endDay", overdate)
                                .addParams("time1", time1)
                                .addParams("content1", content1)
                                .addParams("time2", time2)
                                .addParams("content2", content2)
                                .addParams("time3", time3)
                                .addParams("content3", content3)
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        Toast.makeText(CompileremindActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        Log.e("用药提醒返回", response);
                                        if (response.equals("0")) {
                                            //发送广播通知诊疗页面更新
                                            ListenerManager.getInstance().sendBroadCast("更新日历页面");
                                            Toast.makeText(CompileremindActivity.this, "添加成功，你可以在用药提醒页面查看", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(CompileremindActivity.this, "你在这段时间内已添加过用药了", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                    } else {

                        Toast.makeText(CompileremindActivity.this, "你还未添加至少一条用药提醒", Toast.LENGTH_SHORT).show();

                    }

                    break;


                case R.id.ll_rutname:
                    finish();
                    break;
            }
        }
    };

    private void acquisitionstatrtime() {
        final Calendar c1 = Calendar.getInstance();
        dialog = new DatePickerDialog(CompileremindActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //设置最小设置时间只能为当前时间
                c1.set(myear, mmonth, mday);
                dialog.getDatePicker().setMinDate(c1.getTimeInMillis());
                //判断设定的时间,不能为以前的时间
                if (year < myear || (year == myear && monthOfYear < mmonth) || (year == myear && monthOfYear == mmonth && dayOfMonth < mday)) {
                    Toast.makeText(CompileremindActivity.this, "不能选择以前的时间", Toast.LENGTH_SHORT).show();
                } else {
                    c1.set(year, monthOfYear, dayOfMonth);
                    date = (String) DateFormat.format("yyy-MM-dd", c1);
                    tv_starttime.setText(date);
                }

            }
        }, myear, mmonth, mday);
        dialog.show();
    }

    private void acquisitionendtime() {
        final Calendar c1 = Calendar.getInstance();
        dialog = new DatePickerDialog(CompileremindActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //设置最小设置时间只能为当前时间
                c1.set(myear, mmonth, mday);
                dialog.getDatePicker().setMinDate(c1.getTimeInMillis());
                //判断设定的时间,不能为以前的时间
                if (year < myear || (year == myear && monthOfYear < mmonth) || (year == myear && monthOfYear == mmonth && dayOfMonth < mday)) {
                    Toast.makeText(CompileremindActivity.this, "不能选择以前的时间", Toast.LENGTH_SHORT).show();
                } else {
                    c1.set(year, monthOfYear, dayOfMonth);
                    date = (String) DateFormat.format("yyy-MM-dd", c1);
                    tv_endtiem.setText(date);
                }

            }
        }, myear, mmonth, mday);
        dialog.show();
    }


    @Override
    public void notifyAllActivity(String str) {

    }

    ;

}


