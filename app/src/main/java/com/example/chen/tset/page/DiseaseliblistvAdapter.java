package com.example.chen.tset.page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chen.tset.Data.DiseaseDepartment;
import com.example.chen.tset.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class DiseaseliblistvAdapter extends BaseAdapter {
    private final List<DiseaseDepartment> list;
    Context context;
    int mSelect = 0;

    public DiseaseliblistvAdapter(Context context, List<DiseaseDepartment> list) {
        this.context = context;
        this.list = list;
    }

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
        ImageLoader.getInstance().displayImage(list.get(position).getIcon(), viewHolder.iv_dislistv);
        if (mSelect == position) {
            viewHolder.dise_ll.setBackgroundColor(android.graphics.Color.parseColor("#6fc9e6"));
            viewHolder.tv_dislistv.setTextColor(0xffffffff);
        } else {
            viewHolder.dise_ll.setBackgroundColor(android.graphics.Color.parseColor("#e0e0e0"));
            viewHolder.tv_dislistv.setTextColor(android.graphics.Color.parseColor("#323232"));
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView tv_dislistv;
        private ImageView iv_dislistv;
        private LinearLayout dise_ll;

        ViewHolder(View v) {
            tv_dislistv = (TextView) v.findViewById(R.id.tv_dislistv);
            iv_dislistv = (ImageView) v.findViewById(R.id.iv_dislistv);
            dise_ll = (LinearLayout) v.findViewById(R.id.dise_ll);
        }
    }

}
