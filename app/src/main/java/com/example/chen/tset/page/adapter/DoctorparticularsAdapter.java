package com.example.chen.tset.page.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chen.tset.Data.entity.Doctorcomment;
import com.example.chen.tset.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/9/5 0005.
 */
public class DoctorparticularsAdapter extends BaseAdapter {
    private Context context;
    private List<Doctorcomment> list;

    public DoctorparticularsAdapter(Context context, List<Doctorcomment> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Doctorcomment getItem(int position) {
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
            convertView = inflater.inflate(R.layout.doctorparticulars_comment_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        ImageLoader.getInstance().displayImage(list.get(position).getUserIcon(), viewHolder.iv_icon);
        viewHolder.tv_content.setText(list.get(position).getContent());
        viewHolder.tv_name.setText(list.get(position).getUserName());
        viewHolder.tv_time.setText(list.get(position).getTime());
        return convertView;
    }

    static class ViewHolder {
        private TextView tv_content, tv_time, tv_name;
        private CircleImageView iv_icon;

        ViewHolder(View v) {
            tv_content = (TextView) v.findViewById(R.id.tv_content);
            tv_time = (TextView) v.findViewById(R.id.tv_time);
            tv_name = (TextView) v.findViewById(R.id.tv_name);
            iv_icon = (CircleImageView) v.findViewById(R.id.iv_icon);
        }
    }
}
