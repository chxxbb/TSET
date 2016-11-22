package com.example.chen.tset.page.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.IListener;
import com.example.chen.tset.Utils.ListenerManager;
import com.example.chen.tset.page.adapter.EncyclopediaAdapter;

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
    private ViewPager vp_encyclopedia;
    View view1, view2;
    private RelativeLayout rl_tab1, rl_tab2;
    private TextView tv_tab1, tv_tab2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_encyclopedia, null);


        findView();
        init();


        return view;
    }


    private void findView() {

        vp_encyclopedia = (ViewPager) view.findViewById(R.id.vp_encyclopedia);
        vp_encyclopedia.addOnPageChangeListener(listener);
        view1 = view.findViewById(R.id.view1);
        view2 = view.findViewById(R.id.view2);

        rl_tab1 = (RelativeLayout) view.findViewById(R.id.rl_tab1);
        rl_tab2 = (RelativeLayout) view.findViewById(R.id.rl_tab2);

        tv_tab1 = (TextView) view.findViewById(R.id.tv_tab1);
        tv_tab2 = (TextView) view.findViewById(R.id.tv_tab2);

        tv_tab1.setTextColor(android.graphics.Color.parseColor("#6fc9e6"));
        tv_tab2.setTextColor(android.graphics.Color.parseColor("#999999"));

        rl_tab1.setOnClickListener(rllistener);
        rl_tab2.setOnClickListener(rllistener);

        view1.setVisibility(View.VISIBLE);
        view2.setVisibility(View.GONE);


    }

    private void init() {
        flist = new ArrayList<Fragment>();

        charactersafeFragment = new CharactersafeFragment();
        flist.add(charactersafeFragment);


        diseaselibFragment1 = new DiseaselibFragment();
        flist.add(diseaselibFragment1);


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

                tv_tab1.setTextColor(android.graphics.Color.parseColor("#6fc9e6"));
                tv_tab2.setTextColor(android.graphics.Color.parseColor("#999999"));


            } else if (position == 1) {
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);

                tv_tab2.setTextColor(android.graphics.Color.parseColor("#6fc9e6"));
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

            }
        }
    };



}
