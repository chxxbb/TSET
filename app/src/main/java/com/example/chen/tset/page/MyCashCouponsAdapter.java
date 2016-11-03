package com.example.chen.tset.page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chen.tset.R;

import java.util.List;

/**
 * Created by Administrator on 2016/11/2 0002.
 */
public class MyCashCouponsAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;

    public MyCashCouponsAdapter(Context context, List<String> list) {
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
            convertView = inflater.inflate(R.layout.mycashcoupons_listview_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if (list.get(position).equals("")) {
            viewHolder.rl_mycash_use_background.setBackgroundResource(R.drawable.my_cashcoupon_haveaccessto_background);
            viewHolder.tv_mycash_use_stater.setText("");
        } else {
            viewHolder.rl_mycash_use_background.setBackgroundResource(R.drawable.my_cashcoupon_unavailable_background);
            viewHolder.tv_mycash_use_stater.setText("已使用");
        }

        if (position == 2) {
            viewHolder.tv_cash_coupons_type.setText("快速挂号劵");
        }

        return convertView;
    }

    static class ViewHolder {
        TextView tv_mycash_use_stater;
        RelativeLayout rl_mycash_use_background;
        //现金卷类型
        TextView tv_cash_coupons_type;

        ViewHolder(View v) {
            tv_mycash_use_stater = (TextView) v.findViewById(R.id.tv_mycash_use_stater);
            rl_mycash_use_background = (RelativeLayout) v.findViewById(R.id.rl_mycash_use_background);
            tv_cash_coupons_type = (TextView) v.findViewById(R.id.tv_cash_coupons_type);

        }
    }
}
