package com.example.chen.tset.page.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

public class BannerViewAdapter extends PagerAdapter {
    private List<ImageView> list;
    private Context context;
    private List<String> data;

    public BannerViewAdapter(Context context, List<ImageView> list, List<String> data) {
        this.context = context;
        this.list = list;
        this.data = data;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        container.addView(list.get(position));
        list.get(position).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击banner打开浏览器
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(data.get(position));
                intent.setData(content_url);
                context.startActivity(intent);

            }
        });
        return list.get(position);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
