package com.example.chen.tset.View.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import java.util.Locale;

import okhttp3.Call;

/**
 * 添加提醒页面
 */
public class CompileremindActivity extends MyBaseActivity implements IListener {
    private RelativeLayout rl_starttime, rl_endtiem, rl_time_select1, rl_add_remind, rl_add_remind1, rl_time_select2, rl_time_select3;
    private TextView tv_starttime, tv_endtiem, tv_pas, tv_time1, tv_time2, tv_time3;
    private EditText et_compile_remind_content1, et_compile_remind_content2, et_compile_remind_content3;
    LinearLayout ll_rutname;
    int myear, mmonth, mday;
    DatePickerDialog dialog;
    Calendar c;
    private String date;
    private ScrollView scrollView;
    SharedPsaveuser sp;

    Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);

    SimpleDateFormat fmtTime = new SimpleDateFormat("HH:mm");

    LinearLayout ll_remind_case1, ll_remind_case2;

    String startDay;

    String endDay;

    String time1;

    String content1;

    String time2;

    String content2;

    String time3;

    String content3;


    int dataYears;

    int dataMonth;

    int dataDay;

    PharmacyDao db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compileremind);

        try {

            db = new PharmacyDao(this);
            findView();
            sp = new SharedPsaveuser(CompileremindActivity.this);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void findView() {

        //选择结束时间
        rl_endtiem = (RelativeLayout) findViewById(R.id.rl_endtime);
        //开始时间
        rl_starttime = (RelativeLayout) findViewById(R.id.rl_starttime);

        tv_endtiem = (TextView) findViewById(R.id.tv_endtime);

        tv_starttime = (TextView) findViewById(R.id.tv_starttime);

        tv_pas = (TextView) findViewById(R.id.tv_pas);

        scrollView = (ScrollView) findViewById(R.id.scrollView);

        scrollView.setVerticalScrollBarEnabled(false);

        rl_time_select1 = (RelativeLayout) findViewById(R.id.rl_time_select1);

        tv_time1 = (TextView) findViewById(R.id.tv_time1);

        et_compile_remind_content1 = (EditText) findViewById(R.id.et_compile_remind_content1);

        rl_add_remind = (RelativeLayout) findViewById(R.id.rl_add_remind);

        ll_remind_case1 = (LinearLayout) findViewById(R.id.ll_remind_case1);

        rl_add_remind1 = (RelativeLayout) findViewById(R.id.rl_add_remind1);

        rl_time_select2 = (RelativeLayout) findViewById(R.id.rl_time_select2);

        tv_time2 = (TextView) findViewById(R.id.tv_time2);

        et_compile_remind_content2 = (EditText) findViewById(R.id.et_compile_remind_content2);

        ll_remind_case2 = (LinearLayout) findViewById(R.id.ll_remind_case2);

        rl_time_select3 = (RelativeLayout) findViewById(R.id.rl_time_select3);

        tv_time3 = (TextView) findViewById(R.id.tv_time3);

        et_compile_remind_content3 = (EditText) findViewById(R.id.et_compile_remind_content3);

        ll_rutname = (LinearLayout) findViewById(R.id.ll_rutname);


        rl_starttime.setOnClickListener(listener);

        rl_endtiem.setOnClickListener(listener);

        tv_pas.setOnClickListener(listener);

        rl_time_select1.setOnClickListener(listener);

        rl_add_remind.setOnClickListener(listener);

        rl_time_select2.setOnClickListener(listener);

        rl_add_remind1.setOnClickListener(listener);

        rl_time_select3.setOnClickListener(listener);

        ll_rutname.setOnClickListener(listener);


        tv_time1.setText("8:00");

        tv_time2.setText("13:00");

        tv_time3.setText("18:00");


        //获取当前日期
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Date curDate = new Date(System.currentTimeMillis());

        tv_starttime.setText(formatter.format(curDate));

        tv_endtiem.setText(formatter.format(curDate));

        c = Calendar.getInstance();

        myear = c.get(Calendar.YEAR);

        mmonth = c.get(Calendar.MONTH);

        mday = c.get(Calendar.DAY_OF_MONTH);


        dataYears = c.get(Calendar.YEAR);

        dataMonth = c.get(Calendar.MONTH);

        dataDay = c.get(Calendar.DAY_OF_MONTH);


    }


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

                //选择第一个用药时间
                case R.id.rl_time_select1:
                    TimePickerDialog timeDlg = new TimePickerDialog(CompileremindActivity.this, t, dateAndTime.get(Calendar.HOUR_OF_DAY), dateAndTime.get(Calendar.MINUTE), true);
                    timeDlg.show();
                    break;

                case R.id.rl_add_remind:
                    if (et_compile_remind_content1.getText().toString() == null || et_compile_remind_content1.getText().toString().trim().equals("")) {
                        Toast.makeText(CompileremindActivity.this, "你的第一条用药提醒还未设置用药", Toast.LENGTH_SHORT).show();
                    } else {

                        rl_add_remind.setVisibility(View.GONE);
                        ll_remind_case1.setVisibility(View.VISIBLE);
                        rl_add_remind1.setVisibility(View.VISIBLE);
                    }
                    break;


                case R.id.rl_time_select2:
                    TimePickerDialog timeDlg1 = new TimePickerDialog(CompileremindActivity.this, t1, dateAndTime.get(Calendar.HOUR_OF_DAY), dateAndTime.get(Calendar.MINUTE), true);
                    timeDlg1.show();
                    break;


                case R.id.rl_add_remind1:

                    if (et_compile_remind_content2.getText().toString() == null || et_compile_remind_content2.getText().toString().trim().equals("")) {
                        Toast.makeText(CompileremindActivity.this, "你的第二条用药提醒还未设置用药", Toast.LENGTH_SHORT).show();
                    } else {
                        rl_add_remind1.setVisibility(View.GONE);
                        ll_remind_case2.setVisibility(View.VISIBLE);
                    }
                    break;

                case R.id.rl_time_select3:
                    TimePickerDialog timeDlg2 = new TimePickerDialog(CompileremindActivity.this, t2, dateAndTime.get(Calendar.HOUR_OF_DAY), dateAndTime.get(Calendar.MINUTE), true);
                    timeDlg2.show();
                    break;


                case R.id.tv_pas:
                    startDay = tv_starttime.getText().toString();
                    endDay = tv_endtiem.getText().toString();
                    time1 = tv_time1.getText().toString();
                    content1 = et_compile_remind_content1.getText().toString();
                    if (et_compile_remind_content2.getText().toString() == null || et_compile_remind_content2.getText().toString().trim().equals("")) {
                        time2 = "";
                        content2 = "";

                    } else {
                        time2 = tv_time2.getText().toString();
                        content2 = et_compile_remind_content2.getText().toString();
                    }

                    if (et_compile_remind_content3.getText().toString() == null || et_compile_remind_content3.getText().toString().trim().equals("")) {
                        time3 = "";
                        content3 = "";
                    } else {
                        time3 = tv_time3.getText().toString();
                        content3 = et_compile_remind_content3.getText().toString();
                    }


                    if (!tv_time1.getText().toString().equals("") && !et_compile_remind_content1.getText().toString().trim().equals("")) {

                        OkHttpUtils
                                .post()
                                .url(Http_data.http_data + "/AddRemind")
                                .addParams("userId", sp.getTag().getId() + "")
                                .addParams("startDay", startDay)
                                .addParams("endDay", endDay)
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

                                        if (response.equals("0")) {
                                            //发送广播通知诊疗页面更新
                                            ListenerManager.getInstance().sendBroadCast("更新日历页面");

                                            Pharmacyremind pharmacyremind=new Pharmacyremind(sp.getTag().getId()+"",startDay,endDay,time1,content1,time2,content2,time3,content3);
                                            db.addPharmacy(pharmacyremind);

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


    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {

        //同DatePickerDialog控件
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            tv_time1.setText(fmtTime.format(dateAndTime.getTime()));

        }
    };


    TimePickerDialog.OnTimeSetListener t1 = new TimePickerDialog.OnTimeSetListener() {

        //同DatePickerDialog控件
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            tv_time2.setText(fmtTime.format(dateAndTime.getTime()));

        }
    };

    TimePickerDialog.OnTimeSetListener t2 = new TimePickerDialog.OnTimeSetListener() {

        //同DatePickerDialog控件
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            tv_time3.setText(fmtTime.format(dateAndTime.getTime()));

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


                    dataYears = Integer.parseInt((String) DateFormat.format("yyy", c1));

                    dataMonth = Integer.parseInt((String) DateFormat.format("MM", c1));

                    dataDay = Integer.parseInt((String) DateFormat.format("dd", c1));

                    tv_starttime.setText(date);

                    tv_endtiem.setText(date);

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
//                c1.set(myear, mmonth, mday);

                c1.set(dataYears, dataMonth - 1, dataDay);

                dialog.getDatePicker().setMinDate(c1.getTimeInMillis());
                //判断设定的时间,不能为以前的时间


                if (year < dataYears || (year == dataYears && monthOfYear < dataMonth - 1) || (year == dataYears && monthOfYear == dataMonth - 1 && dayOfMonth < dataDay)) {

                    Toast.makeText(CompileremindActivity.this, "不能选择开始之前的时间", Toast.LENGTH_SHORT).show();

                } else {

                    c1.set(year, monthOfYear, dayOfMonth);

                    date = (String) DateFormat.format("yyy-MM-dd", c1);

                    tv_endtiem.setText(date);

                }

            }
        }, dataYears, dataMonth - 1, dataDay);
        dialog.show();
    }


    @Override
    public void notifyAllActivity(String str) {

    }


}


