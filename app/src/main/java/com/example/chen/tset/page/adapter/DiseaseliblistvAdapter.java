package com.example.chen.tset.page.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chen.tset.Data.entity.DiseaseDepartment;
import com.example.chen.tset.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class DiseaseliblistvAdapter extends BaseAdapter {
    private List<DiseaseDepartment> list;
    Context context;


    public DiseaseliblistvAdapter(Context context, List<DiseaseDepartment> list) {
        this.context = context;
        this.list = list;
    }

    int mSelect = 0;

    public void changeSelected(int positon) { //刷新方法
        if (positon != mSelect) {
            mSelect = positon;
            notifyDataSetChanged();
        }

    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public DiseaseDepartment getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.disease_listv_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.tv_dislistv.setText(list.get(position).getName());
        ImageLoader.getInstance().displayImage(list.get(position).getIcon1(), viewHolder.iv_dislistv);
        ImageLoader.getInstance().displayImage(list.get(position).getIcon2(), viewHolder.iv_dislistv1);
        //根据点击的条目，改变此条目的背景，及疾病图片
        if (mSelect == position) {
            viewHolder.iv_dislistv1.setVisibility(View.VISIBLE);
            viewHolder.iv_dislistv.setVisibility(View.GONE);
            viewHolder.dise_ll.setBackgroundColor(android.graphics.Color.parseColor("#6fc9e6"));
            viewHolder.tv_dislistv.setTextColor(0xffffffff);
        } else {
            viewHolder.iv_dislistv1.setVisibility(View.GONE);
            viewHolder.iv_dislistv.setVisibility(View.VISIBLE);
            viewHolder.dise_ll.setBackgroundColor(android.graphics.Color.parseColor("#f4f4f4"));
            viewHolder.tv_dislistv.setTextColor(android.graphics.Color.parseColor("#666666"));
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView tv_dislistv;
        private ImageView iv_dislistv, iv_dislistv1;
        private LinearLayout dise_ll;

        ViewHolder(View v) {
            tv_dislistv = (TextView) v.findViewById(R.id.tv_dislistv);
            iv_dislistv = (ImageView) v.findViewById(R.id.iv_dislistv);
            dise_ll = (LinearLayout) v.findViewById(R.id.dise_ll);
            iv_dislistv1 = (ImageView) v.findViewById(R.id.iv_dislistv1);
        }
    }


}
