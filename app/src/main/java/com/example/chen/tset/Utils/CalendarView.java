package com.example.chen.tset.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.chen.tset.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 日历控件，支持旧历、节气、日期标注、点击操作 （参考网络上的日历控件改写）
 *
 * @author wangcccong
 * @version 1.406 create at: Mon, 03 Sep. 2014
 *          <br>update at: Mon, 23 Sep. 2014
 *          &nbsp;&nbsp;新增日期标注和点击操作
 */
public class CalendarView extends LinearLayout implements OnTouchListener,
        AnimationListener, OnGestureListener {


    /**
     * 点击日历
     */
    public interface OnCalendarViewListener {
        void onCalendarItemClick(CalendarView view, Date date);
    }

    /**
     * 顶部控件所占高度
     */
    private final static int TOP_HEIGHT = 48;
    /**
     * 日历item中默认id从0xff0000开始
     */
    private final static int DEFAULT_ID = 0xff0000;

    // 判断手势用
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    // 屏幕宽度和高度
    private int screenWidth;

    // 动画
    private Animation slideLeftIn;
    private Animation slideLeftOut;
    private Animation slideRightIn;
    private Animation slideRightOut;
    private ViewFlipper viewFlipper;
    private GestureDetector mGesture = null;

    /**
     * 上一月
     */
    private GridView gView1;
    /**
     * 当月
     */
    private GridView gView2;
    /**
     * 下一月
     */
    private GridView gView3;

    boolean bIsSelection = false;// 是否是选择事件发生
    private Calendar calStartDate = Calendar.getInstance();// 当前显示的日历
    private Calendar calSelected = Calendar.getInstance(); // 选择的日历
    private CalendarGridViewAdapter gAdapter;
    private CalendarGridViewAdapter gAdapter1;
    private CalendarGridViewAdapter gAdapter3;

    private LinearLayout mMainLayout;
    private TextView mTitle; // 显示年月

    private int iMonthViewCurrentMonth = 0; // 当前视图月
    private int iMonthViewCurrentYear = 0; // 当前视图年

    private static final int caltitleLayoutID = 66; // title布局ID
    private static final int calLayoutID = 55; // 日历布局ID
    private Context mContext;

    /**
     * 标注日期
     */
    private final List<Date> markDates;

    private OnCalendarViewListener mListener;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mContext = context;
        markDates = new ArrayList<Date>();
        init();
    }

    // 初始化相关工作
    protected void init() {
        // 得到屏幕的宽度
        screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;

//         滑动的动画
        slideLeftIn = new TranslateAnimation(screenWidth, 0, 0, 0);
        slideLeftIn.setDuration(200);
        slideLeftIn.setAnimationListener(this);
        slideLeftOut = new TranslateAnimation(0, -screenWidth, 0, 0);
        slideLeftOut.setDuration(200);
        slideLeftOut.setAnimationListener(this);
        slideRightIn = new TranslateAnimation(-screenWidth, 0, 0, 0);
        slideRightIn.setDuration(200);
        slideRightIn.setAnimationListener(this);
        slideRightOut = new TranslateAnimation(0, screenWidth, 0, 0);
        slideRightOut.setDuration(200);
        slideRightOut.setAnimationListener(this);

        // 手势操作
        mGesture = new GestureDetector(mContext, this);

        // 获取到当前日期
        UpdateStartDateForMonth();
        // 绘制界面
        setOrientation(LinearLayout.HORIZONTAL);
        mMainLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams main_params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mMainLayout.setLayoutParams(main_params);
        mMainLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        mMainLayout.setOrientation(LinearLayout.VERTICAL);
        addView(mMainLayout);

        // 顶部控件
        generateTopView();


        LinearLayout br = new LinearLayout(mContext);
        br.setBackgroundResource(R.drawable.consulting_wire);
        LinearLayout.LayoutParams params_br = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, 3);
        mMainLayout.addView(br, params_br);


        // 中间显示星期
        generateWeekGirdView();


        // 底部显示日历
        viewFlipper = new ViewFlipper(mContext);
        RelativeLayout.LayoutParams fliper_params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        fliper_params.addRule(RelativeLayout.BELOW, caltitleLayoutID);
        mMainLayout.addView(viewFlipper, fliper_params);
        generateClaendarGirdView();

        // 最下方的一条线条

    }

    /**
     * 生成顶部控件
     */
    @SuppressWarnings("deprecation")
    private void generateTopView() {
        // 顶部显示上一个下一个，以及当前年月
        RelativeLayout top = new RelativeLayout(mContext);
        top.setBackgroundColor(android.graphics.Color.parseColor("#6fc9e6"));
        LinearLayout.LayoutParams top_params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                ViewUtil.dip2px(mContext, TOP_HEIGHT));
        top.setLayoutParams(top_params);
        mMainLayout.addView(top);
        // 左方按钮、中间日期显示、右方按钮
        mTitle = new TextView(mContext);
        android.widget.RelativeLayout.LayoutParams title_params = new android.widget.RelativeLayout.LayoutParams(
                android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
                android.widget.RelativeLayout.LayoutParams.MATCH_PARENT);
        mTitle.setLayoutParams(title_params);
        mTitle.setTextColor(Color.WHITE);
        mTitle.setTextSize(18);
        mTitle.setFocusableInTouchMode(true);
        mTitle.setMarqueeRepeatLimit(-1);
        mTitle.setEllipsize(TruncateAt.MARQUEE);
        mTitle.setSingleLine(true);
        mTitle.setGravity(Gravity.CENTER);
        mTitle.setHorizontallyScrolling(true);
        mTitle.setText("2014年9月");
        top.addView(mTitle);

        // 左方按钮
        ImageButton mLeftView = new ImageButton(mContext);
        mLeftView.setBackgroundResource(R.drawable.consulting_left_arrows);



        android.widget.RelativeLayout.LayoutParams leftPP = new android.widget.RelativeLayout.LayoutParams(
                ViewUtil.dip2px(mContext, 15), ViewUtil.dip2px(mContext, 22));
        leftPP.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        leftPP.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        DisplayMetrics dm = new DisplayMetrics();
        dm = mContext.getResources().getDisplayMetrics();
        final float density = dm.density;

        if (density == 1.5) {
            leftPP.setMargins((int) (density * 80), 0, 0, 0);
        } else {
            leftPP.setMargins((int) (density * 110), 0, 0, 0);
        }
        mLeftView.setLayoutParams(leftPP);
        mLeftView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                viewFlipper.setInAnimation(slideRightIn);
                viewFlipper.setOutAnimation(slideRightOut);
                viewFlipper.showPrevious();
                setPrevViewItem();
            }
        });
        top.addView(mLeftView);

        // 右方按钮
        ImageButton mRightView = new ImageButton(mContext);
        mRightView.setBackgroundResource(R.drawable.consulting_right_arrows);

        android.widget.RelativeLayout.LayoutParams rightPP = new android.widget.RelativeLayout.LayoutParams(
                ViewUtil.dip2px(mContext, 15), ViewUtil.dip2px(mContext, 22));
        rightPP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rightPP.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);


        if (density == 1.5) {
            rightPP.setMargins(0, 0, (int) (density * 80), 0);
        } else {
            rightPP.setMargins(0, 0, (int) (density * 110), 0);
        }

        mRightView.setLayoutParams(rightPP);
        mRightView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                viewFlipper.setInAnimation(slideLeftIn);
                viewFlipper.setOutAnimation(slideLeftOut);
                viewFlipper.showNext();
                setNextViewItem();
            }
        });
        top.addView(mRightView);


        ImageButton textView = new ImageButton(mContext);

        textView.setBackgroundResource(R.drawable.consulting_more);
        android.widget.RelativeLayout.LayoutParams righttext = new android.widget.RelativeLayout.LayoutParams((int) (density * 18), (int) (density * 18));
        righttext.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        righttext.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        righttext.setMargins(0, 0, (int) (density * 15), 0);
        textView.setLayoutParams(righttext);
        textView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                View mPopWindowView = View.inflate(mContext, R.layout.consulting_popuwidow_case, null);
                final PopupWindow mPopWindow = new PopupWindow(mPopWindowView, (int) (density * 90), (int) (density * 100), true);
                mPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mPopWindow.showAsDropDown(v);
                LinearLayout ll_pharmacy = (LinearLayout) mPopWindowView.findViewById(R.id.ll_pharmacy);
                LinearLayout ll_health = (LinearLayout) mPopWindowView.findViewById(R.id.ll_health);
                ll_pharmacy.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "用药提醒", Toast.LENGTH_SHORT).show();
                        mPopWindow.dismiss();
                    }
                });
                ll_health.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "健康状况", Toast.LENGTH_SHORT).show();
                        mPopWindow.dismiss();
                    }
                });
            }
        });
        top.addView(textView);
    }

    /**
     * 生成中间显示week
     */
    private void generateWeekGirdView() {
        GridView gridView = new GridView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        gridView.setLayoutParams(params);
        gridView.setNumColumns(7);// 设置每行列数
        gridView.setGravity(Gravity.CENTER_VERTICAL);// 位置居中
        gridView.setVerticalSpacing(1);// 垂直间隔
        gridView.setHorizontalSpacing(1);// 水平间隔


        int i = screenWidth / 7;
        int j = screenWidth - (i * 7);
        int x = j / 2;
        gridView.setPadding(x, 25, 0, 25);// 居中
        WeekGridAdapter weekAdapter = new WeekGridAdapter(mContext);
        gridView.setAdapter(weekAdapter);// 设置菜单Adapter
        mMainLayout.addView(gridView);
    }

    /**
     * 生成底部日历
     */
    public void generateClaendarGirdView() {
        Calendar tempSelected1 = Calendar.getInstance(); // 临时
        Calendar tempSelected2 = Calendar.getInstance(); // 临时
        Calendar tempSelected3 = Calendar.getInstance(); // 临时
        tempSelected1.setTime(calStartDate.getTime());
        tempSelected2.setTime(calStartDate.getTime());
        tempSelected3.setTime(calStartDate.getTime());

        gView1 = new CalendarGridView(mContext);
        tempSelected1.add(Calendar.MONTH, -1);
        gAdapter1 = new CalendarGridViewAdapter(mContext, tempSelected1, markDates);
        gView1.setAdapter(gAdapter1);// 设置菜单Adapter

        gView1.setId(calLayoutID);
        gView2 = new CalendarGridView(mContext);
        gAdapter = new CalendarGridViewAdapter(mContext, tempSelected2, markDates);
        gView2.setAdapter(gAdapter);// 设置菜单Adapter
        gView2.setId(calLayoutID);


        gView3 = new CalendarGridView(mContext);
        tempSelected3.add(Calendar.MONTH, 1);
        gAdapter3 = new CalendarGridViewAdapter(mContext, tempSelected3, markDates);
        gView3.setAdapter(gAdapter3);// 设置菜单Adapter
        gView3.setId(calLayoutID);

        gView2.setOnTouchListener(this);
        gView1.setOnTouchListener(this);
        gView3.setOnTouchListener(this);


        if (viewFlipper.getChildCount() != 0) {
            viewFlipper.removeAllViews();
        }

        viewFlipper.addView(gView2);
        viewFlipper.addView(gView3);
        viewFlipper.addView(gView1);

        String title = calStartDate.get(Calendar.YEAR)
                + "年"
                + NumberHelper.LeftPad_Tow_Zero(calStartDate
                .get(Calendar.MONTH) + 1) + "月";
        mTitle.setText(title);
    }

    // 上一个月
    private void setPrevViewItem() {
        iMonthViewCurrentMonth--;// 当前选择月--
        // 如果当前月为负数的话显示上一年
        if (iMonthViewCurrentMonth == -1) {
            iMonthViewCurrentMonth = 11;
            iMonthViewCurrentYear--;
        }
        calStartDate.set(Calendar.DAY_OF_MONTH, 1); // 设置日为当月1日
        calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth); // 设置月
        calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear); // 设置年
    }

    // 下一个月
    private void setNextViewItem() {
        iMonthViewCurrentMonth++;
        if (iMonthViewCurrentMonth == 12) {
            iMonthViewCurrentMonth = 0;
            iMonthViewCurrentYear++;
        }
        calStartDate.set(Calendar.DAY_OF_MONTH, 1);
        calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
        calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
    }

    // 根据改变的日期更新日历
    // 填充日历控件用
    private void UpdateStartDateForMonth() {
        calStartDate.set(Calendar.DATE, 1); // 设置成当月第一天
        iMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);// 得到当前日历显示的月
        iMonthViewCurrentYear = calStartDate.get(Calendar.YEAR);// 得到当前日历显示的年

        // 星期一是2 星期天是1 填充剩余天数
        int iDay = 0;
        int iFirstDayOfWeek = Calendar.MONDAY;
        int iStartDay = iFirstDayOfWeek;
        if (iStartDay == Calendar.MONDAY) {
            iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
            if (iDay < 0)
                iDay = 6;
        }
        if (iStartDay == Calendar.SUNDAY) {
            iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
            if (iDay < 0)
                iDay = 6;
        }

    }

    /**
     * 设置标注的日期
     *
     * @param markDates
     */
    public void setMarkDates(List<Date> markDates) {
        this.markDates.clear();
        this.markDates.addAll(markDates);
        gAdapter.notifyDataSetChanged();
        gAdapter1.notifyDataSetChanged();
        gAdapter3.notifyDataSetChanged();
    }


    /**
     * 设置点击日历监听
     *
     * @param listener
     */
    public void setOnCalendarViewListener(OnCalendarViewListener listener) {
        this.mListener = listener;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGesture.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        // TODO Auto-generated method stub
        try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
            // right to left swipe
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                viewFlipper.setInAnimation(slideLeftIn);
                viewFlipper.setOutAnimation(slideLeftOut);
                viewFlipper.showNext();
                setNextViewItem();
                return true;

            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                viewFlipper.setInAnimation(slideRightIn);
                viewFlipper.setOutAnimation(slideRightOut);
                viewFlipper.showPrevious();
                setPrevViewItem();
                return true;

            }
        } catch (Exception e) {
            // nothing
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        // 得到当前选中的是第几个单元格
        int pos = gView2.pointToPosition((int) e.getX(), (int) e.getY());
        LinearLayout txtDay = (LinearLayout) gView2.findViewById(pos + DEFAULT_ID);
        if (txtDay != null) {
            if (txtDay.getTag() != null) {
                Date date = (Date) txtDay.getTag();
                calSelected.setTime(date);
                gAdapter.setSelectedDate(calSelected);
//                gAdapter.notifyDataSetChanged();

                if (mListener != null)
                    mListener.onCalendarItemClick(this, date);
            }
        }
        return false;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // TODO Auto-generated method stub
        generateClaendarGirdView();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }

}

