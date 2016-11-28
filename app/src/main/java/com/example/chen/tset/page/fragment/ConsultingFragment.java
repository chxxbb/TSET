package com.example.chen.tset.page.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AnimRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import com.example.chen.tset.Data.entity.Calendarinit;
import com.example.chen.tset.Data.entity.ConsultingRemindState;
import com.example.chen.tset.Data.entity.PharmacyState;
import com.example.chen.tset.Data.entity.Calendarform;
import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.R;

import com.example.chen.tset.Utils.CalendarUtil;
import com.example.chen.tset.Utils.Lauar;
import com.example.chen.tset.page.view.CalendarGridView;
import com.example.chen.tset.Utils.IListener;
import com.example.chen.tset.Utils.ListenerManager;

import com.example.chen.tset.page.view.MyScrollview;
import com.example.chen.tset.Utils.db.PharmacyDao;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.example.chen.tset.View.activity.CompilePharmacyRemindActivity;
import com.example.chen.tset.View.activity.CompileremindActivity;
import com.example.chen.tset.View.activity.HealthconditionActivity;
import com.example.chen.tset.View.activity.HealthparticularsActivity;
import com.example.chen.tset.View.activity.PharmacyremindActivity;
import com.example.chen.tset.View.activity.ReservationlistActivity;
import com.example.chen.tset.page.adapter.CalendarAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.Iterator;

import java.util.LinkedList;
import java.util.List;


import okhttp3.Call;


/**
 * Created by Administrator on 2016/9/21 0021.
 * 诊疗页面，还包括健康日历的左右滑动
 */
public class ConsultingFragment extends Fragment implements View.OnClickListener, IListener {
    View view;
    private RelativeLayout rl_loading;
    private MyScrollview scrollview;
    private LinearLayout ll_right, ll_left;
    private GestureDetector gestureDetector = null;
    private CalendarAdapter calV = null;
    private ViewFlipper flipper = null;
    private CalendarGridView gridView = null;
    private static int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
    private static int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private String currentDate = "";

    boolean onclickstater = true;


    //用户点击的时间
    String date;
    /**
     * 每次添加gridview到viewflipper中时给的标记
     */
    private int gvFlag = 0;
    /**
     * 当前的年月，现在日历顶端
     */
    private TextView currentMonth;
    /**
     * 上个月
     */
    private ImageView prevMonth;
    /**
     * 下个月
     */
    private ImageView nextMonth;

    List<PharmacyState> list;


    private LinearLayout ll_consulting_popup_case;


    private LinearLayout ll_registration_info, ll_consulting_phramacy, ll_consulting_health;

//    private ToggleButton tb_registration, tb_health, tb_pharmacy;
//    ll_health,ll_pharmacy,ll_registration,

    private TextView tv_registration_info, tv_consulting_pharmacy, tv_consulting_health;

    //挂号提醒选择状态
    int registrationSelect = 1;

    //健康提醒选择状态
    int healthSelect = 1;


    //用药提醒选择状态
    int pharmacySelect = 1;

    String registrationId;

    Gson gson = new Gson();

    List<Calendarform> clist;

    PharmacyDao db;
    SharedPsaveuser sp;

    List<ConsultingRemindState> data;

    Calendarinit calendarinit;

    String pharmacydate;

    //添加用药提醒
    LinearLayout ll_add_medicine_remind;

    //添加健康状况
    LinearLayout ll_add_health_condition;


    TextView tv_lunar_calendar, tv_solar_terms, tv_chinese_era;

    Lauar lauar = new Lauar();

    private ToggleButton cal_toggleBtn, cal_toggleBtn1, cal_toggleBtn2;

    String str;

