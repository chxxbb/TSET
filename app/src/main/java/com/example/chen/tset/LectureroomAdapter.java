package com.example.chen.tset;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/8/26 0026.
 */
public class LectureroomAdapter extends BaseAdapter{
    List<String> list;
    Context context;

    public LectureroomAdapter(Context context, List<String> list) {
        this.context = context;
        this.list=list;
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

    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater=LayoutInflater.from(parent.getContext());
            convertView=inflater.inflate(R.layout.lectureroom_item,parent,false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder= (ViewHolder) convertView.getTag();
        viewHolder.tv_lectr.setText(list.get(position));
        return convertView;
    }
    static class ViewHolder{
        private TextView tv_lectr;
        ViewHolder(View v){
            tv_lectr= (TextView) v.findViewById(R.id.tv_lectr);
        }
    }
}
