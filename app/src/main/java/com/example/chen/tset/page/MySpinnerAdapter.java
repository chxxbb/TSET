package com.example.chen.tset.page;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/10/28 0028.
 * //自定义spinner
 */
public class MySpinnerAdapter extends ArrayAdapter{
    Context context;


    public MySpinnerAdapter(Context context, int resource, Object[] objects) {
        super(context, resource, objects);
        this.context=context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(0);
        return convertView;
    }
}
