package com.example.chen.tset.page.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chen.tset.Data.entity.Pharmacyremind;
import com.example.chen.tset.R;
import com.example.chen.tset.View.activity.CompilePharmacyRemindActivity;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.pharmacy_remind_lv_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.tv_startendtime.setText(list.get(position).getStartdate() + "-" + list.get(position).getOverdate());
        viewHolder.tv_amtime.setText(list.get(position).getTime1());
        viewHolder.tv_amcontent.setText(list.get(position).getContent1());

        if (list.get(position).getTime2().equals("")) {
            viewHolder.linearlayout1.setVisibility(View.GONE);
        } else {
            viewHolder.tv_pmtime.setText(list.get(position).getTime2());
            viewHolder.tv_pmcontent.setText(list.get(position).getContent2());
        }

        if (list.get(position).getTime3().equals("")) {
            viewHolder.linearlayout2.setVisibility(View.GONE);
        } else {
            viewHolder.tv_nighttime.setText(list.get(position).getTime3());
            viewHolder.tv_nightcontent.setText(list.get(position).getContent3());
        }

        viewHolder.ll_compile_pharmacy_remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CompilePharmacyRemindActivity.class);
                intent.putExtra("startdate",list.get(position).getStartdate());
                intent.putExtra("overdate",list.get(position).getOverdate());

                intent.putExtra("time1",list.get(position).getTime1());
                intent.putExtra("content1",list.get(position).getContent1());

                intent.putExtra("time2",list.get(position).getTime2());
                intent.putExtra("content2",list.get(position).getContent2());

                intent.putExtra("time3",list.get(position).getTime3());
                intent.putExtra("content3",list.get(position).getContent3());
                context.startActivity(intent);
            }
        });


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
        private LinearLayout linearlayout1;
        private LinearLayout linearlayout2;
        private LinearLayout ll_compile_pharmacy_remind;

        ViewHolder(View v) {
            tv_amcontent = (TextView) v.findViewById(R.id.tv_amcontent);
            tv_nighttime = (TextView) v.findViewById(R.id.tv_nighttime);
            tv_pmcontent = (TextView) v.findViewById(R.id.tv_pmcontent);
            tv_startendtime = (TextView) v.findViewById(R.id.tv_startendtime);
            tv_nightcontent = (TextView) v.findViewById(R.id.tv_nightcontent);
            tv_pmtime = (TextView) v.findViewById(R.id.tv_pmtime);
            tv_amtime = (TextView) v.findViewById(R.id.tv_amtime);
            linearlayout1 = (LinearLayout) v.findViewById(R.id.linearlayout1);
            linearlayout2 = (LinearLayout) v.findViewById(R.id.linearlayout2);
            ll_compile_pharmacy_remind= (LinearLayout) v.findViewById(R.id.ll_compile_pharmacy_remind);
        }
    }
}
