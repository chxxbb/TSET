package com.example.chen.tset.page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chen.tset.Data.Pharmacyremind;
import com.example.chen.tset.R;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5 0005.
 */
public class PharmacyremindAdapter extends BaseAdapter {
    private Context context;
    private List<Pharmacyremind> list;

    public PharmacyremindAdapter(Context context, List<Pharmacyremind> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Pharmacyremind getItem(int position) {
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
            convertView = inflater.inflate(R.layout.pharmacy_remind_lv_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.tv_amcontent.setText(list.get(position).getAmcontent());
        viewHolder.tv_nighttime.setText(list.get(position).getNighttime());
        viewHolder.tv_pmcontent.setText(list.get(position).getPmcontent());
        viewHolder.tv_startendtime.setText(list.get(position).getStartendtime());
        viewHolder.tv_nightcontent.setText(list.get(position).getNightcontent());
        viewHolder.tv_pmtime.setText(list.get(position).getPmtime());
        viewHolder.tv_amtime.setText(list.get(position).getAmtime());
        return convertView;
    }

    static class ViewHolder {
        private TextView tv_amcontent;
        private TextView tv_nighttime;
        private TextView tv_pmcontent;
        private TextView tv_startendtime;
        private TextView tv_nightcontent;
        private TextView tv_pmtime;
        private TextView tv_amtime;

        ViewHolder(View v) {
            tv_amcontent = (TextView) v.findViewById(R.id.tv_amcontent);
            tv_nighttime = (TextView) v.findViewById(R.id.tv_nighttime);
            tv_pmcontent = (TextView) v.findViewById(R.id.tv_pmcontent);
            tv_startendtime = (TextView) v.findViewById(R.id.tv_startendtime);
            tv_nightcontent = (TextView) v.findViewById(R.id.tv_nightcontent);
            tv_pmtime = (TextView) v.findViewById(R.id.tv_pmtime);
            tv_amtime = (TextView) v.findViewById(R.id.tv_amtime);
        }
    }
}
