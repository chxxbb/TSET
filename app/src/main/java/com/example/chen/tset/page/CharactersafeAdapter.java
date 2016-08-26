package com.example.chen.tset.page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chen.tset.R;

import java.util.List;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class CharactersafeAdapter extends BaseAdapter {
    Context context;
    List<String> list;

    public CharactersafeAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
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
            convertView = inflater.inflate(R.layout.encyclopedia_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.tv.setText(list.get(position));
        return convertView;
    }

    static class ViewHolder {
        private TextView tv;

        ViewHolder(View v) {
            tv = (TextView) v.findViewById(R.id.tv);
        }
    }
}
