package com.example.chen.tset.page.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class LeadAdapter extends PagerAdapter {
    private ArrayList<View> datas = new ArrayList<>();
    public void addim(View v){
        if(v!=null)
            datas.add(v);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = datas.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = datas.get(position);
        container.removeView(view);

    }
}
