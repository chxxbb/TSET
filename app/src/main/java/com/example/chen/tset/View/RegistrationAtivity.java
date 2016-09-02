package com.example.chen.tset.View;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.app.AlertDialog.Builder;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.R;
import com.example.chen.tset.page.RegistrationageAdapter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;

public class RegistrationAtivity extends AppCompatActivity {
    private RelativeLayout rl_city, rl_gender, rl_time, rl_age;
    private Dialog setHeadDialog;
    private View dialogView;
    private TextView tv_city, tv_gender, tv_time, tv_age;
    Calendar c;
    int myear, mmonth, mday;
    DatePickerDialog dialog;
    List<String> list;
    RegistrationageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_ativity);
        findView();
        init();
    }


    private void findView() {
        rl_city = (RelativeLayout) findViewById(R.id.rl_city);
        tv_city = (TextView) findViewById(R.id.tv_city);
        rl_gender = (RelativeLayout) findViewById(R.id.rl_gender);
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        rl_time = (RelativeLayout) findViewById(R.id.rl_time);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_age = (TextView) findViewById(R.id.tv_age);
        rl_age = (RelativeLayout) findViewById(R.id.rl_age);
        rl_city.setOnClickListener(listener);
        rl_gender.setOnClickListener(listener);
        rl_age.setOnClickListener(listener);
        rl_time.setOnClickListener(listener);
        //获取当前时间
        c = Calendar.getInstance();
        myear = c.get(Calendar.YEAR);
        mmonth = c.get(Calendar.MONTH);
        mday = c.get(Calendar.DAY_OF_MONTH);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_city:
                    cityshowDialog();
                    break;
                case R.id.rl_gender:
                    gendershowDialog();
                    break;
                case R.id.rl_time:
                    selectiontime();
                    break;
                case R.id.rl_age:
                    ageshowDialog();
                    break;
            }


        }
    };

    private void ageshowDialog() {
        setHeadDialog = new Builder(this).create();
        setHeadDialog.show();
        //设置弹出框视图
        dialogView = View.inflate(getApplicationContext(), R.layout.registration_dialog, null);
        ListView lv_registration = (ListView) dialogView.findViewById(R.id.lv_registration);
        lv_registration.setVerticalScrollBarEnabled(false);
        list = new ArrayList<>();
        for (int i = 1; i < 19; i++) {
            list.add(i + "岁");
        }
        adapter = new RegistrationageAdapter(this, list);
        lv_registration.setAdapter(adapter);
        adapter.notifyDataSetChanged();
//        lv_registration.setAdapter(new ArrayAdapter<String>(this, R.layout.registration_dialog_item, R.id.btn, data));
        setHeadDialog.getWindow().setContentView(dialogView);
        WindowManager.LayoutParams lp = setHeadDialog.getWindow().getAttributes();
        setHeadDialog.getWindow().setAttributes(lp);
        agedialogclick();
    }

    private void agedialogclick() {
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        ListView lv_registration = (ListView) dialogView.findViewById(R.id.lv_registration);
        lv_registration.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_age.setText(list.get(position));
                tv_age.setTextColor(android.graphics.Color.parseColor("#323232"));
                setHeadDialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
            }
        });

    }

    //选择预约时间
    public void selectiontime() {
        final Calendar c1 = Calendar.getInstance();
        dialog = new DatePickerDialog(RegistrationAtivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //设置最小设置时间只能为当前时间
                c1.set(myear, mmonth, mday);
                dialog.getDatePicker().setMinDate(c1.getTimeInMillis());
                //判断设定的时间,不能为以前的时间
                if (year < myear || (year == myear && monthOfYear < mmonth) || (year == myear && monthOfYear == mmonth && dayOfMonth < mday)) {
                    Toast.makeText(RegistrationAtivity.this, "不能选择以前的时间", Toast.LENGTH_SHORT).show();
                } else if (year == myear && monthOfYear - mmonth >= 2 || (year - myear >= 1 && mmonth != 12 && monthOfYear != 1) || (monthOfYear - mmonth == 1 && dayOfMonth > mday)) {
                    Toast.makeText(RegistrationAtivity.this, "请设定一个月内的时间", Toast.LENGTH_SHORT).show();
                } else {
                    c1.set(year, monthOfYear, dayOfMonth);
                    tv_time.setText(DateFormat.format("yyy-MM-dd", c1));
                    tv_time.setTextColor(android.graphics.Color.parseColor("#323232"));
                }

            }
        }, myear, mmonth, mday);
        dialog.show();
    }

    //选择性别
    private void gendershowDialog() {
        setHeadDialog = new Builder(this).create();
        setHeadDialog.show();
        //设置弹出框视图
        dialogView = View.inflate(getApplicationContext(), R.layout.registration_city_case, null);
        setHeadDialog.getWindow().setContentView(dialogView);
        WindowManager.LayoutParams lp = setHeadDialog.getWindow().getAttributes();
        setHeadDialog.getWindow().setAttributes(lp);
        genderdialogclick();
    }

    private void genderdialogclick() {
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        Button btn_chnegdu = (Button) dialogView.findViewById(R.id.btn_chengdu);
        Button btn_shenzheng = (Button) dialogView.findViewById(R.id.btn_shenzheng);
        btn_chnegdu.setText("男");
        btn_shenzheng.setText("女");
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
            }
        });
        btn_chnegdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
                tv_gender.setText("男");
                tv_gender.setTextColor(android.graphics.Color.parseColor("#323232"));

            }
        });
        btn_shenzheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
                tv_gender.setText("女");
                tv_gender.setTextColor(android.graphics.Color.parseColor("#323232"));
            }
        });
    }

    //选择城市
    public void cityshowDialog() {
        setHeadDialog = new Builder(this).create();
        setHeadDialog.show();
        dialogView = View.inflate(getApplicationContext(), R.layout.registration_city_case, null);
        setHeadDialog.getWindow().setContentView(dialogView);
        WindowManager.LayoutParams lp = setHeadDialog.getWindow()
                .getAttributes();
        setHeadDialog.getWindow().setAttributes(lp);
        citydialogclick();
    }

    private void citydialogclick() {
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        Button btn_chnegdu = (Button) dialogView.findViewById(R.id.btn_chengdu);
        Button btn_shenzheng = (Button) dialogView.findViewById(R.id.btn_shenzheng);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
            }
        });
        btn_chnegdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
                tv_city.setText("成都");
                tv_city.setTextColor(android.graphics.Color.parseColor("#323232"));

            }
        });
        btn_shenzheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
                tv_city.setText("深圳");
                tv_city.setTextColor(android.graphics.Color.parseColor("#323232"));
            }
        });
    }

    private void init() {
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/resulter")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(RegistrationAtivity.this, "失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("一键挂号返回", response);
                    }
                });
    }
}
