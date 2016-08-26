package com.example.chen.tset.page;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chen.tset.R;

import java.util.List;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class DiseaselibrecyvAdapter extends BaseAdapter {
    private final List<String> list;
    Context context;

    public DiseaselibrecyvAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
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
            convertView = inflater.inflate(R.layout.disease_recyv_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.dise_tv1.setText(list.get(position));
        viewHolder.dise_tv1.setText(list.get(position));
        return convertView;
    }

    static class ViewHolder {
        private TextView dise_tv1;
        private TextView dise_tv2;

        ViewHolder(View v) {
            dise_tv1 = (TextView) v.findViewById(R.id.dise_tv1);
            dise_tv2 = (TextView) v.findViewById(R.id.dise_tv2);
        }
    }

}
