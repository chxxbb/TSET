package com.example.chen.tset.page.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chen.tset.Data.entity.Lecture;
import com.example.chen.tset.R;
import com.example.chen.tset.page.view.RoundCornerImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/8/26 0026.
 */
public class LectureroomAdapter extends RecyclerView.Adapter {
    List<Lecture> list;
    Context context;
    LayoutInflater inflater;
    private RecylerViewListener listener;

    public List<Lecture> getList() {
        return list;
    }

    public void setList(List<Lecture> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public LectureroomAdapter(Context context, List<Lecture> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    class Viewholder extends RecyclerView.ViewHolder {
        private TextView tv_lectr;
        private RoundCornerImageView rcImageView;
        private LinearLayout linearLayout;

        public Viewholder(View itemView) {
            super(itemView);
            tv_lectr = (TextView) itemView.findViewById(R.id.tv_lectr);
            rcImageView = (RoundCornerImageView) itemView.findViewById(R.id.rcImageView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lectureroom_item, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Viewholder viewholder = (Viewholder) holder;
        viewholder.tv_lectr.setText(list.get(position).getTitle());
        String uri = list.get(position).getCover();
        ImageLoader.getInstance().displayImage(uri, viewholder.rcImageView);

        //点击打开手机自带播放器播放视频
        viewholder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(list.get(position).getVideo()), "video/mp4");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //给RecyclerView设置点击事件
    public interface RecylerViewListener {
        void onClick(View v, int position);
    }

    public void setRecylerViewListener(RecylerViewListener listener) {
        this.listener = listener;
    }


}
