package com.example.chen.tset.page.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chen.tset.Data.entity.ConsultingRemindState;
import com.example.chen.tset.Data.entity.PharmacyState;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.SpecialCalendar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 日历gridview中的每一个item显示的textview
 *
 * @author Vincent Lee
 */
public class CalendarAdapter extends BaseAdapter {
    private boolean isLeapyear = false; // 是否为闰年
    private int daysOfMonth = 0; // 某月的天数
    private int dayOfWeek = 0; // 具体某一天是星期几
    private int lastDaysOfMonth = 0; // 上一个月的总天数
    private Context context;
    private String[] dayNumber = new String[42]; // 一个gridview中的日期存入此数组中
    private SpecialCalendar sc = null;
    private Resources res = null;
    private Drawable drawable = null;

    private String currentYear = "";
    private String currentMonth = "";
    private String currentDay = "";

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    private int currentFlag = -1; // 用于标记当天


    private String showYear = ""; // 用于在头部显示的年份
    private String showMonth = ""; // 用于在头部显示的月份
    private String animalsYear = "";
    private String leapMonth = ""; // 闰哪一个月

    // 系统当前时间
    private String sysDate = "";
    private String sys_year = "";
    private String sys_month = "";
    private String sys_day = "";

    List<PharmacyState> list;

    List<ConsultingRemindState> data;

    String scheduleDay;


    //用药提醒设置
    int pharmacy = 0;

    public void pharmacyremind(int position) {
        if (position != pharmacy) {
            pharmacy = position;
            notifyDataSetChanged();
        }
    }

    //挂号提醒
    int registration = 0;

    public void registrationremind(int position) {
        if (position != registration) {
            registration = position;
            notifyDataSetChanged();
        }
    }

    //健康提醒
    int health = 0;

    public void healthremind(int position) {
        if (position != health) {
            health = position;
            notifyDataSetChanged();
        }
    }


    public CalendarAdapter() {
        Date date = new Date();
        sysDate = sdf.format(date); // 当期日期
        sys_year = sysDate.split("-")[0];
        sys_month = sysDate.split("-")[1];
        sys_day = sysDate.split("-")[2];

    }


    int mSelect;

    //点击改变背景
    public void changeSelected(int positon) { //刷新方法
        if (positon != mSelect) {
            mSelect = positon;
            notifyDataSetChanged();
        }

    }

