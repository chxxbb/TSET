package com.example.chen.tset.page;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class EncyclopediaAdapter extends FragmentPagerAdapter{
    List<Fragment> flist;
    //Talout标题集合
    List<String> slist;
    public EncyclopediaAdapter(FragmentManager fm,List<Fragment> flist) {
        super(fm);
        this.flist=flist;
        slist=new ArrayList<String>();
        slist.add("疾病库");
        slist.add("品格与安全");
        slist.add("营养保健");
        slist.add("育儿知识");
    }

    @Override
    public Fragment getItem(int position) {
        return flist.get(position);
    }

    @Override
    public int getCount() {
        return flist.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return slist.get(position);
    }
}