/**
 * 显示week的布局adapter
 */
class WeekGridAdapter extends BaseAdapter {

    final String[] titles = new String[]{"日", "一", "二", "三", "四", "五", "六"};
    private Context mContext;

    public WeekGridAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return titles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView week = new TextView(mContext);
        android.view.ViewGroup.LayoutParams week_params = new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        week.setLayoutParams(week_params);
        week.setGravity(Gravity.CENTER);
        week.setTextColor(Color.WHITE);
        week.setText(getItem(position)+"");
        return week;
    }
}

/**
 * 显示日期的adapter
 */
class CalendarGridViewAdapter extends BaseAdapter {

    /**
     * 日历item中默认id从0xff0000开始
     */
    private final static int DEFAULT_ID = 0xff0000;
    private Calendar calStartDate = Calendar.getInstance();// 当前显示的日历
    private Calendar calSelected = Calendar.getInstance(); // 选择的日历

    /**
     * 标注的日期
     */
    private List<Date> markDates;

    private Context mContext;


    private Calendar calToday = Calendar.getInstance(); // 今日

    private ArrayList<Date> titles;

    private ArrayList<Date> getDates() {

        UpdateStartDateForMonth();

        ArrayList<Date> alArrayList = new ArrayList<Date>();

        for (int i = 1; i <= 42; i++) {
            alArrayList.add(calStartDate.getTime());
            calStartDate.add(Calendar.DAY_OF_MONTH, 1);
        }

        return alArrayList;
    }


