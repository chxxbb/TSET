package com.example.chen.tset.page;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import com.example.chen.tset.Data.CalendarSign;
import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.CalendarGridView;
import com.example.chen.tset.View.CompileremindActivity;
import com.example.chen.tset.View.HealthconditionActivity;
import com.example.chen.tset.View.PharmacyremindActivity;
import com.example.chen.tset.View.ReservationlistActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public class ConsultingFragment extends Fragment implements View.OnClickListener {
    View view;
    private RelativeLayout rl_loading;
    private ScrollView scrollview;
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

    List<CalendarSign> list;

    private LinearLayout ll_consulting_popup_case;


    private LinearLayout ll_registration, ll_health, ll_pharmacy, ll_registration_info;

    private ToggleButton tb_registration, tb_health, tb_pharmacy;

    private TextView tv_registration_info;

    //挂号提醒选择状态
    int registrationSelect = 1;

    //健康提醒选择状态
    int healthSelect = 1;


    //用药提醒选择状态
    int pharmacySelect = 1;

    String registrationId;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_consulting, null);


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
        calhttpinit();


        return view;
    }

    private void calhttpinit() {
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

            }
        }
    };


    private void findView() {
        rl_loading = (RelativeLayout) view.findViewById(R.id.rl_loading);
        scrollview = (ScrollView) view.findViewById(R.id.scrollview);
        scrollview.setVerticalScrollBarEnabled(false);

        currentMonth = (TextView) view.findViewById(R.id.currentMonth);
        prevMonth = (ImageView) view.findViewById(R.id.prevMonth);
        nextMonth = (ImageView) view.findViewById(R.id.nextMonth);

        ll_right = (LinearLayout) view.findViewById(R.id.ll_right);
        ll_left = (LinearLayout) view.findViewById(R.id.ll_left);

        ll_registration = (LinearLayout) view.findViewById(R.id.ll_registration);
        tb_registration = (ToggleButton) view.findViewById(R.id.tb_registration);
        ll_registration.setOnClickListener(tblistener);

        tb_health = (ToggleButton) view.findViewById(R.id.tb_health);
        ll_health = (LinearLayout) view.findViewById(R.id.ll_health);
        ll_health.setOnClickListener(tblistener);


        tb_pharmacy = (ToggleButton) view.findViewById(R.id.tb_pharmacy);
        ll_pharmacy = (LinearLayout) view.findViewById(R.id.ll_pharmacy);
        ll_pharmacy.setOnClickListener(tblistener);

        ll_consulting_popup_case = (LinearLayout) view.findViewById(R.id.ll_consulting_popup_case);

        ll_registration_info = (LinearLayout) view.findViewById(R.id.ll_registration_info);
        tv_registration_info = (TextView) view.findViewById(R.id.tv_registration_info);


        ll_left.setOnClickListener(this);
        ll_right.setOnClickListener(this);
        ll_consulting_popup_case.setOnClickListener(listener);


        if (tb_registration.isChecked()) {
            registrationSelect = 1;
        } else {
            registrationSelect = 2;
        }

        if (tb_health.isChecked()) {
            healthSelect = 1;
        } else {
            healthSelect = 2;
        }


        if (tb_pharmacy.isChecked()) {
            pharmacySelect = 1;
        } else {
            pharmacySelect = 2;
        }


    }


    private View.OnClickListener tblistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_registration:

                    if (registrationSelect == 1) {
                        tb_registration.setChecked(false);
                        registrationSelect = 2;
                    } else if (registrationSelect == 2) {
                        tb_registration.setChecked(true);
                        registrationSelect = 1;
                    }
                    break;


                case R.id.ll_health:
                    if (healthSelect == 1) {
                        tb_health.setChecked(false);
                        healthSelect = 2;
                    } else if (healthSelect == 2) {
                        tb_health.setChecked(true);
                        healthSelect = 1;
                    }
                    break;


                case R.id.ll_pharmacy:
                    if (pharmacySelect == 1) {
                        tb_pharmacy.setChecked(false);
                        pharmacySelect = 2;
                    } else if (pharmacySelect == 2) {
                        tb_pharmacy.setChecked(true);
                        pharmacySelect = 1;
                    }
                    break;
            }
        }
    };


    private void init() {

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
        CalendarSign sign = new CalendarSign("2016年9月28日", 1);
        CalendarSign sign1 = new CalendarSign("2016年9月22日", 2);
        CalendarSign sign2 = new CalendarSign("2016年9月7日", 3);
        CalendarSign sign3 = new CalendarSign("2016年8月7日", 3);
        CalendarSign sign4 = new CalendarSign("2016年10月7日", 3);
        list.add(sign);
        list.add(sign1);
        list.add(sign2);
        list.add(sign3);
        list.add(sign4);

        calV = new CalendarAdapter(getContext(), getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, list);
        addGridView();
        gridView.setAdapter(calV);
        flipper.addView(gridView, 0);
        addTextToTopTextView(currentMonth);


    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int gvFlag = 0; // 每次添加gridview到viewflipper中时给的标记
            if (e1.getX() - e2.getX() > 120) {
                // 像左滑动
                enterNextMonth(gvFlag);
                return true;
            } else if (e1.getX() - e2.getX() < -120) {
                // 向右滑动
                enterPrevMonth(gvFlag);
                return true;
            }
            return false;
        }
    }


    /**
     * 移动到下一个月
     *
     * @param gvFlag
     */
    private void enterNextMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth++; // 下一个月

        calV = new CalendarAdapter(getContext(), this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, list);
        gridView.setAdapter(calV);
        addTextToTopTextView(currentMonth); // 移动到下一月后，将当月显示在头标题中
        gvFlag++;
        flipper.addView(gridView, gvFlag);
        flipper.setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_left_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_left_out));
        flipper.showNext();
        flipper.removeViewAt(0);
    }

    /**
     * 移动到上一个月
     *
     * @param gvFlag
     */
    private void enterPrevMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth--; // 上一个月

        calV = new CalendarAdapter(getContext(), this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, list);
        gridView.setAdapter(calV);
        gvFlag++;
        addTextToTopTextView(currentMonth); // 移动到上一月后，将当月显示在头标题中
        flipper.addView(gridView, gvFlag);

        flipper.setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_right_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_right_out));
        flipper.showPrevious();
        flipper.removeViewAt(0);
    }


    /**
     * 添加头部的年份 闰哪月等信息
     *
     * @param view
     */
    public void addTextToTopTextView(TextView view) {
        StringBuffer textDate = new StringBuffer();
        textDate.append(calV.getShowYear()).append("年").append(calV.getShowMonth()).append("月").append("\t");
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


                    String date = scheduleYear + "-0" + scheduleMonth + "-" + scheduleDay;
                    Log.e("时间", date);
                    Log.e("userid", User_Http.user.getId() + "");
                    OkHttpUtils
                            .post()
                            .url(Http_data.http_data + "/FindUserIntradayByUserIdAndDate")
                            .addParams("userId", User_Http.user.getId() + "")
                            .addParams("date", date)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Log.e("失败", "失败");
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    Log.e("诊疗返回", response);
//                                    if (response.equals("0")) {
//                                        tv_registration_info.setText("无挂号");
//                                    } else {
//                                        tv_registration_info.setText("已挂号");
//                                        ll_registration_info.setOnClickListener(lllistener);
//                                        registrationId = response;
//
//                                    }


                                }
                            });


                }
            }
        });
        gridView.setLayoutParams(params);
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

    //诊疗页面弹出框
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //获取手机密度
            DisplayMetrics dm = new DisplayMetrics();
            dm = getContext().getResources().getDisplayMetrics();
            final float density = dm.density;


            View mPopWindowView = View.inflate(getContext(), R.layout.consulting_popuwidow_case, null);
            //调整弹出框位置
            final PopupWindow mPopWindow = new PopupWindow(mPopWindowView, (int) (density * 90), (int) (density * 100), true);
            mPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mPopWindow.showAsDropDown(v);


            LinearLayout ll_pharmacy = (LinearLayout) mPopWindowView.findViewById(R.id.ll_pharmacy);


            LinearLayout ll_health = (LinearLayout) mPopWindowView.findViewById(R.id.ll_health);

            //用药提醒
            ll_pharmacy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopWindow.dismiss();
                    Intent intent = new Intent(getContext(), CompileremindActivity.class);
                    startActivity(intent);
                }
            });


            //健康状况
            ll_health.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopWindow.dismiss();
                    Intent intent = new Intent(getContext(), HealthconditionActivity.class);
                    startActivity(intent);
                }
            });
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


}
