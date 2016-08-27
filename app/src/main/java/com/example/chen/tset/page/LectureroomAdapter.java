package com.example.chen.tset.page;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chen.tset.Data.Lecture;
import com.example.chen.tset.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/8/26 0026.
 */
public class LectureroomAdapter extends RecyclerView.Adapter {
    List<Lecture> list;
    Context context;
    LayoutInflater inflater;

    public LectureroomAdapter(Context context, List<Lecture> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    class Viewholder extends RecyclerView.ViewHolder {
        private TextView tv_lectr;
        private RoundCornerImageView rcImageView;
        public Viewholder(View itemView) {
            super(itemView);
            tv_lectr = (TextView) itemView.findViewById(R.id.tv_lectr);
            rcImageView= (RoundCornerImageView) itemView.findViewById(R.id.rcImageView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lectureroom_item, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Viewholder viewholder = (Viewholder) holder;
        viewholder.tv_lectr.setText(list.get(position).getTitle());
        String uri=list.get(position).getCover();
        ImageLoader.getInstance().displayImage(uri, viewholder.rcImageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
