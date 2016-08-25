package com.example.chen.tset;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class EncyclopediaFragment extends Fragment {
    View view;
    EncyclopediaAdapter adapter;
    List<Fragment> flist;
    //标题栏集合
    DiseaselibFragment diseaselibFragment1;
    DiseaselibFragment diseaselibFragment2;
    DiseaselibFragment diseaselibFragment3;
    DiseaselibFragment diseaselibFragment4;
    private TabLayout tabLayout;
    private ViewPager vp_encyclopedia;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_encyclopedia,null);
        findView();
        init();
        return view;
    }


    private void findView() {
        tabLayout= (TabLayout) view.findViewById(R.id.tabLayout);
        vp_encyclopedia= (ViewPager) view.findViewById(R.id.vp_encyclopedia);
    }

    private void init() {
        flist=new ArrayList<Fragment>();
        diseaselibFragment1=new DiseaselibFragment();
        diseaselibFragment2=new DiseaselibFragment();
        diseaselibFragment3=new DiseaselibFragment();
        diseaselibFragment4=new DiseaselibFragment();
        flist.add(diseaselibFragment1);
        flist.add(diseaselibFragment2);
        flist.add(diseaselibFragment3);
        flist.add(diseaselibFragment4);
        adapter=new EncyclopediaAdapter(getChildFragmentManager(),flist);
        tabLayout.setTabsFromPagerAdapter(adapter);
        vp_encyclopedia.setAdapter(adapter);
        vp_encyclopedia.setOffscreenPageLimit(flist.size());
        tabLayout.setupWithViewPager(vp_encyclopedia);
        tabLayout.setTabTextColors(android.graphics.Color.parseColor("#999999"),android.graphics.Color.parseColor("#6fc9e6"));
        tabLayout.setSelectedTabIndicatorColor(android.graphics.Color.parseColor("#6fc9e6"));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }
}
