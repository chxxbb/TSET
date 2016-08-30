package com.example.chen.tset.page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chen.tset.Data.Information;
import com.example.chen.tset.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class CharactersafeAdapter extends BaseAdapter {
    Context context;
    List<Information> list;

    public CharactersafeAdapter(Context context, List<Information> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Information getItem(int position) {
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
        viewHolder.tv_title.setText(list.get(position).getTitle());
        ImageLoader.getInstance().displayImage(list.get(position).getImgurl(), viewHolder.iv_img);
        return convertView;
    }

    static class ViewHolder {
        private TextView tv_title;
        private ImageView iv_img;

        ViewHolder(View v) {
            tv_title = (TextView) v.findViewById(R.id.tv_title);
            iv_img = (ImageView) v.findViewById(R.id.iv_img);
        }
    }
}