    String today;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_consulting, null);


        sp = new SharedPsaveuser(getContext());

        clist = new ArrayList<>();
        //设置预加载页面
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1);
                    handler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        str = formatter.format(curDate);
        pharmacydate = sdf.format(curDate);
        today = sdf.format(curDate);
        findAllByDate(str);
        //注册监听器
        ListenerManager.getInstance().registerListtener(this);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


    }


    //更新诊疗页面
    @Override
    public void notifyAllActivity(String str) {
        if (str.equals("更新日历页面")) {


            handler.sendEmptyMessage(0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String str1 = formatter.format(curDate);
            pharmacydate = sdf.format(curDate);
            findAllByDate(str1);
            //注册监听器
            ListenerManager.getInstance().registerListtener(this);

        }


    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    findView();
                    init();
                    rl_loading.setVisibility(View.GONE);
                    break;
                case 1:
                    calV.notifyDataSetChanged();
                    break;
                case 2:
//                    Toast.makeText(getContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    ll_registration_info.setVisibility(View.GONE);
                    ll_consulting_phramacy.setVisibility(View.GONE);
                    ll_consulting_health.setVisibility(View.GONE);


                    tv_registration_info = (TextView) view.findViewById(R.id.tv_registration_info);

                    ll_consulting_health = (LinearLayout) view.findViewById(R.id.ll_consulting_health);

                    tv_consulting_health = (TextView) view.findViewById(R.id.tv_consulting_health);

                    tv_consulting_pharmacy = (TextView) view.findViewById(R.id.tv_consulting_pharmacy);


                    break;
                case 4:
                    /**
                     * 判断当天或点击的日期中是否有挂号，健康状况，用药
                     * 如果有则显示条目，并显示详细信息
                     * 如果没有则隐藏条目
                     */

                    //挂号
                    if (calendarinit.getRegistrationId() == 0) {
                        ll_registration_info.setVisibility(View.GONE);
                    } else {
                        ll_registration_info.setVisibility(View.VISIBLE);
                        tv_registration_info.setText("有挂号" + ",请于" + pharmacydate + "准时就诊");

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");


                        ll_registration_info.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), ReservationlistActivity.class);
                                intent.putExtra("ReservationID", calendarinit.getRegistrationId() + "");
                                startActivity(intent);
                            }
                        });
                    }


                    //健康状况
                    if (calendarinit.getTag() == null && calendarinit.getContent() == null) {
                        ll_consulting_health.setVisibility(View.GONE);
                        if (date == null || date.equals(today)) {
                            ll_add_health_condition.setVisibility(View.VISIBLE);
                        } else {
                            ll_add_health_condition.setVisibility(View.GONE);
                        }


                    } else {
                        ll_add_health_condition.setVisibility(View.GONE);
                        ll_consulting_health.setVisibility(View.VISIBLE);
                        tv_consulting_health.setText(calendarinit.getTag() + "  " + calendarinit.getContent());
                        ll_consulting_health.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), HealthparticularsActivity.class);
                                intent.putExtra("time", pharmacydate);
                                intent.putExtra("tag", calendarinit.getTag());
                                intent.putExtra("content", calendarinit.getContent());
                                intent.putExtra("healthId", calendarinit.getHealth_id());
                                startActivity(intent);
                            }
                        });
                    }

                    //用药
                    if (calendarinit.getTime1() == null && calendarinit.getTime2() == null && calendarinit.getTime3() == null) {
                        ll_consulting_phramacy.setVisibility(View.GONE);
                    } else {
                        ll_consulting_phramacy.setVisibility(View.VISIBLE);
                        tv_consulting_pharmacy.setText("用药时间：  " + calendarinit.getTime1() + "   " + calendarinit.getTime2() + "   " + calendarinit.getTime3());
                        ll_consulting_phramacy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), CompilePharmacyRemindActivity.class);
                                intent.putExtra("remindId", calendarinit.getRemindId() + "");
                                startActivity(intent);
                            }
                        });
                    }
                    break;

            }
        }
    };


    private void findView() {
        rl_loading = (RelativeLayout) view.findViewById(R.id.rl_loading);
        scrollview = (MyScrollview) view.findViewById(R.id.scrollview);
        scrollview.setVerticalScrollBarEnabled(false);

        currentMonth = (TextView) view.findViewById(R.id.currentMonth);
        prevMonth = (ImageView) view.findViewById(R.id.prevMonth);
        nextMonth = (ImageView) view.findViewById(R.id.nextMonth);

        ll_add_health_condition = (LinearLayout) view.findViewById(R.id.ll_add_health_condition);

        ll_add_medicine_remind = (LinearLayout) view.findViewById(R.id.ll_add_medicine_remind);

        ll_right = (LinearLayout) view.findViewById(R.id.ll_right);
        ll_left = (LinearLayout) view.findViewById(R.id.ll_left);


        cal_toggleBtn = (ToggleButton) view.findViewById(R.id.cal_toggleBtn);
        cal_toggleBtn1 = (ToggleButton) view.findViewById(R.id.cal_toggleBtn1);
        cal_toggleBtn2 = (ToggleButton) view.findViewById(R.id.cal_toggleBtn2);

        tv_solar_terms = (TextView) view.findViewById(R.id.tv_solar_terms);

        tv_lunar_calendar = (TextView) view.findViewById(R.id.tv_lunar_calendar);


        ll_consulting_phramacy = (LinearLayout) view.findViewById(R.id.ll_consulting_phramacy);
        ll_consulting_phramacy.setOnClickListener(remindlistener);

        ll_consulting_popup_case = (LinearLayout) view.findViewById(R.id.ll_consulting_popup_case);

        ll_registration_info = (LinearLayout) view.findViewById(R.id.ll_registration_info);
        tv_registration_info = (TextView) view.findViewById(R.id.tv_registration_info);

        ll_consulting_health = (LinearLayout) view.findViewById(R.id.ll_consulting_health);

        tv_consulting_health = (TextView) view.findViewById(R.id.tv_consulting_health);

        tv_consulting_pharmacy = (TextView) view.findViewById(R.id.tv_consulting_pharmacy);

        tv_chinese_era = (TextView) view.findViewById(R.id.tv_chinese_era);

        ll_add_medicine_remind.setOnClickListener(addlistener);
        ll_add_health_condition.setOnClickListener(addlistener);

        ll_left.setOnClickListener(this);
        ll_right.setOnClickListener(this);


        SimpleDateFormat s1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat s2 = new SimpleDateFormat("MM");
        SimpleDateFormat s3 = new SimpleDateFormat("dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str1 = s1.format(curDate);
        String str2 = s2.format(curDate);
        String str3 = s3.format(curDate);

        CalendarUtil c = new CalendarUtil();

        int y = Integer.parseInt(str1);
        int m = Integer.parseInt(str2);
        int d = Integer.parseInt(str3);

        String festival;

        String lunarCalendar = c.getChineseMonth(y, m, d) + c.getChineseDay(y, m, d);


        c.setGregorian(y, m, d);
        c.computeChineseFields();
        c.computeSolarTerms();


        String lauar1 = lauar.getLunar(str1, str2, str3);

        if (c.judgefestival(m, d, lunarCalendar).equals("")) {
            tv_solar_terms.setText(c.getDateString().trim());
        } else {
            tv_solar_terms.setText(c.judgefestival(m, d, lunarCalendar).trim());
        }


        if (tv_solar_terms.getText().toString().equals("")) {
            tv_solar_terms.setVisibility(View.GONE);
        } else {
            tv_solar_terms.setVisibility(View.VISIBLE);
        }


        tv_chinese_era.setText(lauar1);
        tv_lunar_calendar.setText("农历" + lunarCalendar);


    }


    private void init() {
        data = new ArrayList<>();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        currentDate = sdf.format(date); // 当期日期
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);


        gestureDetector = new GestureDetector(getContext(), new MyGestureListener());
        flipper = (ViewFlipper) view.findViewById(R.id.flipper);
        flipper.removeAllViews();


        list = new ArrayList<>();


        calV = new CalendarAdapter(getContext(), getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, data);
        addGridView();
        gridView.setAdapter(calV);
        flipper.addView(gridView, 0);
        addTextToTopTextView(currentMonth);


        //默认提醒设置为圈选中
        calV.pharmacyremind(1);
        calV.registrationremind(1);
        calV.healthremind(1);


        //判断提醒设置是否被选中，选中则改变健康日历上面的标记
        cal_toggleBtn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    calV.pharmacyremind(2);
                } else {
                    calV.pharmacyremind(1);
                }
            }
        });


        cal_toggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    calV.registrationremind(2);
                } else {
                    calV.registrationremind(1);
                }
            }
        });


        cal_toggleBtn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    calV.healthremind(2);
                } else {
                    calV.healthremind(1);
                }
            }
        });


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式

        findCalendar(df.format(new Date()));


    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int gvFlag = 0; // 每次添加gridview到viewflipper中时给的标记
            if (e1.getX() - e2.getX() > 60) {


                // 像左滑动
                enterNextMonth(gvFlag);


                return true;
            } else if (e1.getX() - e2.getX() < -60) {


                // 向右滑动
                enterPrevMonth(gvFlag);


                return true;
            }


            return false;
        }
    }


    //移动到下一个月
    private void enterNextMonth(int gvFlag) {

        addGridView(); // 添加一个gridView
        jumpMonth++; // 下一个月

        calV = new CalendarAdapter(getContext(), this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, data);
        gridView.setAdapter(calV);
        addTextToTopTextView(currentMonth); // 移动到下一月后，将当月显示在头标题中


        //加载滑动到的页面日期数据
        findAllByDate(calV.getShowYear() + "-" + calV.getShowMonth());


        gvFlag++;
        flipper.addView(gridView, gvFlag);


        //设置滑动动画
        flipper.setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_left_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_left_out));
        flipper.showNext();
        flipper.removeViewAt(0);

        //滑动页面后提醒设置选中变为默认全被选中
        calV.pharmacyremind(1);
        calV.registrationremind(1);
        calV.healthremind(1);

        cal_toggleBtn.setChecked(false);
        cal_toggleBtn1.setChecked(false);
        cal_toggleBtn2.setChecked(false);


    }

    //移动到上一个月
    private void enterPrevMonth(int gvFlag) {


        addGridView(); // 添加一个gridView
        jumpMonth--; // 上一个月

        calV = new CalendarAdapter(getContext(), this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, data);
        gridView.setAdapter(calV);
        gvFlag++;
        addTextToTopTextView(currentMonth); // 移动到上一月后，将当月显示在头标题中
        String str = null;
        //调整日期格式，如果月份为各位数则在其他前面+1
        if (calV.getShowMonth().equals("1") || calV.getShowMonth().equals("2") || calV.getShowMonth().equals("3") || calV.getShowMonth().equals("4") || calV.getShowMonth().equals("5") || calV.getShowMonth().equals("6") || calV.getShowMonth().equals("7") || calV.getShowMonth().equals("8") || calV.getShowMonth().equals("9")) {
            str = calV.getShowYear() + "-0" + calV.getShowMonth();
        } else {
            str = calV.getShowYear() + "-" + calV.getShowMonth();

        }

        //加载滑动到的页面日期数据
        findAllByDate(str);

        flipper.addView(gridView, gvFlag);

        flipper.setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_right_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_right_out));
        flipper.showPrevious();
        flipper.removeViewAt(0);

        calV.pharmacyremind(1);
        calV.registrationremind(1);
        calV.healthremind(1);

        cal_toggleBtn.setChecked(false);
        cal_toggleBtn1.setChecked(false);
        cal_toggleBtn2.setChecked(false);


    }


    /**
     * 添加头部的年份 闰哪月等信息
     *
     * @param view
     */
    public void addTextToTopTextView(TextView view) {
        StringBuffer textDate = new StringBuffer();
        textDate.append(calV.getShowYear()).append("年").append(calV.getShowMonth()).append("月");
        view.setText(textDate);
    }


    private void addGridView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        // 取得屏幕的宽度和高度

        WindowManager windowManager = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);


        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth();
        int Height = display.getHeight();

        gridView = new CalendarGridView(getContext());
        gridView.setNumColumns(7);
        gridView.setColumnWidth(40);
        // gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        if (Width == 720 && Height == 1280) {
            gridView.setColumnWidth(40);
        }
        gridView.setGravity(Gravity.CENTER_VERTICAL);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // 去除gridView边框
        gridView.setVerticalSpacing(1);
        gridView.setHorizontalSpacing(1);


        gridView.setOnTouchListener(new View.OnTouchListener() {
            // 将gridview中的触摸事件回传给gestureDetector

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return gestureDetector.onTouchEvent(event);

            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // TODO Auto-generated method stub


                calV.changeSelected(position);

                int startPosition = calV.getStartPositon();
                int endPosition = calV.getEndPosition();
                if (startPosition <= position + 7 && position <= endPosition - 7) {
                    String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0]; // 这一天的阳历
                    String scheduleYear = calV.getShowYear();
                    String scheduleMonth = calV.getShowMonth();

                    String month = null;
                    String day = null;

                    //调整日期格式，如果月份为各位数则在其他前面+1
                    if (scheduleMonth.equals("1") || scheduleMonth.equals("2") || scheduleMonth.equals("3") || scheduleMonth.equals("4") || scheduleMonth.equals("5") || scheduleMonth.equals("6") || scheduleMonth.equals("7") || scheduleMonth.equals("8") || scheduleMonth.equals("9")) {
                        month = "0" + scheduleMonth;
                    } else {
                        month = scheduleMonth;
                    }

                    if (scheduleDay.equals("1") || scheduleDay.equals("2") || scheduleDay.equals("3") || scheduleDay.equals("4") || scheduleDay.equals("5") || scheduleDay.equals("6") || scheduleDay.equals("7") || scheduleDay.equals("8") || scheduleDay.equals("9")) {
                        day = "0" + scheduleDay;
                    } else {
                        day = scheduleDay;
                    }


                    date = scheduleYear + "-" + month + "-" + day;
                    pharmacydate = scheduleYear + "-" + month + "-" + day;


                    findCalendar(date);

                    CalendarUtil c = new CalendarUtil();


                    int y = Integer.parseInt(scheduleYear);
                    int m = Integer.parseInt(month);
                    int d = Integer.parseInt(day);

                    String lunarCalendar = c.getChineseMonth(y, m, d) + c.getChineseDay(y, m, d);

                    c.setGregorian(y, m, d);
                    c.computeChineseFields();
                    c.computeSolarTerms();

                    if (c.judgefestival(m, d, lunarCalendar).equals("")) {
                        tv_solar_terms.setText(c.getDateString());
                    } else {
                        tv_solar_terms.setText(c.judgefestival(m, d, lunarCalendar));
                    }

                    String lauar1 = lauar.getLunar(scheduleYear, month, day);

                    if (tv_solar_terms.getText().toString().equals("")) {
                        tv_solar_terms.setVisibility(View.GONE);
                    } else {
                        tv_solar_terms.setVisibility(View.VISIBLE);
                    }

                    tv_chinese_era.setText(lauar1);

                    tv_lunar_calendar.setText("农历" + lunarCalendar);


                }
            }
        });
        gridView.setLayoutParams(params);
    }

    //点击日期获取当天信息
    public void findCalendar(final String date1) {


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/FindCalendar")
                        .addParams("userId", sp.getTag().getId() + "")
                        .addParams("date", date1)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {

                                if (response.equals("{}")) {
                                    handler.sendEmptyMessage(3);
                                } else {
                                    calendarinit = gson.fromJson(response, Calendarinit.class);
                                    handler.sendEmptyMessage(4);
                                }


                            }
                        });
            }
        });
        thread.start();
    }


    //获取健康日历页面数据
    public void findAllByDate(final String str) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/FindCalendarList")
                        .addParams("userId", sp.getTag().getId() + "")
                        .addParams("date", str)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                handler.sendEmptyMessage(2);
                            }

                            @Override
                            public void onResponse(String response, int id) {


                                Type listtype = new TypeToken<LinkedList<ConsultingRemindState>>() {
                                }.getType();
                                LinkedList<ConsultingRemindState> leclist = gson.fromJson(response, listtype);
                                for (Iterator it = leclist.iterator(); it.hasNext(); ) {
                                    ConsultingRemindState consultingRemindState = (ConsultingRemindState) it.next();
                                    data.add(consultingRemindState);
                                }
                                handler.sendEmptyMessage(1);
                            }

                        });
            }
        });
        thread.start();


    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.ll_right: // 下一个月
                enterNextMonth(gvFlag);
                break;
            case R.id.ll_left: // 上一个月
                enterPrevMonth(gvFlag);
                break;

            default:
                break;


        }
    }


    private View.OnClickListener addlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //跳转至用药设置
                case R.id.ll_add_medicine_remind:
                    Intent intent = new Intent(getContext(), CompileremindActivity.class);
                    startActivity(intent);
                    break;
                //跳转至健康状况
                case R.id.ll_add_health_condition:
                    Intent intent1 = new Intent(getContext(), HealthconditionActivity.class);
                    startActivity(intent1);
                    break;
            }
        }
    };

    private View.OnClickListener lllistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_registration_info:
                    Intent intent = new Intent(getContext(), ReservationlistActivity.class);
                    intent.putExtra("ReservationID", registrationId);
                    startActivity(intent);
                    break;
            }
        }
    };

    private View.OnClickListener remindlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_consulting_phramacy:
                    Intent intent = new Intent(getContext(), PharmacyremindActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };


}
