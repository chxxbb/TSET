package com.example.chen.tset.page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chen.tset.Data.Registration;
import com.example.chen.tset.R;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5 0005.
 */
public class DoctorparticularsAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;

    public DoctorparticularsAdapter(Context context, List<String> list) {
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
            convertView = inflater.inflate(R.layout.doctorparticulars_comment_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.textView.setText(list.get(position));
        return convertView;
    }

    static class ViewHolder {
        private TextView textView;

        ViewHolder(View v) {
            textView = (TextView) v.findViewById(R.id.textView);
        }
    }
}
