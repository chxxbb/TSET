package com.example.chen.tset.page.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chen.tset.Data.entity.Reservation;
import com.example.chen.tset.R;

import java.util.List;

/**
 * Created by Administrator on 2016/8/27 0027.
 */
public class ReservationlistvAdapter extends BaseAdapter {
    private Context context;
    private List<Reservation> list;

    public ReservationlistvAdapter(Context context, List<Reservation> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Reservation getItem(int position) {
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
            convertView = inflater.inflate(R.layout.reservation_listv_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.tv_time.setText("有效期：" + list.get(position).getReservationDate());
        viewHolder.tv_order.setText("订单号：" + list.get(position).getOrderCode());
        viewHolder.tv_status.setText(list.get(position).getOrderStatus());
        viewHolder.tv_money.setText("￥" + list.get(position).getMoney());

        if (list.get(position).getOrderStatus().equals("已取消") || list.get(position).getOrderStatus().equals("已过期")) {
            viewHolder.tv_status.setTextColor(android.graphics.Color.parseColor("#999999"));
        } else {
            viewHolder.tv_status.setTextColor(android.graphics.Color.parseColor("#6fc9e6"));
        }

        return convertView;
    }

    static class ViewHolder {
        private TextView tv_time;
        private TextView tv_order;
        private TextView tv_status;
        private TextView tv_money;

        ViewHolder(View v) {
            tv_time = (TextView) v.findViewById(R.id.tv_time);
            tv_order = (TextView) v.findViewById(R.id.tv_order);
            tv_status = (TextView) v.findViewById(R.id.tv_status);
            tv_money = (TextView) v.findViewById(R.id.tv_money);

        }
    }

}
