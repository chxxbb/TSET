package com.example.chen.tset.page;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chen.tset.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class DiseaselibFragment extends Fragment {
    View view;
    DiseaseliblistvAdapter adapter;
    DiseaselibrecyvAdapter diseaselibrecyvAdapter;
    ListView listview_dise, recyv_dise;
    //左边listview集合
    List<String> list;
    //右边RecyclerView集合
    List<String> list1;
    private LinearLayout dise_ll = null;
    private TextView tv_dislistv = null;
    private ImageView iv_dislistv;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_diseaselib, null);
        findView();
        listviewinit();
        recyclerViewinit();
        return view;
    }

    private void findView() {
        listview_dise = (ListView) view.findViewById(R.id.listview_dise);
        recyv_dise = (ListView) view.findViewById(R.id.recyv_dise);
        listview_dise.setOnItemClickListener(listener);
        listview_dise.setVerticalScrollBarEnabled(false);
        recyv_dise.setVerticalScrollBarEnabled(false);
    }

    private void listviewinit() {
        list = new ArrayList<>();
        list.add("小儿呼吸");
        list.add("小儿消化");
        list.add("行为发育");
        list.add("小儿神经");
        list.add("儿童内分泌");
        list.add("儿童皮肤");
        list.add("小儿眼科");
        list.add("儿童耳鼻喉");
        list.add("小儿口腔");
        list.add("小儿外科");
        list.add("小儿泌尿");
        list.add("小儿肾病");
        list.add("新生儿科");
        list.add("小儿保健");
        list.add("儿童心理");
        adapter = new DiseaseliblistvAdapter(getContext(), list);
        listview_dise.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void recyclerViewinit() {
        list1 = new ArrayList<>();
        list1.add("多动症");
        list1.add("多动症1");
        list1.add("多动症2");
        list1.add("多动症3");
        list1.add("多动症4");
        list1.add("多动症5");
        list1.add("多动症6");
        list1.add("多动症7");
        list1.add("多动症7");
        list1.add("多动症7");
        list1.add("多动症7");
        list1.add("多动症7");
        list1.add("多动症7");
        list1.add("多动症7");
        list1.add("多动症7");
        list1.add("多动症7");
        list1.add("多动症7");

        diseaselibrecyvAdapter = new DiseaselibrecyvAdapter(getContext(), list1);
        recyv_dise.setAdapter(diseaselibrecyvAdapter);
        diseaselibrecyvAdapter.notifyDataSetChanged();

    }


    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (dise_ll == null) {
                dise_ll = (LinearLayout) view.findViewById(R.id.dise_ll);
                dise_ll.setBackgroundColor(android.graphics.Color.parseColor("#6fc9e6"));
                tv_dislistv = (TextView) view.findViewById(R.id.tv_dislistv);
                tv_dislistv.setTextColor(0xffffffff);
            } else {
                dise_ll.setBackgroundColor(android.graphics.Color.parseColor("#e0e0e0"));
                tv_dislistv.setTextColor(android.graphics.Color.parseColor("#323232"));
                dise_ll = (LinearLayout) view.findViewById(R.id.dise_ll);
                dise_ll.setBackgroundColor(android.graphics.Color.parseColor("#6fc9e6"));
                tv_dislistv = (TextView) view.findViewById(R.id.tv_dislistv);
                tv_dislistv.setTextColor(0xffffffff);
            }
        }
    };
}
