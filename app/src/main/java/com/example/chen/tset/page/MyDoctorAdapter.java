package com.example.chen.tset.page;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.chen.tset.Data.Inquiry;
import com.example.chen.tset.Data.MyDoctor;
import com.example.chen.tset.R;
import com.example.chen.tset.View.ChatpageActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/9/27 0027.
 */
public class MyDoctorAdapter extends BaseAdapter {
    private Context context;
    private List<MyDoctor> list;

    public MyDoctorAdapter(Context context, List<MyDoctor> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MyDoctor getItem(int position) {
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
            convertView = inflater.inflate(R.layout.inquiry_list_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        final ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.textView.setText(list.get(position).getDoctorTitle());
        viewHolder.tv_name.setText(list.get(position).getDoctorName());
        viewHolder.btn_money.setText("￥" + list.get(position).getChatCost());
        viewHolder.tv_intro.setText("擅长：" + list.get(position).getAdept());
        viewHolder.tv_section.setText(list.get(position).getDoctorSection());
        ImageLoader.getInstance().displayImage(list.get(position).getDoctorIcon(), viewHolder.iv_icon);

        //跳转到聊天页面
        viewHolder.fl_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatpageActivity.class);
                intent.putExtra("name", list.get(position).getDoctorName());
                intent.putExtra("icon", list.get(position).getDoctorIcon());
                intent.putExtra("doctorID", list.get(position).getDoctorId() + "");
                intent.putExtra("username", list.get(position).getDoctorUserName());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        private TextView textView;
        private CircleImageView iv_icon;
        private TextView tv_title;
        private TextView tv_name;
        private Button btn_money;
        private TextView tv_intro;
        private TextView tv_section;
        private FrameLayout fl_chat;

        ViewHolder(View v) {
            textView = (TextView) v.findViewById(R.id.textView);
            iv_icon = (CircleImageView) v.findViewById(R.id.iv_icon);
            tv_title = (TextView) v.findViewById(R.id.tv_title);
            tv_name = (TextView) v.findViewById(R.id.tv_name);
            btn_money = (Button) v.findViewById(R.id.btn_money);
            tv_intro = (TextView) v.findViewById(R.id.tv_intro);
            tv_section = (TextView) v.findViewById(R.id.tv_section);
            fl_chat = (FrameLayout) v.findViewById(R.id.fl_chat);
        }
    }
}
