package com.example.chen.tset.page;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
        if (position % 2 == 0) {
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