    // construct
    public CalendarGridViewAdapter(Context context, Calendar cal, List<Date> dates) {
        calStartDate = cal;
        this.mContext = context;
        titles = getDates();
        this.markDates = dates;


    }


    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return titles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 整个Item
        LinearLayout itemLayout = new LinearLayout(mContext);
        itemLayout.setId(position + DEFAULT_ID);
        itemLayout.setGravity(Gravity.CENTER);
        itemLayout.setOrientation(1);


        DisplayMetrics dm = new DisplayMetrics();
        dm = mContext.getResources().getDisplayMetrics();
        float density = dm.density;

        int i;
        if (density == 1.5) {
            i = (int) (5 * density);
        }else if(density==3.0){
            i= (int) (12*density);
        } else {
            i = (int) (10 * density);
        }

        itemLayout.setPadding(0, i, 0, i);


        Date myDate = (Date) getItem(position);
        itemLayout.setTag(myDate);
        Calendar calCalendar = Calendar.getInstance();
        calCalendar.setTime(myDate);

        // 显示日期day
        TextView textDay = new TextView(mContext);// 日期
        LinearLayout.LayoutParams text_params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        textDay.setGravity(Gravity.CENTER_HORIZONTAL);
        int day = myDate.getDate(); // 日期
        textDay.setText(String.valueOf(day));
        textDay.setId(position + DEFAULT_ID);
        itemLayout.addView(textDay, text_params);

        // 显示公历
        TextView chineseDay = new TextView(mContext);
        LinearLayout.LayoutParams chinese_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        chineseDay.setGravity(Gravity.CENTER_HORIZONTAL);
        chineseDay.setTextSize(9);
        CalendarUtil calendarUtil = new CalendarUtil(calCalendar);
        chineseDay.setText(calendarUtil.toString());
        itemLayout.addView(chineseDay, chinese_params);
        textDay.setTextColor(android.graphics.Color.parseColor("#ffffff"));
        chineseDay.setTextColor(android.graphics.Color.parseColor("#ffffff"));


        // 如果是当前日期则显示不同颜色
        if (equalsDate(calToday.getTime(), myDate)) {
            textDay.setTextColor(android.graphics.Color.parseColor("#f9e491"));
            chineseDay.setTextColor(android.graphics.Color.parseColor("#f9e491"));
        }

