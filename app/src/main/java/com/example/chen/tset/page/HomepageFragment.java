package com.example.chen.tset.page;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import android.widget.ScrollView;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.IListener;
import com.example.chen.tset.Utils.ListenerManager;
import com.example.chen.tset.View.HealthconditionActivity;
import com.example.chen.tset.View.InquiryActivity;
import com.example.chen.tset.View.LectureoomActivity;
import com.example.chen.tset.View.RegistrationAtivity;


import java.util.ArrayList;

import java.util.List;

/**
 * Created by Administrator on 2016/9/14 0014.
 * 首页
 */
public class HomepageFragment extends Fragment implements IListener {
    View view;
    private BannerView bannerView;
    private ScrollView scrollView;
    private LinearLayout ll_patriarch_lecture_room, ll_home_order_registration, ll_home_health_record, ll_diagnosistreat_manage, ll_home_article_more, ll_home_doctor_more;
    private ListViewForScrollView home_listView;
    HomeDoctorRecommendAdapter adapter;
    List<String> list;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homepage, null);

        //注册广播
        ListenerManager.getInstance().registerListtener(this);


        findView();

        init();
        return view;
    }


    private void findView() {
        bannerView = (BannerView) view.findViewById(R.id.bannerView);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);

        //家长讲堂
        ll_patriarch_lecture_room = (LinearLayout) view.findViewById(R.id.ll_patriarch_lecture_room);

        //预约挂号
        ll_home_order_registration = (LinearLayout) view.findViewById(R.id.ll_home_order_registration);

        //健康小鸡
        ll_home_health_record = (LinearLayout) view.findViewById(R.id.ll_home_health_record);

        //诊疗管理
        ll_diagnosistreat_manage = (LinearLayout) view.findViewById(R.id.ll_diagnosistreat_manage);

        //更多文章
        ll_home_article_more = (LinearLayout) view.findViewById(R.id.ll_home_article_more);

        //更多医生
        ll_home_doctor_more = (LinearLayout) view.findViewById(R.id.ll_home_doctor_more);

        home_listView = (ListViewForScrollView) view.findViewById(R.id.home_listView);


        //隐藏滚动条
        scrollView.setVerticalScrollBarEnabled(false);

        //使scrollView显示在头部，重写listview解决了scrollview与listview冲突，但会出现默认显示listview
        scrollView.smoothScrollTo(0, 0);


        ll_patriarch_lecture_room.setOnClickListener(listener);
        ll_home_order_registration.setOnClickListener(listener);
        ll_home_health_record.setOnClickListener(listener);
        ll_diagnosistreat_manage.setOnClickListener(listener);
        ll_home_doctor_more.setOnClickListener(listener);
        ll_home_article_more.setOnClickListener(listener);
    }


    private void init() {
        list = new ArrayList<>();
        list.add("姓名1");
        list.add("姓名2");
        adapter = new HomeDoctorRecommendAdapter(getContext(), list);
        home_listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //跳转至讲堂页面
                case R.id.ll_patriarch_lecture_room:
                    Intent intent = new Intent(getContext(), LectureoomActivity.class);
                    startActivity(intent);
                    break;
                //跳转至一键挂号页面
                case R.id.ll_home_order_registration:
                    Intent intent1 = new Intent(getContext(), RegistrationAtivity.class);
                    startActivity(intent1);
                    break;
                //跳转健康状况页面
                case R.id.ll_home_health_record:
                    Intent intent2 = new Intent(getContext(), HealthconditionActivity.class);
                    startActivity(intent2);
                    break;
                //跳转至名医库
                case R.id.ll_home_doctor_more:
                    Intent intent3 = new Intent(getContext(), InquiryActivity.class);
                    startActivity(intent3);
                    break;
                //诊疗页面
                case R.id.ll_diagnosistreat_manage:
                    //发送广播通知显示诊疗页面
                    ListenerManager.getInstance().sendBroadCast("显示诊疗页面");
                    break;
                //资讯页面
                case R.id.ll_home_article_more:
                    ListenerManager.getInstance().sendBroadCast("显示资讯页面");
                    Http_data.state=2;
                    break;
            }
        }
    };

    public void onResume() {
        super.onResume();
        bannerView.bannerStartPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
        bannerView.bannerStopPlay();
    }

    @Override
    public void notifyAllActivity(String str) {

    }
}
