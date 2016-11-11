package com.example.chen.tset.page.adapter;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chen.tset.R;

import java.util.List;

/**
 * Created by Administrator on 2016/11/8 0008.
 */
public class LectureListAdapter extends BaseAdapter{
    private List<String> list;
    Context context;


    public LectureListAdapter(Context context, List<String> list) {
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
            convertView = inflater.inflate(R.layout.lecture_list_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        TextPaint tp =  viewHolder.textView1.getPaint();
        tp.setFakeBoldText(true);

        viewHolder.textView.setText("李狗蛋"+list.get(position)+"号 / 25'61'");


        return convertView;
    }

    static class ViewHolder {
        TextView textView;
        TextView textView1;

        ViewHolder(View v) {
            textView= (TextView) v.findViewById(R.id.textView);
            textView1= (TextView) v.findViewById(R.id.textView1);

        }
    }
}
