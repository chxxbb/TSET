package com.example.chen.tset.page;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chen.tset.Data.Lecture;
import com.example.chen.tset.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class DiseaselibrecyvAdapter extends RecyclerView.Adapter {
    //    private final List<String> list;
//    Context context;
//
//    public DiseaselibrecyvAdapter(Context context, List<String> list) {
//        this.context = context;
//        this.list = list;
//    }
//
//
//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    @Override
//    public String getItem(int position) {
//        return list.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//            convertView = inflater.inflate(R.layout.disease_recyv_item, parent, false);
//            convertView.setTag(new ViewHolder(convertView));
//        }
//        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
//        viewHolder.dise_tv1.setText(list.get(position));
//        viewHolder.dise_tv1.setText(list.get(position));
//        return convertView;
//    }
//
//    static class ViewHolder {
//        private TextView dise_tv1;
//        private TextView dise_tv2;
//
//        ViewHolder(View v) {
//            dise_tv1 = (TextView) v.findViewById(R.id.dise_tv1);
//            dise_tv2 = (TextView) v.findViewById(R.id.dise_tv2);
//        }
//    }
    List<String> list;
    Context context;
    LayoutInflater inflater;
    View view;


    public DiseaselibrecyvAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    class Viewholder extends RecyclerView.ViewHolder {
        private TextView tv_disease;
        private View view;

        public Viewholder(View itemView) {
            super(itemView);
            tv_disease = (TextView) itemView.findViewById(R.id.tv_disease);
            view = itemView.findViewById(R.id.view);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.disease_recyv_item, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Viewholder viewholder = (Viewholder) holder;
        viewholder.tv_disease.setText(list.get(position));
        if (position % 2 != 0) {
            viewholder.view.setVisibility(View.VISIBLE);
        } else {
            viewholder.view.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
