package com.example.chen.tset.page;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chen.tset.R;
import com.example.chen.tset.Utils.CalendarView;
import com.example.chen.tset.Utils.Consultinginit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public class ConsultingFragment extends Fragment {
    View view;
    private CalendarView calendarView;
    private RelativeLayout rl_loading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_consulting, null);
        findView();
        init();
        return view;
    }


    private void findView() {
        calendarView = (CalendarView) view.findViewById(R.id.calendar);
        rl_loading = (RelativeLayout) view.findViewById(R.id.rl_loading);


        //点击事件
        calendarView.setOnCalendarViewListener(new CalendarView.OnCalendarViewListener() {

            @Override
            public void onCalendarItemClick(CalendarView view, Date date) {
                // TODO Auto-generated method stub
                final SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);

                Toast.makeText(getActivity(), format.format(date), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void init() {
        List<Date> markDates = new ArrayList<Date>();
        markDates.add(new Date());
        calendarView.setMarkDates(markDates);
        Consultinginit.list.add("2016-09-22");
        Consultinginit.list.add("2016-09-25");
        Consultinginit.list.add("2016-09-28");
        Consultinginit.list.add("2016-08-28");
        Consultinginit.list.add("2016-10-28");
        Consultinginit.slist.add("1");
        Consultinginit.slist.add("2");
        Consultinginit.slist.add("3");
        Consultinginit.slist.add("3");
        Consultinginit.slist.add("3");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }).start();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    calendarView.setVisibility(View.VISIBLE);
                    rl_loading.setVisibility(View.GONE);
                    break;


            }
        }
    };


}
