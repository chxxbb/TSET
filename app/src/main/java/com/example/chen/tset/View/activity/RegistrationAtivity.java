package com.example.chen.tset.View.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.IListener;
import com.example.chen.tset.Utils.ListenerManager;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.example.chen.tset.page.adapter.RegistrationageAdapter;
import com.example.chen.tset.page.adapter.RegistrationdivisionAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;

/**
 * 一键挂号
 */
public class RegistrationAtivity extends MyBaseActivity implements IListener {
    private RelativeLayout rl_city, rl_gender, rl_time, rl_age, rl_professionaltitle, rl_departments, rl_nonetwork, rl_loading, rl_use_cash_coupons;
    private LinearLayout ll_rutregistration, ll_cancel, ll_registr, ll_et_describe;
    private Dialog setHeadDialog;
    private View dialogView;
    private TextView tv_city, tv_gender, tv_time, tv_age, tv_professionaltitle, tv_departments, tv_cash_coupons_stater;
    private ProgressBar progressBar;
    private EditText et_phone, et_name, et_describe;
    private Button btn_pay;
    Calendar c;
    int myear, mmonth, mday;
    DatePickerDialog dialog;
    //年龄数据
    List<String> list;
    //职称
    List<String> data;
    //科室
    List<String> data1;
    RegistrationageAdapter adapter;
    RegistrationdivisionAdapter divisionAdapter;
    RegistrationdivisionAdapter divisionAdapter1;
    private RadioButton rb_zhifb, rb_wenx;
    private Button btn_confirm_payment;
    Gson gson;
    //订单号
    private String orderCode;

    SharedPsaveuser sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListenerManager.getInstance().registerListtener(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_registration_ativity);

        try {
            sp = new SharedPsaveuser(this);
            findView();


            divisioninit();


        } catch (Exception e) {
            e.printStackTrace();
        }
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
        rl_professionaltitle = (RelativeLayout) findViewById(R.id.rl_professionaltitle);
        tv_professionaltitle = (TextView) findViewById(R.id.tv_professionaltitle);
        rl_departments = (RelativeLayout) findViewById(R.id.rl_departments);
        tv_departments = (TextView) findViewById(R.id.tv_departments);
        btn_pay = (Button) findViewById(R.id.btn_pay);
        ll_rutregistration = (LinearLayout) findViewById(R.id.ll_rutregistration);
        rl_nonetwork = (RelativeLayout) findViewById(R.id.rl_nonetwork);
        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_describe = (EditText) findViewById(R.id.et_describe);
        ll_registr = (LinearLayout) findViewById(R.id.ll_registr);
        rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
        ll_et_describe = (LinearLayout) findViewById(R.id.ll_et_describe);
        rl_departments.setOnClickListener(listener);
        rl_city.setOnClickListener(listener);

        rl_gender.setOnClickListener(listener);
        rl_age.setOnClickListener(listener);
        rl_time.setOnClickListener(listener);
        rl_professionaltitle.setOnClickListener(listener);
        btn_pay.setOnClickListener(listener);
        ll_rutregistration.setOnClickListener(listener);
        ll_et_describe.setOnClickListener(listener);
        //获取当前时间
        c = Calendar.getInstance();
        myear = c.get(Calendar.YEAR);
        mmonth = c.get(Calendar.MONTH);
        mday = c.get(Calendar.DAY_OF_MONTH);
        data = new ArrayList<>();
        data1 = new ArrayList<>();
        gson = new

                Gson();
//        professionaltitleinit();

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //城市
                case R.id.rl_city:
                    cityshowDialog();
                    break;
                //性别
                case R.id.rl_gender:
                    gendershowDialog();
                    break;
                //时间
                case R.id.rl_time:
                    selectiontime();
                    break;
                //年龄
                case R.id.rl_age:
                    ageshowDialog();
                    break;
                //职称
                case R.id.rl_professionaltitle:
                    professionaltitleshowDialog();
                    break;
                //科室
                case R.id.rl_departments:
                    departmentsshowDialog();
                    break;
                //支付
                case R.id.btn_pay:
                    pay();
                    break;
                case R.id.ll_rutregistration:
                    finish();
                    break;
                case R.id.ll_et_describe:

