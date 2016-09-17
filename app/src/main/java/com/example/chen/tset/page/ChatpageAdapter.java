package com.example.chen.tset.page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chen.tset.Data.Chatcontent;
import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/9/14 0014.
 */
public class ChatpageAdapter extends BaseAdapter {
    private Context context;
    private List<Chatcontent> list;
    private String doctoricon;
    LayoutInflater inflater;
    private final int TYPE1 = 1;
    private final int TYPE2 = 2;

    public ChatpageAdapter(Context context, List<Chatcontent> list, String doctoricon) {
        this.context = context;
        this.list = list;
        this.doctoricon = doctoricon;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Chatcontent getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }


    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getContent().substring(0, 1).equals("1") && position != 0) {
            return TYPE1;
        } else {
            return TYPE2;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder1 viewHolder1 = null;
        ViewHolder2 viewHolder2 = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE1:
                    convertView = inflater.inflate(R.layout.chatpage_listv_rightitem, parent, false);
                    viewHolder1 = new ViewHolder1();
                    viewHolder1.tv_right_text = (TextView) convertView.findViewById(R.id.tv_right_text);
                    viewHolder1.iv_right_head = (CircleImageView) convertView.findViewById(R.id.iv_right_head);
                    convertView.setTag(viewHolder1);
                    break;
                case TYPE2:
                    convertView = inflater.inflate(R.layout.chatpage_listv_leftitem, parent, false);
                    viewHolder2 = new ViewHolder2();
                    viewHolder2.tv_left_text = (TextView) convertView.findViewById(R.id.tv_left_text);
                    viewHolder2.iv_left_icon = (CircleImageView) convertView.findViewById(R.id.iv_left_icon);
                    convertView.setTag(viewHolder2);
                    break;

            }
        } else {
            switch (type) {
                case TYPE1:
                    viewHolder1 = (ViewHolder1) convertView.getTag();
                    break;
                case TYPE2:
                    viewHolder2 = (ViewHolder2) convertView.getTag();
                    break;

            }
        }
        switch (type) {
            case TYPE1:
                viewHolder1.tv_right_text.setText(list.get(position).getContent().substring(1));
                ImageLoader.getInstance().displayImage(User_Http.user.getIcon(), viewHolder1.iv_right_head);
                break;
            case TYPE2:
                viewHolder2.tv_left_text.setText(list.get(position).getContent().substring(1));
                ImageLoader.getInstance().displayImage(doctoricon, viewHolder2.iv_left_icon);
                break;

        }

        return convertView;
    }

    public class ViewHolder1 {
        private TextView tv_right_text;
        private CircleImageView iv_right_head;

    }

    public class ViewHolder2 {
        private TextView tv_left_text;
        private CircleImageView iv_left_icon;

    }

}