package com.example.chen.tset.View;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CompileremindActivity extends AppCompatActivity {
    private CustomTimePicker timepacker;
    private RelativeLayout rl_starttime, rl_endtiem;
    private TextView tv_starttime, tv_endtiem;
    int myear, mmonth, mday;
    Calendar c;
    DatePickerDialog dialog;
    private String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compileremind);
        findView();

    }

    private void findView() {
        timepacker = (CustomTimePicker) findViewById(R.id.timepicker);
        rl_endtiem = (RelativeLayout) findViewById(R.id.rl_endtime);
        rl_starttime = (RelativeLayout) findViewById(R.id.rl_starttime);
        tv_endtiem = (TextView) findViewById(R.id.tv_endtime);
        tv_starttime = (TextView) findViewById(R.id.tv_starttime);
        //获取当前时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate = new Date(System.currentTimeMillis());
        tv_starttime.setText(formatter.format(curDate));
        tv_endtiem.setText(formatter.format(curDate));
        rl_starttime.setOnClickListener(listener);
        rl_endtiem.setOnClickListener(listener);
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
                    tv_starttime.setText(acquisitiontime());
                    break;
                case R.id.rl_endtime:
                    tv_endtiem.setText(acquisitiontime());
                    break;
            }
        }
    };

    private String acquisitiontime() {
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
                }

            }
        }, myear, mmonth, mday);
        dialog.show();
        return date;
    }

}
