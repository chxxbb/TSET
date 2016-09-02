package com.example.chen.tset.page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.chen.tset.Data.Inquiry;
import com.example.chen.tset.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/9/1 0001.
 */
public class InquiryAdapter extends BaseAdapter {
    private Context context;
    private List<Inquiry> list;

    public InquiryAdapter(Context context, List<Inquiry> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Inquiry getItem(int position) {
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
            convertView = inflater.inflate(R.layout.inquiry_list_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.textView.setText(9.9 + "分");
        viewHolder.tv_title.setText(list.get(position).getTitle());
        viewHolder.tv_name.setText(list.get(position).getName());
        viewHolder.btn_money.setText(list.get(position).getMoney());
        viewHolder.tv_intro.setText(list.get(position).getIntro());
        viewHolder.tv_section.setText(list.get(position).getSection());
        ImageLoader.getInstance().displayImage(list.get(position).getIcon(), viewHolder.iv_icon);
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

        ViewHolder(View v) {
            textView = (TextView) v.findViewById(R.id.textView);
            iv_icon = (CircleImageView) v.findViewById(R.id.iv_icon);
            tv_title = (TextView) v.findViewById(R.id.tv_title);
            tv_name = (TextView) v.findViewById(R.id.tv_name);
            btn_money = (Button) v.findViewById(R.id.btn_money);
            tv_intro = (TextView) v.findViewById(R.id.tv_intro);
            tv_section = (TextView) v.findViewById(R.id.tv_section);
        }
    }
}
