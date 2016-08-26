package com.example.chen.tset;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class LectureroomFragment extends Fragment{
    View view;
    private ListView listView;
    private LectureroomAdapter adapter;
    List<String> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_lectureroom,null);
        findView();
        init();
        return view;
    }

    private void findView() {
        listView= (ListView) view.findViewById(R.id.listView);
        listView.setVerticalScrollBarEnabled(false);
    }
    private void init() {
        list=new ArrayList<>();
        list.add("张老师为你讲解膝盖积水的处理和治疗");
        list.add("张老师为你讲解膝盖积水的处理和治疗1");
        list.add("张老师为你讲解膝盖积水的处理和治疗2");
        list.add("张老师为你讲解膝盖积水的处理和治疗3");
        list.add("张老师为你讲解膝盖积水的处理和治疗4");
        list.add("张老师为你讲解膝盖积水的处理和治疗5");
        list.add("张老师为你讲解膝盖积水的处理和治疗6");
        adapter=new LectureroomAdapter(getContext(),list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