                    et_describe.requestFocus();
                    //点击后弹出软键盘
                    InputMethodManager imm = (InputMethodManager) RegistrationAtivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    break;

            }


        }
    };

    private void pay() {

        //判断选项是否都已选择完
        if (tv_city.getText().toString().equals("请选择城市")) {
            Toast.makeText(RegistrationAtivity.this, "请选择城市", Toast.LENGTH_SHORT).show();
        } else if (tv_departments.getText().toString().equals("选择科室")) {
            Toast.makeText(RegistrationAtivity.this, "请选择科室", Toast.LENGTH_SHORT).show();
        } else if (tv_professionaltitle.getText().toString().equals("选择职称")) {
            Toast.makeText(RegistrationAtivity.this, "请选择职称", Toast.LENGTH_SHORT).show();
        } else if (tv_time.getText().toString().equals("选择预约时间")) {
            Toast.makeText(RegistrationAtivity.this, "请选择预约时间", Toast.LENGTH_SHORT).show();
        } else if (et_name.getText().toString().equals("") || et_name.getText().toString() == null) {
            Toast.makeText(RegistrationAtivity.this, "请输入姓名", Toast.LENGTH_SHORT).show();
        } else if (tv_gender.getText().toString().equals("患者性别")) {
            Toast.makeText(RegistrationAtivity.this, "选择预约性别", Toast.LENGTH_SHORT).show();
        } else if (tv_age.getText().toString().equals("患者年龄")) {
            Toast.makeText(RegistrationAtivity.this, "请选择年龄", Toast.LENGTH_SHORT).show();
        } else if (et_phone.getText().toString().equals("") || et_name.getText().toString() == null) {
            Toast.makeText(RegistrationAtivity.this, "请输入联系方式", Toast.LENGTH_SHORT).show();
        } else if (!isMobileNO(et_phone.getText().toString())) {
            Toast.makeText(RegistrationAtivity.this, "请填写正确的手机号", Toast.LENGTH_SHORT).show();
        } else {


            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            final String str = formatter.format(curDate);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpUtils
                            .post()
                            .url(Http_data.http_data + "/AddRegistrationOrder")
                            .addParams("city", tv_city.getText().toString())
                            .addParams("section", tv_departments.getText().toString())
                            .addParams("title", tv_professionaltitle.getText().toString())
                            .addParams("time", tv_time.getText().toString())
                            .addParams("gender", tv_gender.getText().toString())
                            .addParams("age", tv_age.getText().toString())
                            .addParams("name", et_name.getText().toString())
                            .addParams("phone", et_phone.getText().toString())
                            .addParams("content", et_describe.getText().toString())
                            .addParams("userId", sp.getTag().getId() + "")
                            .addParams("createTime", str)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Toast.makeText(RegistrationAtivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                }


                                @Override
                                public void onResponse(String response, int id) {


                                    if (response.equals("2")) {
                                        handler.sendEmptyMessage(0);
                                    } else if (response.equals("1")) {
                                        handler.sendEmptyMessage(1);
                                    } else {
                                        //挂号完成通知诊疗页面更新
                                        ListenerManager.getInstance().sendBroadCast("更新日历页面");
                                        orderCode = response;
                                        handler.sendEmptyMessage(2);
                                    }

                                }
                            });
                }
            }).start();


        }

    }

    public boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(RegistrationAtivity.this, "挂号失败,请稍后再试", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(RegistrationAtivity.this, "你这一天已经挂过号了", Toast.LENGTH_SHORT).show();
                    break;
                case 2:

                    setHeadDialog = new Dialog(RegistrationAtivity.this, R.style.CustomDialog);
                    setHeadDialog.show();
                    WindowManager windowManager = getWindowManager();
                    Display display = windowManager.getDefaultDisplay();
                    dialogView = View.inflate(getApplicationContext(), R.layout.payment_dialog, null);


                    rb_wenx = (RadioButton) dialogView.findViewById(R.id.rb_wenx);
                    rb_zhifb = (RadioButton) dialogView.findViewById(R.id.rb_zhifb);
                    ll_cancel = (LinearLayout) dialogView.findViewById(R.id.ll_cancel);

                    tv_cash_coupons_stater = (TextView) dialogView.findViewById(R.id.tv_cash_coupons_stater);

                    //使用现金卷
                    rl_use_cash_coupons = (RelativeLayout) dialogView.findViewById(R.id.rl_use_cash_coupons);


                    //确认支付
                    btn_confirm_payment = (Button) dialogView.findViewById(R.id.btn_confirm_payment);


                    rb_wenx.setChecked(true);
                    progressBar = (ProgressBar) dialogView.findViewById(R.id.progressBar);
                    setHeadDialog.getWindow().setContentView(dialogView);
                    WindowManager.LayoutParams lp = setHeadDialog.getWindow().getAttributes();
                    lp.width = display.getWidth();
                    setHeadDialog.getWindow().setAttributes(lp);
                    Thread thread = new Thread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    int progressBarMax = progressBar.getMax();
                                    try {
                                        //设置progressBar时间,3分钟内未支付则关闭此页面
                                        while (progressBarMax != progressBar.getProgress()) {
                                            int stepProgress = progressBarMax / 1000;
                                            int currentprogress = progressBar.getProgress();
                                            progressBar.setProgress(currentprogress + stepProgress);
                                            Thread.sleep(180);
                                        }
                                        finish();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();

                                    }
                                }
                            });
                    thread.start();
                    payonclick();
                    break;

            }
        }
    };


    private void payonclick() {
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
        //确认挂号
        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
                Intent intent = new Intent(RegistrationAtivity.this, ReservationActivity.class);
                startActivity(intent);
                Toast.makeText(RegistrationAtivity.this, "你可以在我的预约查看未完成的订单", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //使用现金卷
//        rl_use_cash_coupons.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(RegistrationAtivity.this, MyCashCouponsActivity.class);
//                intent.putExtra("type", "registration");
//                startActivity(intent);
//            }
//        });

        btn_confirm_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/ChangOrderStatusByOrderCode")
                        .addParams("orderCode", orderCode)
                        .addParams("status", "已预约")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Toast.makeText(RegistrationAtivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response, int id) {


                                if (response.equals("0")) {
                                    setHeadDialog.dismiss();
                                    Intent intent = new Intent(RegistrationAtivity.this, ReservationActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(RegistrationAtivity.this, "挂号成功,你可以在我的预约查看预约详情", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });
            }
        });
    }


    private void departmentsshowDialog() {

        setHeadDialog = new Dialog(this, R.style.CustomDialog);
        setHeadDialog.show();
        dialogView = View.inflate(getApplicationContext(), R.layout.registration_dialog, null);
        ListView lv_registration = (ListView) dialogView.findViewById(R.id.lv_registration);
        lv_registration.setVerticalScrollBarEnabled(false);
        lv_registration.setAdapter(divisionAdapter1);
        setHeadDialog.getWindow().setContentView(dialogView);
        WindowManager.LayoutParams lp = setHeadDialog.getWindow().getAttributes();
        setHeadDialog.getWindow().setAttributes(lp);
        departmentsdialogclick();
    }


    public void departmentsdialogclick() {
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        ListView lv_registration = (ListView) dialogView.findViewById(R.id.lv_registration);
        RelativeLayout rl_regisration = (RelativeLayout) dialogView.findViewById(R.id.rl_regisration);

        rl_regisration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
            }
        });


        lv_registration.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_departments.setText(data1.get(position));
                tv_departments.setTextColor(android.graphics.Color.parseColor("#323232"));
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


    private void professionaltitleshowDialog() {


        setHeadDialog = new Dialog(this, R.style.CustomDialog);
        setHeadDialog.show();

        dialogView = View.inflate(getApplicationContext(), R.layout.registration_dialog, null);
        ListView lv_registration = (ListView) dialogView.findViewById(R.id.lv_registration);

        lv_registration.setVerticalScrollBarEnabled(false);
        lv_registration.setAdapter(divisionAdapter);

        setHeadDialog.getWindow().setContentView(dialogView);

        WindowManager.LayoutParams lp = setHeadDialog.getWindow().getAttributes();
        setHeadDialog.getWindow().setAttributes(lp);
        professionaltitleclick();

    }


    private void professionaltitleclick() {
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        ListView lv_registration = (ListView) dialogView.findViewById(R.id.lv_registration);
        RelativeLayout rl_regisration = (RelativeLayout) dialogView.findViewById(R.id.rl_regisration);

        rl_regisration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
            }
        });
        lv_registration.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_professionaltitle.setText(data.get(position));
                tv_professionaltitle.setTextColor(android.graphics.Color.parseColor("#323232"));
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


    private void ageshowDialog() {
//        setHeadDialog = new Builder(this).create();
        setHeadDialog = new Dialog(this, R.style.CustomDialog);
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
        RelativeLayout rl_regisration = (RelativeLayout) dialogView.findViewById(R.id.rl_regisration);
        ListView lv_registration = (ListView) dialogView.findViewById(R.id.lv_registration);

        rl_regisration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
            }
        });
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
//        setHeadDialog = new Builder(this).create();
        setHeadDialog = new Dialog(this, R.style.CustomDialog);
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
        RelativeLayout rl_city = (RelativeLayout) dialogView.findViewById(R.id.rl_city);
        btn_chnegdu.setText("男");
        btn_shenzheng.setText("女");
        rl_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
            }
        });


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
//        setHeadDialog = new Builder(this).create();
        setHeadDialog = new Dialog(this, R.style.CustomDialog);
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
        RelativeLayout rl_city = (RelativeLayout) dialogView.findViewById(R.id.rl_city);
        rl_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
            }
        });


        btn_cancel.setOnClickListener(
                new View.OnClickListener() {
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


    private void divisioninit() {
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/FindSectionAndTitleList")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ll_registr.setVisibility(View.GONE);
                        rl_nonetwork.setVisibility(View.VISIBLE);
                        rl_loading.setVisibility(View.GONE);
                        Toast.makeText(RegistrationAtivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {


                        Type listtype = new TypeToken<List<List<String>>>() {
                        }.getType();
                        List<List<String>> mlist = gson.fromJson(response, listtype);

                        for (int j = 0; j < mlist.get(1).size(); j++) {
                            data.add(mlist.get(1).get(j));
                        }


                        for (int i = 0; i < mlist.get(0).size(); i++) {
                            data1.add(mlist.get(0).get(i));
                        }

                        //职称
                        divisionAdapter = new RegistrationdivisionAdapter(RegistrationAtivity.this, data);
                        divisionAdapter.notifyDataSetChanged();

                        //科室
                        divisionAdapter1 = new RegistrationdivisionAdapter(RegistrationAtivity.this, data1);
                        divisionAdapter1.notifyDataSetChanged();

                        rl_loading.setVisibility(View.GONE);

                    }

                });
    }

    @Override
    public void notifyAllActivity(String str) {
        if (str.equals("更新支付弹出框")) {
            tv_cash_coupons_stater.setText("快速挂号劵(￥25)");
            btn_confirm_payment.setText("确认支付0￥");
        }
    }
}