    public CalendarAdapter(Context context, Resources rs, int jumpMonth, int jumpYear, int year_c, int month_c, int day_c, List<ConsultingRemindState> data) {
        this();
        this.context = context;
        sc = new SpecialCalendar();
        this.res = rs;
        this.data = data;

        int stepYear = year_c + jumpYear;
        int stepMonth = month_c + jumpMonth;
        if (stepMonth > 0) {
            // 往下一个月滑动
            if (stepMonth % 12 == 0) {
                stepYear = year_c + stepMonth / 12 - 1;
                stepMonth = 12;
            } else {
                stepYear = year_c + stepMonth / 12;
                stepMonth = stepMonth % 12;
            }
        } else {
            // 往上一个月滑动
            stepYear = year_c - 1 + stepMonth / 12;
            stepMonth = stepMonth % 12 + 12;
            if (stepMonth % 12 == 0) {

            }
        }

        currentYear = String.valueOf(stepYear); // 得到当前的年份
        currentMonth = String.valueOf(stepMonth); // 得到本月
        // （jumpMonth为滑动的次数，每滑动一次就增加一月或减一月）
        currentDay = String.valueOf(day_c); // 得到当前日期是哪天

        getCalendar(Integer.parseInt(currentYear), Integer.parseInt(currentMonth));

    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dayNumber.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.calendar_item, null);
        }
        final TextView textView = (TextView) convertView.findViewById(R.id.tvtext);
        final LinearLayout ll_calendar = (LinearLayout) convertView.findViewById(R.id.ll_calendar);
        String d = dayNumber[position];

        //设置显示的日期
        textView.setText(d);

        if (position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
//            // 当前月信息显示

        } else {
            ll_calendar.setVisibility(View.GONE);
        }

        if (currentFlag == position) {
            // 设置当天的背景
            textView.setText("今");

        }

        if (getDateByClickItem(position).equals("1") || getDateByClickItem(position).equals("2") || getDateByClickItem(position).equals("3") || getDateByClickItem(position).equals("4") || getDateByClickItem(position).equals("5") || getDateByClickItem(position).equals("6") || getDateByClickItem(position).equals("7") || getDateByClickItem(position).equals("8") || getDateByClickItem(position).equals("9")) {
            scheduleDay = "0" + getDateByClickItem(position); // 这一天的日期
        } else {
            scheduleDay = getDateByClickItem(position);
        }

        String scheduleYear = getShowYear();
        String scheduleMonth = getShowMonth();


        //点击改变背景
        if (mSelect == position) {

            textView.setBackgroundResource(R.drawable.cousulting_checked);


        } else {

            textView.setBackgroundColor(android.graphics.Color.parseColor("#ffffff"));


        }

        // 全部未被选中

        if (pharmacy == 2 && health == 2 && registration == 2) {


        }

        //只有用药提醒被选中
        else if (pharmacy == 1 && registration == 2 && health == 2) {
            for (int i = 0; i < data.size(); i++) {
                if ((scheduleYear + "-" + scheduleMonth + "-" + scheduleDay).equals(data.get(i).getDate()) && position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
                    if (data.get(i).getRemindId() != 0) {
                        if (mSelect == position) {
                            textView.setBackgroundResource(R.drawable.consulting_onclick9);
                        } else {
                            textView.setBackgroundResource(R.drawable.consulting9);
                        }
                    }
                }
            }
        }
        //只有健康提醒被选中
        else if (pharmacy == 2 && registration == 2 && health == 1) {
            for (int i = 0; i < data.size(); i++) {
                if ((scheduleYear + "-" + scheduleMonth + "-" + scheduleDay).equals(data.get(i).getDate()) && position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
                    if (data.get(i).getHealthId() != 0) {

                        if (mSelect == position) {
                            textView.setBackgroundResource(R.drawable.consulting_onclick7);
                        } else {
                            textView.setBackgroundResource(R.drawable.consulting7);
                        }

                    }
                }

            }
        }
        //只有挂号提醒被选中
        else if (pharmacy == 2 && registration == 1 && health == 2) {
            for (int i = 0; i < data.size(); i++) {
                if ((scheduleYear + "-" + scheduleMonth + "-" + scheduleDay).equals(data.get(i).getDate()) && position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
                    if (data.get(i).getRegistrationId() != 0) {

                        if (mSelect == position) {
                            textView.setBackgroundResource(R.drawable.consulting_onclick6);
                        } else {
                            textView.setBackgroundResource(R.drawable.consulting6);
                        }

                    }
                }

            }
        }

        //挂号，健康都被选中
        else if (pharmacy == 2 && registration == 1 && health == 1) {
            for (int i = 0; i < data.size(); i++) {
                if ((scheduleYear + "-" + scheduleMonth + "-" + scheduleDay).equals(data.get(i).getDate()) && position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
                    if (data.get(i).getRegistrationId() != 0) {

                        if (mSelect == position) {
                            textView.setBackgroundResource(R.drawable.consulting_onclick6);
                        } else {
                            textView.setBackgroundResource(R.drawable.consulting6);
                        }

                    } else if (data.get(i).getHealthId() != 0 && data.get(i).getRegistrationId() == 0) {
                        if (mSelect == position) {
                            textView.setBackgroundResource(R.drawable.consulting_onclick7);
                        } else {
                            textView.setBackgroundResource(R.drawable.consulting7);
                        }
                    }
                }

            }
        }
        //挂号，用药都被选中
        else if (pharmacy == 1 && registration == 1 && health == 2) {
            for (int i = 0; i < data.size(); i++) {
                if ((scheduleYear + "-" + scheduleMonth + "-" + scheduleDay).equals(data.get(i).getDate()) && position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
                    if ((data.get(i).getRemindId() != 0 && data.get(i).getRegistrationId() == 0 && data.get(i).getHealthId() == 0) || (data.get(i).getHealthId() != 0 && data.get(i).getRegistrationId() == 0 && data.get(i).getRemindId() != 0)) {
                        if (mSelect == position) {
                            textView.setBackgroundResource(R.drawable.consulting_onclick9);
                        } else {
                            textView.setBackgroundResource(R.drawable.consulting9);
                        }
                    } else if (data.get(i).getRegistrationId() != 0 && data.get(i).getRemindId() == 0) {
                        if (mSelect == position) {
                            textView.setBackgroundResource(R.drawable.consulting_onclick6);
                        } else {
                            textView.setBackgroundResource(R.drawable.consulting6);
                        }
                    } else if (data.get(i).getRegistrationId() != 0 && data.get(i).getRemindId() != 0) {
                        if (mSelect == position) {
                            textView.setBackgroundResource(R.drawable.consulting_onclick3);
                        } else {
                            textView.setBackgroundResource(R.drawable.consulting3);
                        }
                    }

                }
            }
        }

        //健康，用药都被选中
        else if (pharmacy == 1 && registration == 2 && health == 1) {
            for (int i = 0; i < data.size(); i++) {
                if ((scheduleYear + "-" + scheduleMonth + "-" + scheduleDay).equals(data.get(i).getDate()) && position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
                    if ((data.get(i).getRemindId() != 0 && data.get(i).getRegistrationId() == 0 && data.get(i).getHealthId() == 0) || (data.get(i).getRemindId() != 0 && data.get(i).getRegistrationId() != 0 && data.get(i).getHealthId() == 0)) {
                        if (mSelect == position) {
                            textView.setBackgroundResource(R.drawable.consulting_onclick9);
                        } else {
                            textView.setBackgroundResource(R.drawable.consulting9);
                        }
                    } else if (data.get(i).getHealthId() != 0 && data.get(i).getRemindId() == 0) {
                        if (mSelect == position) {
                            textView.setBackgroundResource(R.drawable.consulting_onclick7);
                        } else {
                            textView.setBackgroundResource(R.drawable.consulting7);
                        }
                    } else if (data.get(i).getHealthId() != 0 && data.get(i).getRemindId() != 0) {
                        if (mSelect == position) {
                            textView.setBackgroundResource(R.drawable.consulting_onclick8);
                        } else {
                            textView.setBackgroundResource(R.drawable.consulting8);
                        }
                    }
                }

            }
        }
        //全部都为选中状态
        else {

            for (int i = 0; i < data.size(); i++) {

                if ((scheduleYear + "-" + scheduleMonth + "-" + scheduleDay).equals(data.get(i).getDate()) && position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {

                    if ((data.get(i).getHealthId() != 0 && data.get(i).getRegistrationId() != 0 && data.get(i).getRemindId() != 0) || (data.get(i).getHealthId() == 0 && data.get(i).getRegistrationId() != 0 && data.get(i).getRemindId() != 0)) {
                        if (mSelect == position) {
                            textView.setBackgroundResource(R.drawable.consulting_onclick3);
                        } else {
                            textView.setBackgroundResource(R.drawable.consulting3);
                        }
                    } else if ((data.get(i).getRegistrationId() != 0 && data.get(i).getRemindId() == 0 && data.get(i).getHealthId() != 0) || (data.get(i).getRegistrationId() != 0 && data.get(i).getRemindId() == 0 && data.get(i).getHealthId() == 0)) {
                        if (mSelect == position) {
                            textView.setBackgroundResource(R.drawable.consulting_onclick6);
                        } else {
                            textView.setBackgroundResource(R.drawable.consulting6);
                        }
                    } else if (data.get(i).getHealthId() != 0 && data.get(i).getRemindId() == 0 && data.get(i).getRegistrationId() == 0) {
                        if (mSelect == position) {
                            textView.setBackgroundResource(R.drawable.consulting_onclick7);
                        } else {
                            textView.setBackgroundResource(R.drawable.consulting7);
                        }
                    } else if (data.get(i).getHealthId() != 0 && data.get(i).getRemindId() != 0 && data.get(i).getRegistrationId() == 0) {
                        if (mSelect == position) {
                            textView.setBackgroundResource(R.drawable.consulting_onclick8);
                        } else {
                            textView.setBackgroundResource(R.drawable.consulting8);
                        }
                    } else if (data.get(i).getRemindId() != 0 && data.get(i).getHealthId() == 0 && data.get(i).getRegistrationId() == 0) {
                        if (mSelect == position) {
                            textView.setBackgroundResource(R.drawable.consulting_onclick9);
                        } else {
                            textView.setBackgroundResource(R.drawable.consulting9);
                        }
                    }
                }
            }
        }

        if (position == 0) {
            textView.setBackgroundColor(android.graphics.Color.parseColor("#ffffff"));
        }

        return convertView;
    }

    // 得到某年的某月的天数且这月的第一天是星期几

    public void getCalendar(int year, int month) {
        isLeapyear = sc.isLeapYear(year); // 是否为闰年
        daysOfMonth = sc.getDaysOfMonth(isLeapyear, month); // 某月的总天数
        dayOfWeek = sc.getWeekdayOfMonth(year, month); // 某月第一天为星期几
        lastDaysOfMonth = sc.getDaysOfMonth(isLeapyear, month - 1); // 上一个月的总天数
        getweek(year, month);
    }

    // 将一个月中的每一天的值添加入数组dayNuber中
    private void getweek(int year, int month) {
        // 得到当前月的所有日程日期(这些日期需要标记)
        for (int i = 0; i < dayNumber.length; i++) {
            if (i < daysOfMonth + dayOfWeek) { // 本月
                String day = String.valueOf(i - dayOfWeek + 1); // 得到的日期
                dayNumber[i] = day;
                // 对于当前月才去标记当前日期
                if (sys_year.equals(String.valueOf(year)) && sys_month.equals(String.valueOf(month)) && sys_day.equals(day)) {
                    // 标记当前日期
                    currentFlag = i;
                }
                setShowYear(String.valueOf(year));
                setShowMonth(String.valueOf(month));
            } else { // 下一个月
                dayNumber[i] = "";
            }
        }
    }

    /**
     * 点击每一个item时返回item中的日期
     *
     * @param position
     * @return
     */
    public String getDateByClickItem(int position) {
        return dayNumber[position];
    }


    /**
     * 在点击gridView时，得到这个月中第一天的位置
     *
     * @return
     */
    public int getStartPositon() {
        return dayOfWeek + 7;
    }

    /**
     * 在点击gridView时，得到这个月中最后一天的位置
     *
     * @return
     */
    public int getEndPosition() {
        return (dayOfWeek + daysOfMonth + 7) - 1;
    }

    public String getShowYear() {
        return showYear;
    }

    public void setShowYear(String showYear) {
        this.showYear = showYear;
    }

    public String getShowMonth() {
        return showMonth;
    }

    public void setShowMonth(String showMonth) {
        this.showMonth = showMonth;
    }

    public String getAnimalsYear() {
        return animalsYear;
    }

    public void setAnimalsYear(String animalsYear) {
        this.animalsYear = animalsYear;
    }

    public String getLeapMonth() {
        return leapMonth;
    }

    public void setLeapMonth(String leapMonth) {
        this.leapMonth = leapMonth;
    }

}
