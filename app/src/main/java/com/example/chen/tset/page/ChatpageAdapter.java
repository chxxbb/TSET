package com.example.chen.tset.page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chen.tset.R;

import java.util.List;

/**
 * Created by Administrator on 2016/9/14 0014.
 */
public class ChatpageAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;

    public ChatpageAdapter(Context context, List<String> list) {
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
            convertView = inflater.inflate(R.layout.chatpage_listv_leftitem, parent, false);
            convertView.setTag(new ViewHolder1(convertView));
        }
        ViewHolder1 viewHolder = (ViewHolder1) convertView.getTag();
//        if(list.get(position).substring(0,1).equals("1")){
//            viewHolder.ll_chat.setVisibility(View.GONE);
//            viewHolder.tv1.setText(list.get(position).substring(1));
//        }else {
//            viewHolder.rl_chat.setVisibility(View.GONE);
//            viewHolder.tv.setText(list.get(position).substring(1));
//        }
        viewHolder.tv.setText(list.get(position).substring(1));
        return convertView;
    }

    static class ViewHolder1 {
        private TextView tv;
        private TextView tv1;
        private LinearLayout ll_chat;
        private RelativeLayout rl_chat;

        ViewHolder1(View v) {
            tv = (TextView) v.findViewById(R.id.tv);
//            tv1= (TextView) v.findViewById(R.id.tv1);
//            ll_chat= (LinearLayout) v.findViewById(R.id.ll_chat);
//            rl_chat= (RelativeLayout) v.findViewById(R.id.rl_chat);
        }
    }
}
