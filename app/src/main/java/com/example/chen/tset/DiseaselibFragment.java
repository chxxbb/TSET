package com.example.chen.tset;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class DiseaselibFragment extends Fragment {
    View view;
    DiseaseliblistvAdapter adapter;
    DiseaselibrecyvAdapter diseaselibrecyvAdapter;
    ListView listview_dise,recyv_dise;
    //左边listview集合
    List<String> list;
    //右边RecyclerView集合
    List<String> list1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_diseaselib,null);
        findView();
        listviewinit();
        recyclerViewinit();
        return view;
    }



    private void findView() {
        listview_dise= (ListView) view.findViewById(R.id.listview_dise);
        recyv_dise= (ListView) view.findViewById(R.id.recyv_dise);
        listview_dise.setOnItemClickListener(listener);
        listview_dise.setVerticalScrollBarEnabled(false);
    }
    private void listviewinit() {
        list=new ArrayList<>();
        list.add("行为发育");
        list.add("小儿神经");
        list.add("内分泌");
        list.add("儿童保健");
        list.add("儿童皮肤");
        list.add("耳鼻喉");
        list.add("过敏反应");
        list.add("小儿外科");
        adapter=new DiseaseliblistvAdapter(getContext(),list);
        listview_dise.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private void recyclerViewinit() {
        list1=new ArrayList<>();
        list1.add("多动症");
        list1.add("多动症1");
        list1.add("多动症2");
        list1.add("多动症3");
        list1.add("多动症4");
        list1.add("多动症5");
        list1.add("多动症6");
        list1.add("多动症7");
        diseaselibrecyvAdapter=new DiseaselibrecyvAdapter(getContext(),list1);
        recyv_dise.setAdapter(diseaselibrecyvAdapter);
        diseaselibrecyvAdapter.notifyDataSetChanged();

    }

    private AdapterView.OnItemClickListener listener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    };
}
