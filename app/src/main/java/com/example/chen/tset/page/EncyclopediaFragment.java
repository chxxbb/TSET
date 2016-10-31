package com.example.chen.tset.page;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.IListener;
import com.example.chen.tset.Utils.ListenerManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class EncyclopediaFragment extends Fragment implements IListener {
    View view;
    EncyclopediaAdapter adapter;
    List<Fragment> flist;
    //标题栏集合
    DiseaselibFragment diseaselibFragment1;
    CharactersafeFragment charactersafeFragment;
    private ViewPager vp_encyclopedia;
    View view1, view2, view3, view4;
    private RelativeLayout rl_tab1, rl_tab2, rl_tab3, rl_tab4;
    private TextView tv_tab1, tv_tab2, tv_tab3, tv_tab4;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_encyclopedia, null);

        //注册广播
        ListenerManager.getInstance().registerListtener(this);
        findView();
        init();

        if (Http_data.state == 2) {
            //使viewpage显示在第二页
            vp_encyclopedia.setCurrentItem(1);
            Http_data.state = 1;
        }
        return view;
    }


    private void findView() {

        vp_encyclopedia = (ViewPager) view.findViewById(R.id.vp_encyclopedia);
        vp_encyclopedia.addOnPageChangeListener(listener);
        view1 = view.findViewById(R.id.view1);
        view2 = view.findViewById(R.id.view2);
        view3 = view.findViewById(R.id.view3);
        view4 = view.findViewById(R.id.view4);
        rl_tab1 = (RelativeLayout) view.findViewById(R.id.rl_tab1);
        rl_tab2 = (RelativeLayout) view.findViewById(R.id.rl_tab2);
        rl_tab3 = (RelativeLayout) view.findViewById(R.id.rl_tab3);
        rl_tab4 = (RelativeLayout) view.findViewById(R.id.rl_tab4);
        tv_tab1 = (TextView) view.findViewById(R.id.tv_tab1);
        tv_tab2 = (TextView) view.findViewById(R.id.tv_tab2);
        tv_tab3 = (TextView) view.findViewById(R.id.tv_tab3);
        tv_tab4 = (TextView) view.findViewById(R.id.tv_tab4);
        tv_tab1.setTextColor(android.graphics.Color.parseColor("#6fc9e6"));
        tv_tab2.setTextColor(android.graphics.Color.parseColor("#999999"));
        tv_tab3.setTextColor(android.graphics.Color.parseColor("#999999"));
        tv_tab4.setTextColor(android.graphics.Color.parseColor("#999999"));
        rl_tab1.setOnClickListener(rllistener);
        rl_tab2.setOnClickListener(rllistener);
        rl_tab3.setOnClickListener(rllistener);
        rl_tab4.setOnClickListener(rllistener);
        view1.setVisibility(View.VISIBLE);
        view2.setVisibility(View.GONE);
        view3.setVisibility(View.GONE);
        view4.setVisibility(View.GONE);

    }

    private void init() {
        flist = new ArrayList<Fragment>();
        diseaselibFragment1 = new DiseaselibFragment();
        flist.add(diseaselibFragment1);


        for (int i = 1; i < 4; i++) {
            charactersafeFragment = new CharactersafeFragment();
            charactersafeFragment.setI(i);
            flist.add(charactersafeFragment);
        }


        adapter = new EncyclopediaAdapter(getChildFragmentManager(), flist);
        vp_encyclopedia.setAdapter(adapter);
        vp_encyclopedia.setOffscreenPageLimit(flist.size());

    }

    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);
                tv_tab1.setTextColor(android.graphics.Color.parseColor("#6fc9e6"));
                tv_tab2.setTextColor(android.graphics.Color.parseColor("#999999"));
                tv_tab3.setTextColor(android.graphics.Color.parseColor("#999999"));
                tv_tab4.setTextColor(android.graphics.Color.parseColor("#999999"));

            } else if (position == 1) {
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);
                view3.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);
                tv_tab2.setTextColor(android.graphics.Color.parseColor("#6fc9e6"));
                tv_tab1.setTextColor(android.graphics.Color.parseColor("#999999"));
                tv_tab3.setTextColor(android.graphics.Color.parseColor("#999999"));
                tv_tab4.setTextColor(android.graphics.Color.parseColor("#999999"));
            } else if (position == 2) {
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.VISIBLE);
                view4.setVisibility(View.GONE);
                tv_tab3.setTextColor(android.graphics.Color.parseColor("#6fc9e6"));
                tv_tab2.setTextColor(android.graphics.Color.parseColor("#999999"));
                tv_tab1.setTextColor(android.graphics.Color.parseColor("#999999"));
                tv_tab4.setTextColor(android.graphics.Color.parseColor("#999999"));
            } else if (position == 3) {
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                view4.setVisibility(View.VISIBLE);
                tv_tab4.setTextColor(android.graphics.Color.parseColor("#6fc9e6"));
                tv_tab2.setTextColor(android.graphics.Color.parseColor("#999999"));
                tv_tab3.setTextColor(android.graphics.Color.parseColor("#999999"));
                tv_tab1.setTextColor(android.graphics.Color.parseColor("#999999"));
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {


        }
    };


    @Override
    public void onStart() {
        super.onStart();

    }

    private View.OnClickListener rllistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_tab1:
                    vp_encyclopedia.setCurrentItem(0, true);
                    break;
                case R.id.rl_tab2:
                    vp_encyclopedia.setCurrentItem(1, true);
                    break;
                case R.id.rl_tab3:
                    vp_encyclopedia.setCurrentItem(2, true);
                    break;
                case R.id.rl_tab4:
                    vp_encyclopedia.setCurrentItem(3, true);
                    break;
            }
        }
    };

    @Override
    public void notifyAllActivity(String str) {

        if (str.equals("显示资讯页面")) {
            //使viewpage显示在第二页
            vp_encyclopedia.setCurrentItem(1);

        }
    }


}
