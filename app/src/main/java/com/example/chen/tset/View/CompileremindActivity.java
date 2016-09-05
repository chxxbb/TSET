package com.example.chen.tset.View;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.chen.tset.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CompileremindActivity extends AppCompatActivity {
    private CustomTimePicker timepacker;
    private RelativeLayout rl_starttime, rl_endtiem;
    private TextView tv_starttime, tv_endtiem, tv_time_complete, tv_remind_set, tv_remind_content, tv_pharmacy_compile;
    int myear, mmonth, mday;
    Calendar c;
    DatePickerDialog dialog;
    private String date;
    private TimePicker timepicker;
    String aMPM = null;
    private FrameLayout frameLayout;
    private LinearLayout ll_pharmacy_compile, ll_add_remind, ll_remind_content, ll_et;
    private View view, v1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compileremind);
        findView();

    }

    private void findView() {
        //日期选择
        timepacker = (CustomTimePicker) findViewById(R.id.timepicker);
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
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        //提醒内容
        tv_remind_content = (TextView) findViewById(R.id.tv_remind_content);
        //用药提醒
        tv_pharmacy_compile = (TextView) findViewById(R.id.tv_pharmacy_compile);
        ll_pharmacy_compile = (LinearLayout) findViewById(R.id.ll_pharmacy_compile);
        ll_add_remind = (LinearLayout) findViewById(R.id.ll_add_remind);
        ll_remind_content = (LinearLayout) findViewById(R.id.ll_remind_content);
        ll_et = (LinearLayout) findViewById(R.id.ll_et);
        view = findViewById(R.id.view);
        v1 = findViewById(R.id.v1);

        //获取当前时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate = new Date(System.currentTimeMillis());
        tv_starttime.setText(formatter.format(curDate));
        tv_endtiem.setText(formatter.format(curDate));
        rl_starttime.setOnClickListener(listener);
        rl_endtiem.setOnClickListener(listener);
        tv_time_complete.setOnClickListener(listener);
        ll_add_remind.setOnClickListener(listener);
        c = Calendar.getInstance();
        myear = c.get(Calendar.YEAR);
        mmonth = c.get(Calendar.MONTH);
        mday = c.get(Calendar.DAY_OF_MONTH);


    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_starttime:
                    acquisitionstatrtime();
                    break;
                case R.id.rl_endtime:
                    acquisitionendtime();
                    break;
                case R.id.tv_time_complete:
                    timepicker.setIs24HourView(true);
                    if (timepicker.getCurrentHour() > 11 && timepicker.getCurrentHour() < 24) {
                        aMPM = "下午";
                    } else {
                        aMPM = "上午";
                    }
                    timepicker.setIs24HourView(false);
                    frameLayout.setVisibility(View.GONE);
                    tv_time_complete.setVisibility(View.GONE);
                    ll_pharmacy_compile.setVisibility(View.GONE);
                    ll_remind_content.setVisibility(View.GONE);
                    ll_add_remind.setVisibility(View.VISIBLE);
                    ll_et.setVisibility(View.VISIBLE);
                    tv_remind_set.setText(aMPM + timepicker.getCurrentHour() + "" + ":" + timepicker.getCurrentMinute() + "");
                    break;
                case R.id.ll_add_remind:
                    rl_endtiem.setVisibility(View.GONE);
                    rl_starttime.setVisibility(View.GONE);
                    view.setVisibility(View.GONE);
                    v1.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    tv_time_complete.setVisibility(View.VISIBLE);
                    ll_pharmacy_compile.setVisibility(View.VISIBLE);
                    ll_remind_content.setVisibility(View.VISIBLE);
                    ll_add_remind.setVisibility(View.GONE);
                    ll_et.setVisibility(View.GONE);
                    tv_remind_set.setText("添加提醒");
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
                    date = (String) DateFormat.format("yyy年MM月dd日", c1);
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
                    date = (String) DateFormat.format("yyy年MM月dd日", c1);
                    tv_endtiem.setText(date);
                }

            }
        }, myear, mmonth, mday);
        dialog.show();
    }

}
