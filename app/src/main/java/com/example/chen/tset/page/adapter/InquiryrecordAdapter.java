package com.example.chen.tset.page.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chen.tset.Data.entity.Inquiryrecord;
import com.example.chen.tset.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/9/6 0006.
 */
public class InquiryrecordAdapter extends BaseAdapter {
    private Context context;
    private List<Inquiryrecord> list;

    public InquiryrecordAdapter(Context context, List<Inquiryrecord> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Inquiryrecord getItem(int position) {
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
            convertView = inflater.inflate(R.layout.inquiryrecord_listv_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.tv_doctor_name.setText(list.get(position).getDoctorname());
        viewHolder.tv_time.setText(list.get(position).getTime());
        viewHolder.tv_statu.setText(list.get(position).getState());
        ImageLoader.getInstance().displayImage(list.get(position).getDoctoricon(), viewHolder.iv_dicon);
        return convertView;
    }

    static class ViewHolder {
        private CircleImageView iv_dicon;
        private TextView tv_doctor_name, tv_statu, tv_time;

        ViewHolder(View v) {
            tv_doctor_name = (TextView) v.findViewById(R.id.tv_doctor_name);
            tv_statu = (TextView) v.findViewById(R.id.tv_statu);
            tv_time = (TextView) v.findViewById(R.id.tv_time);
            iv_dicon = (CircleImageView) v.findViewById(R.id.iv_dicon);
        }
    }
}