        SimpleDateFormat sim2 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            for (int g = 0; g < Consultinginit.list.size(); g++) {
                Date d2 = sim2.parse(Consultinginit.list.get(g));
                if (equalsDate(d2, myDate)) {
                    if (Consultinginit.slist.get(g).equals("1")) {
                        itemLayout.setBackgroundResource(R.drawable.consulting3);
                    } else if (Consultinginit.slist.get(g).equals("2")) {
                        itemLayout.setBackgroundResource(R.drawable.consulting1);
                    } else if (Consultinginit.slist.get(g).equals("3")) {
                        itemLayout.setBackgroundResource(R.drawable.consulting2);
                    }

                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (equalsDate(calSelected.getTime(), myDate)) {
            // 选择的
            itemLayout.setBackgroundResource(R.drawable.consulting_onclick);


        }


        String title = calStartDate.get(Calendar.YEAR)
                + "-"
                + NumberHelper.LeftPad_Tow_Zero(calStartDate
                .get(Calendar.MONTH)) + "-1";


        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = sim.parse(title);
            if (!CalendarUtil.compare(myDate, d)) {
                itemLayout.setVisibility(View.GONE);
                textDay.setEnabled(false);
                chineseDay.setEnabled(false);
                itemLayout.setClickable(false);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        String title1;


        if (NumberHelper.LeftPad_Tow_Zero(calStartDate.get(Calendar.MONTH)).equals("01") || NumberHelper.LeftPad_Tow_Zero(calStartDate.get(Calendar.MONTH)).equals("03") || NumberHelper.LeftPad_Tow_Zero(calStartDate.get(Calendar.MONTH)).equals("05") ||
                NumberHelper.LeftPad_Tow_Zero(calStartDate.get(Calendar.MONTH)).equals("07") || NumberHelper.LeftPad_Tow_Zero(calStartDate.get(Calendar.MONTH)).equals("08") || NumberHelper.LeftPad_Tow_Zero(calStartDate.get(Calendar.MONTH)).equals("10") ||
                NumberHelper.LeftPad_Tow_Zero(calStartDate.get(Calendar.MONTH)).equals("12") || NumberHelper.LeftPad_Tow_Zero(calStartDate.get(Calendar.MONTH)).equals("00")) {

            title1 = calStartDate.get(Calendar.YEAR)
                    + "-"
                    + NumberHelper.LeftPad_Tow_Zero(calStartDate
                    .get(Calendar.MONTH)) + "-31";

        } else if (NumberHelper.LeftPad_Tow_Zero(calStartDate.get(Calendar.MONTH)).equals("04") || NumberHelper.LeftPad_Tow_Zero(calStartDate.get(Calendar.MONTH)).equals("06") || NumberHelper.LeftPad_Tow_Zero(calStartDate.get(Calendar.MONTH)).equals("09") ||
                NumberHelper.LeftPad_Tow_Zero(calStartDate.get(Calendar.MONTH)).equals("11")) {

            title1 = calStartDate.get(Calendar.YEAR)
                    + "-"
                    + NumberHelper.LeftPad_Tow_Zero(calStartDate
                    .get(Calendar.MONTH)) + "-30";

        } else if (NumberHelper.LeftPad_Tow_Zero(calStartDate.get(Calendar.MONTH)).equals("02") && calStartDate.get(Calendar.YEAR) % 4 == 0) {
            title1 = calStartDate.get(Calendar.YEAR)
                    + "-"
                    + NumberHelper.LeftPad_Tow_Zero(calStartDate
                    .get(Calendar.MONTH)) + "-29";
        } else {
            title1 = calStartDate.get(Calendar.YEAR)
                    + "-"
                    + NumberHelper.LeftPad_Tow_Zero(calStartDate
                    .get(Calendar.MONTH)) + "-28";
        }


        try {
            Date d = sim.parse(title1);
            if (!CalendarUtil.compare(d, myDate)) {
                itemLayout.setVisibility(View.GONE);
                textDay.setEnabled(false);
                chineseDay.setEnabled(false);
                itemLayout.setClickable(false);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        /** 设置标注日期颜色 */
        if (markDates != null) {
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            for (Date date : markDates) {
                if (format.format(myDate).equals(format.format(date))) {
                    itemLayout.setBackgroundColor(android.graphics.Color.parseColor("#6fc8e6"));
                    break;
                }
            }
        }


        return itemLayout;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @SuppressWarnings("deprecation")
    private Boolean equalsDate(Date date1, Date date2) {
        if (date1.getYear() == date2.getYear()
                && date1.getMonth() == date2.getMonth()
                && date1.getDate() == date2.getDate()) {
            return true;
        } else {
            return false;
        }

    }

    // 根据改变的日期更新日历
    // 填充日历控件用
    private void UpdateStartDateForMonth() {
        calStartDate.set(Calendar.DATE, 1); // 设置成当月第一天

        // 星期一是2 星期天是1 填充剩余天数
        int iDay = 0;
        int iFirstDayOfWeek = Calendar.MONDAY;
        int iStartDay = iFirstDayOfWeek;
        if (iStartDay == Calendar.MONDAY) {
            iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
            if (iDay < 0)
                iDay = 6;
        }
        if (iStartDay == Calendar.SUNDAY) {
            iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
            if (iDay < 0)
                iDay = 6;
        }
        calStartDate.add(Calendar.DAY_OF_WEEK, -iDay);

        calStartDate.add(Calendar.DAY_OF_MONTH, -1);// 周日第一位
    }

    public void setSelectedDate(Calendar cal) {
        calSelected = cal;
    }

}

/**
 * 用于生成日历展示的GridView布局
 */
class CalendarGridView extends GridView {

    /**
     * 当前操作的上下文对象
     */
    private Context mContext;

    /**
     * CalendarGridView 构造器
     *
     * @param context 当前操作的上下文对象
     */
    public CalendarGridView(Context context) {
        super(context);
        mContext = context;
        initGirdView();
    }

    /**
     * 初始化gridView 控件的布局
     */
    private void initGirdView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);
        setNumColumns(7);// 设置每行列数
        setGravity(Gravity.CENTER_VERTICAL);// 位置居中
        setVerticalSpacing(1);// 垂直间隔
        setHorizontalSpacing(1);// 水平间隔

        int i = mContext.getResources().getDisplayMetrics().widthPixels / 7;
        int j = mContext.getResources().getDisplayMetrics().widthPixels - (i * 7);
        int x = j / 2;
        setPadding(x, 0, 0, 0);// 居中
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

/**
 * 把公历时间处理成农历时间
 */
class CalendarUtil {
    /**
     * 用于保存中文的月份
     */
    private final static String CHINESE_NUMBER[] = {"一", "二", "三", "四", "五",
            "六", "七", "八", "九", "十", "十一", "腊"};

    /**
     * 用于保存展示周几使用
     */
    private final static String WEEK_NUMBER[] = {"日", "一", "二", "三", "四", "五",
            "六"};

    private final static long[] LUNAR_INFO = new long[]{0x04bd8, 0x04ae0,
            0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0,
            0x055d2, 0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540,
            0x0d6a0, 0x0ada2, 0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5,
            0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970,
            0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3,
            0x092e0, 0x1c8d7, 0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0,
            0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0,
            0x0b550, 0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8,
            0x0e950, 0x06aa0, 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570,
            0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5,
            0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0,
            0x195a6, 0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50,
            0x06d40, 0x0af46, 0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0,
            0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0,
            0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7,
            0x025d0, 0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50,
            0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954,
            0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260,
            0x0ea65, 0x0d530, 0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0,
            0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0,
            0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20,
            0x0ada0};

    /**
     * 转换为2012年11月22日格式
     */
    private static SimpleDateFormat chineseDateFormat = new SimpleDateFormat(
            "yyyy年MM月dd日");
    /**
     * 转换为2012-11-22格式
     */
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd");

    /**
     * 计算得到农历的年份
     */
    private int mLuchYear;
    /**
     * 计算得到农历的月份
     */
    private int mLuchMonth;

    /**
     * 计算得到农历的日期
     */
    private int mLuchDay;

    /**
     * 用于标识是事为闰年
     */
    private boolean isLoap;

    /**
     * 用于记录当前处理的时间
     */
    private Calendar mCurrenCalendar;

    /**
     * 传回农历 year年的总天数
     *
     * @param year 将要计算的年份
     * @return 返回传入年份的总天数
     */
    private static int yearDays(int year) {
        int i, sum = 348;
        for (i = 0x8000; i > 0x8; i >>= 1) {
            if ((LUNAR_INFO[year - 1900] & i) != 0)
                sum += 1;
        }
        return (sum + leapDays(year));
    }

    /**
     * 传回农历 year年闰月的天数
     *
     * @param year 将要计算的年份
     * @return 返回 农历 year年闰月的天数
     */
    private static int leapDays(int year) {
        if (leapMonth(year) != 0) {
            if ((LUNAR_INFO[year - 1900] & 0x10000) != 0)
                return 30;
            else
                return 29;
        } else
            return 0;
    }

    /**
     * 传回农历 year年闰哪个月 1-12 , 没闰传回 0
     *
     * @param year 将要计算的年份
     * @return 传回农历 year年闰哪个月 1-12 , 没闰传回 0
     */
    private static int leapMonth(int year) {
        return (int) (LUNAR_INFO[year - 1900] & 0xf);
    }

    /**
     * 传回农历 year年month月的总天数
     *
     * @param year  将要计算的年份
     * @param month 将要计算的月份
     * @return 传回农历 year年month月的总天数
     */
    private static int monthDays(int year, int month) {
        if ((LUNAR_INFO[year - 1900] & (0x10000 >> month)) == 0)
            return 29;
        else
            return 30;
    }


    // ====== 传入 月日的offset 传回干支, 0=甲子
    private static String cyclicalm(int num) {
        final String[] Gan = new String[]{"甲", "乙", "丙", "丁", "戊", "己", "庚",
                "辛", "壬", "癸"};
        final String[] Zhi = new String[]{"子", "丑", "寅", "卯", "辰", "巳", "午",
                "未", "申", "酉", "戌", "亥"};

        return (Gan[num % 10] + Zhi[num % 12]);
    }



    /**
     * 传出y年m月d日对应的农历. yearCyl3:农历年与1864的相差数 ? monCyl4:从1900年1月31日以来,闰月数
     * dayCyl5:与1900年1月31日相差的天数,再加40 ?
     *
     * @param cal
     * @return
     */
    public CalendarUtil(Calendar cal) {
        mCurrenCalendar = cal;
        int leapMonth = 0;
        Date baseDate = null;
        try {
            baseDate = chineseDateFormat.parse("1900年1月31日");
        } catch (ParseException e) {
            e.printStackTrace(); // To change body of catch statement use
            // Options | File Templates.
        }

        // 求出和1900年1月31日相差的天数
        int offset = (int) ((cal.getTime().getTime() - baseDate.getTime()) / 86400000L);
        // 用offset减去每农历年的天数
        // 计算当天是农历第几天
        // i最终结果是农历的年份
        // offset是当年的第几天
        int iYear, daysOfYear = 0;
        for (iYear = 1900; iYear < 2050 && offset > 0; iYear++) {
            daysOfYear = yearDays(iYear);
            offset -= daysOfYear;
        }
        if (offset < 0) {
            offset += daysOfYear;
            iYear--;
        }
        // 农历年份
        mLuchYear = iYear;

        leapMonth = leapMonth(iYear); // 闰哪个月,1-12
        isLoap = false;

        // 用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
        int iMonth, daysOfMonth = 0;
        for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
            // 闰月
            if (leapMonth > 0 && iMonth == (leapMonth + 1) && !isLoap) {
                --iMonth;
                isLoap = true;
                daysOfMonth = leapDays(mLuchYear);
            } else
                daysOfMonth = monthDays(mLuchYear, iMonth);

            offset -= daysOfMonth;
            // 解除闰月
            if (isLoap && iMonth == (leapMonth + 1))
                isLoap = false;
            if (!isLoap) {
            }
        }
        // offset为0时，并且刚才计算的月份是闰月，要校正
        if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
            if (isLoap) {
                isLoap = false;
            } else {
                isLoap = true;
                --iMonth;
            }
        }
        // offset小于0时，也要校正
        if (offset < 0) {
            offset += daysOfMonth;
            --iMonth;

        }
        mLuchMonth = iMonth;
        mLuchDay = offset + 1;
    }

    /**
     * 返化成中文格式
     *
     * @param day
     * @return
     */
    public static String getChinaDayString(int day) {
        String chineseTen[] = {"初", "十", "廿", "卅"};
        int n = day % 10 == 0 ? 9 : day % 10 - 1;
        if (day > 30)
            return "";
        if (day == 10)
            return "初十";
        else
            return chineseTen[day / 10] + CHINESE_NUMBER[n];
    }

    /**
     * 用于显示农历的初几这种格式
     *
     * @return 农历的日期
     */
    public String toString() {
        String message = "";
        // int n = mLuchDay % 10 == 0 ? 9 : mLuchDay % 10 - 1;
        message = getChinaCalendarMsg(mLuchYear, mLuchMonth, mLuchDay);
        if (StringUtil.isNullOrEmpty(message)) {
            String solarMsg = new SolarTermsUtil(mCurrenCalendar)
                    .getSolartermsMsg();
            // 判断当前日期是否为节气
            if (!StringUtil.isNullOrEmpty(solarMsg)) {
                message = solarMsg;
            } else {
                /**
                 * 判断当前日期是否为公历节日
                 */
                String gremessage = new GregorianUtil(mCurrenCalendar)
                        .getGremessage();
                if (!StringUtil.isNullOrEmpty(gremessage)) {
                    message = gremessage;
                } else if (mLuchDay == 1) {
                    message = CHINESE_NUMBER[mLuchMonth - 1] + "月";
                } else {
                    message = getChinaDayString(mLuchDay);
                }

            }
        }
        return message;
    }

    /**
     * 返回农历的年月日
     *
     * @return 农历的年月日格式
     */
    public String getDay() {
        return (isLoap ? "闰" : "") + CHINESE_NUMBER[mLuchMonth - 1] + "月"
                + getChinaDayString(mLuchDay);
    }

    /**
     * 把calendar转化为当前年月日
     *
     * @param calendar Calendar
     * @return 返回成转换好的 年月日格式
     */
    public static String getDay(Calendar calendar) {
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 用于比对二个日期的大小
     *
     * @param compareDate 将要比对的时间
     * @param currentDate 当前时间
     * @return true 表示大于当前时间 false 表示小于当前时间
     */
    public static boolean compare(Date compareDate, Date currentDate) {
        return chineseDateFormat.format(compareDate).compareTo(
                chineseDateFormat.format(currentDate)) >= 0;
    }

    /**
     * 获取当前周几
     *
     * @param calendar
     * @return
     */
    public static String getWeek(Calendar calendar) {
        return "周" + WEEK_NUMBER[calendar.get(Calendar.DAY_OF_WEEK) - 1] + "";
    }



    /**
     * 用于获取中国的传统节日
     *
     * @param month 农历的月
     * @param day   农历日
     * @return 中国传统节日
     */
    private String getChinaCalendarMsg(int year, int month, int day) {
        String message = "";
        if (((month) == 1) && day == 1) {
            message = "春节";
        } else if (((month) == 1) && day == 15) {
            message = "元宵";
        } else if (((month) == 5) && day == 5) {
            message = "端午";
        } else if ((month == 7) && day == 7) {
            message = "七夕";
        } else if (((month) == 8) && day == 15) {
            message = "中秋";
        } else if ((month == 9) && day == 9) {
            message = "重阳";
        } else if ((month == 12) && day == 8) {
            message = "腊八";
        } else {
            if (month == 12) {
                if ((((monthDays(year, month) == 29) && day == 29))
                        || ((((monthDays(year, month) == 30) && day == 30)))) {
                    message = "除夕";
                }
            }
        }
        return message;
    }
}

/**
 * 字符串的处理类
 */
class StringUtil {
    /**
     * 判断是否为null或空值
     *
     * @param str String
     * @return true or false
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断str1和str2是否相同
     *
     * @param str1 str1
     * @param str2 str2
     * @return true or false
     */
    public static boolean equals(String str1, String str2) {
        return str1 == str2 || str1 != null && str1.equals(str2);
    }


}

/**
 * 主要用于把公历日期处理成24节气
 */
class SolarTermsUtil {

    /**
     * 计算得到公历的年份
     */
    private int gregorianYear;

    /**
     * 计算得到公历的月份
     */
    private int gregorianMonth;

    /**
     * 用于计算得到公历的日期
     */
    private int gregorianDate;

    private int chineseYear;
    private int chineseMonth;
    private int chineseDate;

    // 初始日，公历农历对应日期：
    // 公历 1901 年 1 月 1 日，对应农历 4598 年 11 月 11 日
    private static int baseYear = 1901;
    private static int baseMonth = 1;
    private static int baseDate = 1;
    private static int baseIndex = 0;
    private static int baseChineseYear = 4598 - 1;
    private static int baseChineseMonth = 11;
    private static int baseChineseDate = 11;
    private static char[] daysInGregorianMonth = {31, 28, 31, 30, 31, 30, 31,
            31, 30, 31, 30, 31};

    private int sectionalTerm;
    private int principleTerm;

    private static char[][] sectionalTermMap = {
            {7, 6, 6, 6, 6, 6, 6, 6, 6, 5, 6, 6, 6, 5, 5, 6, 6, 5, 5, 5, 5, 5,
                    5, 5, 5, 4, 5, 5},
            {5, 4, 5, 5, 5, 4, 4, 5, 5, 4, 4, 4, 4, 4, 4, 4, 4, 3, 4, 4, 4, 3,
                    3, 4, 4, 3, 3, 3},
            {6, 6, 6, 7, 6, 6, 6, 6, 5, 6, 6, 6, 5, 5, 6, 6, 5, 5, 5, 6, 5, 5,
                    5, 5, 4, 5, 5, 5, 5},
            {5, 5, 6, 6, 5, 5, 5, 6, 5, 5, 5, 5, 4, 5, 5, 5, 4, 4, 5, 5, 4, 4,
                    4, 5, 4, 4, 4, 4, 5},
            {6, 6, 6, 7, 6, 6, 6, 6, 5, 6, 6, 6, 5, 5, 6, 6, 5, 5, 5, 6, 5, 5,
                    5, 5, 4, 5, 5, 5, 5},
            {6, 6, 7, 7, 6, 6, 6, 7, 6, 6, 6, 6, 5, 6, 6, 6, 5, 5, 6, 6, 5, 5,
                    5, 6, 5, 5, 5, 5, 4, 5, 5, 5, 5},
            {7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7, 7, 7, 6, 7, 7, 7, 6, 6,
                    7, 7, 6, 6, 6, 7, 7},
            {8, 8, 8, 9, 8, 8, 8, 8, 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7,
                    7, 7, 6, 7, 7, 7, 6, 6, 7, 7, 7},
            {8, 8, 8, 9, 8, 8, 8, 8, 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7,
                    7, 7, 6, 7, 7, 7, 7},
            {9, 9, 9, 9, 8, 9, 9, 9, 8, 8, 9, 9, 8, 8, 8, 9, 8, 8, 8, 8, 7, 8,
                    8, 8, 7, 7, 8, 8, 8},
            {8, 8, 8, 8, 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7, 7, 7, 6, 7,
                    7, 7, 6, 6, 7, 7, 7},
            {7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7, 7, 7, 6, 7, 7, 7, 6, 6,
                    7, 7, 6, 6, 6, 7, 7}};
    private static char[][] sectionalTermYear = {
            {13, 49, 85, 117, 149, 185, 201, 250, 250},
            {13, 45, 81, 117, 149, 185, 201, 250, 250},
            {13, 48, 84, 112, 148, 184, 200, 201, 250},
            {13, 45, 76, 108, 140, 172, 200, 201, 250},
            {13, 44, 72, 104, 132, 168, 200, 201, 250},
            {5, 33, 68, 96, 124, 152, 188, 200, 201},
            {29, 57, 85, 120, 148, 176, 200, 201, 250},
            {13, 48, 76, 104, 132, 168, 196, 200, 201},
            {25, 60, 88, 120, 148, 184, 200, 201, 250},
            {16, 44, 76, 108, 144, 172, 200, 201, 250},
            {28, 60, 92, 124, 160, 192, 200, 201, 250},
            {17, 53, 85, 124, 156, 188, 200, 201, 250}};
    private static char[][] principleTermMap = {
            {21, 21, 21, 21, 21, 20, 21, 21, 21, 20, 20, 21, 21, 20, 20, 20,
                    20, 20, 20, 20, 20, 19, 20, 20, 20, 19, 19, 20},
            {20, 19, 19, 20, 20, 19, 19, 19, 19, 19, 19, 19, 19, 18, 19, 19,
                    19, 18, 18, 19, 19, 18, 18, 18, 18, 18, 18, 18},
            {21, 21, 21, 22, 21, 21, 21, 21, 20, 21, 21, 21, 20, 20, 21, 21,
                    20, 20, 20, 21, 20, 20, 20, 20, 19, 20, 20, 20, 20},
            {20, 21, 21, 21, 20, 20, 21, 21, 20, 20, 20, 21, 20, 20, 20, 20,
                    19, 20, 20, 20, 19, 19, 20, 20, 19, 19, 19, 20, 20},
            {21, 22, 22, 22, 21, 21, 22, 22, 21, 21, 21, 22, 21, 21, 21, 21,
                    20, 21, 21, 21, 20, 20, 21, 21, 20, 20, 20, 21, 21},
            {22, 22, 22, 22, 21, 22, 22, 22, 21, 21, 22, 22, 21, 21, 21, 22,
                    21, 21, 21, 21, 20, 21, 21, 21, 20, 20, 21, 21, 21},
            {23, 23, 24, 24, 23, 23, 23, 24, 23, 23, 23, 23, 22, 23, 23, 23,
                    22, 22, 23, 23, 22, 22, 22, 23, 22, 22, 22, 22, 23},
            {23, 24, 24, 24, 23, 23, 24, 24, 23, 23, 23, 24, 23, 23, 23, 23,
                    22, 23, 23, 23, 22, 22, 23, 23, 22, 22, 22, 23, 23},
            {23, 24, 24, 24, 23, 23, 24, 24, 23, 23, 23, 24, 23, 23, 23, 23,
                    22, 23, 23, 23, 22, 22, 23, 23, 22, 22, 22, 23, 23},
            {24, 24, 24, 24, 23, 24, 24, 24, 23, 23, 24, 24, 23, 23, 23, 24,
                    23, 23, 23, 23, 22, 23, 23, 23, 22, 22, 23, 23, 23},
            {23, 23, 23, 23, 22, 23, 23, 23, 22, 22, 23, 23, 22, 22, 22, 23,
                    22, 22, 22, 22, 21, 22, 22, 22, 21, 21, 22, 22, 22},
            {22, 22, 23, 23, 22, 22, 22, 23, 22, 22, 22, 22, 21, 22, 22, 22,
                    21, 21, 22, 22, 21, 21, 21, 22, 21, 21, 21, 21, 22}};
    private static char[][] principleTermYear = {
            {13, 45, 81, 113, 149, 185, 201},
            {21, 57, 93, 125, 161, 193, 201},
            {21, 56, 88, 120, 152, 188, 200, 201},
            {21, 49, 81, 116, 144, 176, 200, 201},
            {17, 49, 77, 112, 140, 168, 200, 201},
            {28, 60, 88, 116, 148, 180, 200, 201},
            {25, 53, 84, 112, 144, 172, 200, 201},
            {29, 57, 89, 120, 148, 180, 200, 201},
            {17, 45, 73, 108, 140, 168, 200, 201},
            {28, 60, 92, 124, 160, 192, 200, 201},
            {16, 44, 80, 112, 148, 180, 200, 201},
            {17, 53, 88, 120, 156, 188, 200, 201}};

    private static char[] chineseMonths = {
            // 农历月份大小压缩表，两个字节表示一年。两个字节共十六个二进制位数，
            // 前四个位数表示闰月月份，后十二个位数表示十二个农历月份的大小。
            0x00, 0x04, 0xad, 0x08, 0x5a, 0x01, 0xd5, 0x54, 0xb4, 0x09, 0x64,
            0x05, 0x59, 0x45, 0x95, 0x0a, 0xa6, 0x04, 0x55, 0x24, 0xad, 0x08,
            0x5a, 0x62, 0xda, 0x04, 0xb4, 0x05, 0xb4, 0x55, 0x52, 0x0d, 0x94,
            0x0a, 0x4a, 0x2a, 0x56, 0x02, 0x6d, 0x71, 0x6d, 0x01, 0xda, 0x02,
            0xd2, 0x52, 0xa9, 0x05, 0x49, 0x0d, 0x2a, 0x45, 0x2b, 0x09, 0x56,
            0x01, 0xb5, 0x20, 0x6d, 0x01, 0x59, 0x69, 0xd4, 0x0a, 0xa8, 0x05,
            0xa9, 0x56, 0xa5, 0x04, 0x2b, 0x09, 0x9e, 0x38, 0xb6, 0x08, 0xec,
            0x74, 0x6c, 0x05, 0xd4, 0x0a, 0xe4, 0x6a, 0x52, 0x05, 0x95, 0x0a,
            0x5a, 0x42, 0x5b, 0x04, 0xb6, 0x04, 0xb4, 0x22, 0x6a, 0x05, 0x52,
            0x75, 0xc9, 0x0a, 0x52, 0x05, 0x35, 0x55, 0x4d, 0x0a, 0x5a, 0x02,
            0x5d, 0x31, 0xb5, 0x02, 0x6a, 0x8a, 0x68, 0x05, 0xa9, 0x0a, 0x8a,
            0x6a, 0x2a, 0x05, 0x2d, 0x09, 0xaa, 0x48, 0x5a, 0x01, 0xb5, 0x09,
            0xb0, 0x39, 0x64, 0x05, 0x25, 0x75, 0x95, 0x0a, 0x96, 0x04, 0x4d,
            0x54, 0xad, 0x04, 0xda, 0x04, 0xd4, 0x44, 0xb4, 0x05, 0x54, 0x85,
            0x52, 0x0d, 0x92, 0x0a, 0x56, 0x6a, 0x56, 0x02, 0x6d, 0x02, 0x6a,
            0x41, 0xda, 0x02, 0xb2, 0xa1, 0xa9, 0x05, 0x49, 0x0d, 0x0a, 0x6d,
            0x2a, 0x09, 0x56, 0x01, 0xad, 0x50, 0x6d, 0x01, 0xd9, 0x02, 0xd1,
            0x3a, 0xa8, 0x05, 0x29, 0x85, 0xa5, 0x0c, 0x2a, 0x09, 0x96, 0x54,
            0xb6, 0x08, 0x6c, 0x09, 0x64, 0x45, 0xd4, 0x0a, 0xa4, 0x05, 0x51,
            0x25, 0x95, 0x0a, 0x2a, 0x72, 0x5b, 0x04, 0xb6, 0x04, 0xac, 0x52,
            0x6a, 0x05, 0xd2, 0x0a, 0xa2, 0x4a, 0x4a, 0x05, 0x55, 0x94, 0x2d,
            0x0a, 0x5a, 0x02, 0x75, 0x61, 0xb5, 0x02, 0x6a, 0x03, 0x61, 0x45,
            0xa9, 0x0a, 0x4a, 0x05, 0x25, 0x25, 0x2d, 0x09, 0x9a, 0x68, 0xda,
            0x08, 0xb4, 0x09, 0xa8, 0x59, 0x54, 0x03, 0xa5, 0x0a, 0x91, 0x3a,
            0x96, 0x04, 0xad, 0xb0, 0xad, 0x04, 0xda, 0x04, 0xf4, 0x62, 0xb4,
            0x05, 0x54, 0x0b, 0x44, 0x5d, 0x52, 0x0a, 0x95, 0x04, 0x55, 0x22,
            0x6d, 0x02, 0x5a, 0x71, 0xda, 0x02, 0xaa, 0x05, 0xb2, 0x55, 0x49,
            0x0b, 0x4a, 0x0a, 0x2d, 0x39, 0x36, 0x01, 0x6d, 0x80, 0x6d, 0x01,
            0xd9, 0x02, 0xe9, 0x6a, 0xa8, 0x05, 0x29, 0x0b, 0x9a, 0x4c, 0xaa,
            0x08, 0xb6, 0x08, 0xb4, 0x38, 0x6c, 0x09, 0x54, 0x75, 0xd4, 0x0a,
            0xa4, 0x05, 0x45, 0x55, 0x95, 0x0a, 0x9a, 0x04, 0x55, 0x44, 0xb5,
            0x04, 0x6a, 0x82, 0x6a, 0x05, 0xd2, 0x0a, 0x92, 0x6a, 0x4a, 0x05,
            0x55, 0x0a, 0x2a, 0x4a, 0x5a, 0x02, 0xb5, 0x02, 0xb2, 0x31, 0x69,
            0x03, 0x31, 0x73, 0xa9, 0x0a, 0x4a, 0x05, 0x2d, 0x55, 0x2d, 0x09,
            0x5a, 0x01, 0xd5, 0x48, 0xb4, 0x09, 0x68, 0x89, 0x54, 0x0b, 0xa4,
            0x0a, 0xa5, 0x6a, 0x95, 0x04, 0xad, 0x08, 0x6a, 0x44, 0xda, 0x04,
            0x74, 0x05, 0xb0, 0x25, 0x54, 0x03};

    /**
     * 用于保存24节气
     */
    private static String[] principleTermNames = {"大寒", "雨水", "春分", "谷雨",
            "小满", "夏至", "大暑", "处暑", "秋分", "霜降", "小雪", "冬至"};
    /**
     * 用于保存24节气
     */
    private static String[] sectionalTermNames = {"小寒", "立春", "惊蛰", "清明",
            "立夏", "芒种", "小暑", "立秋", "白露", "寒露", "立冬", "大雪"};

    public SolarTermsUtil(Calendar calendar) {
        gregorianYear = calendar.get(Calendar.YEAR);
        gregorianMonth = calendar.get(Calendar.MONTH) + 1;
        gregorianDate = calendar.get(Calendar.DATE);
        computeChineseFields();
        computeSolarTerms();
    }

    public int computeChineseFields() {
        if (gregorianYear < 1901 || gregorianYear > 2100)
            return 1;
        int startYear = baseYear;
        int startMonth = baseMonth;
        int startDate = baseDate;
        chineseYear = baseChineseYear;
        chineseMonth = baseChineseMonth;
        chineseDate = baseChineseDate;
        // 第二个对应日，用以提高计算效率
        // 公历 2000 年 1 月 1 日，对应农历 4697 年 11 月 25 日
        if (gregorianYear >= 2000) {
            startYear = baseYear + 99;
            startMonth = 1;
            startDate = 1;
            chineseYear = baseChineseYear + 99;
            chineseMonth = 11;
            chineseDate = 25;
        }
        int daysDiff = 0;
        for (int i = startYear; i < gregorianYear; i++) {
            daysDiff += 365;
            if (isGregorianLeapYear(i))
                daysDiff += 1; // leap year
        }
        for (int i = startMonth; i < gregorianMonth; i++) {
            daysDiff += daysInGregorianMonth(gregorianYear, i);
        }
        daysDiff += gregorianDate - startDate;

        chineseDate += daysDiff;
        int lastDate = daysInChineseMonth(chineseYear, chineseMonth);
        int nextMonth = nextChineseMonth(chineseYear, chineseMonth);
        while (chineseDate > lastDate) {
            if (Math.abs(nextMonth) < Math.abs(chineseMonth))
                chineseYear++;
            chineseMonth = nextMonth;
            chineseDate -= lastDate;
            lastDate = daysInChineseMonth(chineseYear, chineseMonth);
            nextMonth = nextChineseMonth(chineseYear, chineseMonth);
        }
        return 0;
    }

    public int computeSolarTerms() {
        if (gregorianYear < 1901 || gregorianYear > 2100)
            return 1;
        sectionalTerm = sectionalTerm(gregorianYear, gregorianMonth);
        principleTerm = principleTerm(gregorianYear, gregorianMonth);
        return 0;
    }

    public static int sectionalTerm(int y, int m) {
        if (y < 1901 || y > 2100)
            return 0;
        int index = 0;
        int ry = y - baseYear + 1;
        while (ry >= sectionalTermYear[m - 1][index])
            index++;
        int term = sectionalTermMap[m - 1][4 * index + ry % 4];
        if ((ry == 121) && (m == 4))
            term = 5;
        if ((ry == 132) && (m == 4))
            term = 5;
        if ((ry == 194) && (m == 6))
            term = 6;
        return term;
    }

    public static int principleTerm(int y, int m) {
        if (y < 1901 || y > 2100)
            return 0;
        int index = 0;
        int ry = y - baseYear + 1;
        while (ry >= principleTermYear[m - 1][index])
            index++;
        int term = principleTermMap[m - 1][4 * index + ry % 4];
        if ((ry == 171) && (m == 3))
            term = 21;
        if ((ry == 181) && (m == 5))
            term = 21;
        return term;
    }

    /**
     * 用于判断输入的年份是否为闰年
     *
     * @param year 输入的年份
     * @return true 表示闰年
     */
    public static boolean isGregorianLeapYear(int year) {
        boolean isLeap = false;
        if (year % 4 == 0)
            isLeap = true;
        if (year % 100 == 0)
            isLeap = false;
        if (year % 400 == 0)
            isLeap = true;
        return isLeap;
    }

    public static int daysInGregorianMonth(int y, int m) {
        int d = daysInGregorianMonth[m - 1];
        if (m == 2 && isGregorianLeapYear(y))
            d++; // 公历闰年二月多一天
        return d;
    }

    public static int daysInChineseMonth(int y, int m) {
        // 注意：闰月 m < 0
        int index = y - baseChineseYear + baseIndex;
        int v = 0;
        int l = 0;
        int d = 30;
        if (1 <= m && m <= 8) {
            v = chineseMonths[2 * index];
            l = m - 1;
            if (((v >> l) & 0x01) == 1)
                d = 29;
        } else if (9 <= m && m <= 12) {
            v = chineseMonths[2 * index + 1];
            l = m - 9;
            if (((v >> l) & 0x01) == 1)
                d = 29;
        } else {
            v = chineseMonths[2 * index + 1];
            v = (v >> 4) & 0x0F;
            if (v != Math.abs(m)) {
                d = 0;
            } else {
                d = 29;
                for (int i = 0; i < bigLeapMonthYears.length; i++) {
                    if (bigLeapMonthYears[i] == index) {
                        d = 30;
                        break;
                    }
                }
            }
        }
        return d;
    }

    public static int nextChineseMonth(int y, int m) {
        int n = Math.abs(m) + 1;
        if (m > 0) {
            int index = y - baseChineseYear + baseIndex;
            int v = chineseMonths[2 * index + 1];
            v = (v >> 4) & 0x0F;
            if (v == m)
                n = -m;
        }
        if (n == 13)
            n = 1;
        return n;
    }

    // 大闰月的闰年年份
    private static int[] bigLeapMonthYears = {6, 14, 19, 25, 33, 36, 38, 41,
            44, 52, 55, 79, 117, 136, 147, 150, 155, 158, 185, 193};

    /**
     * 用于获取24节气的值
     *
     * @return 24节气的值
     */
    public String getSolartermsMsg() {
        String str = "";
        String gm = String.valueOf(gregorianMonth);
        if (gm.length() == 1)
            gm = ' ' + gm;
        String cm = String.valueOf(Math.abs(chineseMonth));
        if (cm.length() == 1)
            cm = ' ' + cm;
        String gd = String.valueOf(gregorianDate);
        if (gd.length() == 1)
            gd = ' ' + gd;
        String cd = String.valueOf(chineseDate);
        if (cd.length() == 1)
            cd = ' ' + cd;
        if (gregorianDate == sectionalTerm) {
            str = " " + sectionalTermNames[gregorianMonth - 1];
        } else if (gregorianDate == principleTerm) {
            str = " " + principleTermNames[gregorianMonth - 1];
        }
        return str;
    }
}

/**
 * 对公历日期的处理类
 */
class GregorianUtil {
    private final static String[][] GRE_FESTVIAL = {
            // 一月
            {"元旦", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
            // 二月
            {"", "", "", "", "", "", "", "", "", "", "", "", "", "情人", "", "",
                    "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
            // 三月
            {"", "", "", "", "", "", "", "妇女", "", "", "", "植树", "", "", "",
                    "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                    ""},
            // 四月
            {"愚人", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
            // 五月
            {"劳动", "", "", "青年", "", "", "", "", "", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                    ""},
            // 六月
            {"儿童", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
            // 七月
            {"建党", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
            // 八月
            {"建军", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
            // 九月
            {"", "", "", "", "", "", "", "", "", "教师", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
            // 十月
            {"国庆", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
            // 十一月
            {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
            // 十二月
            {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", "", "", "圣诞", "", "", "", "",
                    "", ""},};
    private int mMonth;
    private int mDay;

    public GregorianUtil(Calendar calendar) {
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DATE);
    }

    public String getGremessage() {
        return GRE_FESTVIAL[mMonth][mDay - 1];
    }
}

class NumberHelper {
    public static String LeftPad_Tow_Zero(int str) {
        java.text.DecimalFormat format = new java.text.DecimalFormat("00");
        return format.format(str);
    }
}
