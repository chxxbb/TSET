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
    CharactersafeFragment charactersafeFragment;
    private TabLayout tabLayout;
    private ViewPager vp_encyclopedia;
    View view1,view2,view3,view4;


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
        vp_encyclopedia.addOnPageChangeListener(listener);
        view1=view.findViewById(R.id.view1);
        view2=view.findViewById(R.id.view2);
        view3=view.findViewById(R.id.view3);
        view4=view.findViewById(R.id.view4);
        view1.setVisibility(View.VISIBLE);
        view2.setVisibility(View.GONE);
        view3.setVisibility(View.GONE);
        view4.setVisibility(View.GONE);
    }

    private void init() {
        flist=new ArrayList<Fragment>();
        diseaselibFragment1=new DiseaselibFragment();
        flist.add(diseaselibFragment1);
        for(int i=0;i<3;i++){
            charactersafeFragment=new CharactersafeFragment();
            flist.add(charactersafeFragment);
        }
        adapter=new EncyclopediaAdapter(getChildFragmentManager(),flist);
        tabLayout.setTabsFromPagerAdapter(adapter);
        vp_encyclopedia.setAdapter(adapter);
        vp_encyclopedia.setOffscreenPageLimit(flist.size());
        tabLayout.setupWithViewPager(vp_encyclopedia);
        tabLayout.setTabTextColors(android.graphics.Color.parseColor("#999999"),android.graphics.Color.parseColor("#6fc9e6"));
        tabLayout.setSelectedTabIndicatorColor(android.graphics.Color.parseColor("#6fc9e6"));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }
    private ViewPager.OnPageChangeListener listener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if(position==0){
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);
            }else if(position==1){
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);
                view3.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);
            }else if(position==2){
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.VISIBLE);
                view4.setVisibility(View.GONE);
            }
            else if(position==3){
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                view4.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
