package com.example.chen.tset.page;


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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.DiseaseDepartment;
import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class DiseaselibFragment extends Fragment {
    View view;
    DiseaseliblistvAdapter adapter;
    DiseaselibrecyvAdapter diseaselibrecyvAdapter;
    ListView listview_dise;
    RecyclerView recyv_dise;
    //左边listview集合
    List<DiseaseDepartment> list;
    //右边RecyclerView集合
    List<String> list1;
    Gson gson;
    String[] a = null;
    private View view1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_diseaselib, null);
        findView();
        listviewinit();
        httpinit(0);
        return view;
    }


    private void findView() {
        listview_dise = (ListView) view.findViewById(R.id.listview_dise);
        recyv_dise = (RecyclerView) view.findViewById(R.id.recyv_dise);
        view1 = view.findViewById(R.id.view1);
        listview_dise.setOnItemClickListener(listener);
        listview_dise.setOnItemSelectedListener(slistener);
        recyv_dise.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        listview_dise.setVerticalScrollBarEnabled(false);
        recyv_dise.setVerticalScrollBarEnabled(false);
        gson = new Gson();
    }

    private void listviewinit() {
        list = new ArrayList<>();
        adapter = new DiseaseliblistvAdapter(getContext(), list);
        listview_dise.setAdapter(adapter);
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/findSectionList")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        view1.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("百科疾病库", response);
                        view1.setVisibility(View.GONE);
                        Type listtype = new TypeToken<LinkedList<DiseaseDepartment>>() {
                        }.getType();
                        LinkedList<DiseaseDepartment> leclist = gson.fromJson(response, listtype);
                        for (Iterator it = leclist.iterator(); it.hasNext(); ) {
                            DiseaseDepartment dd = (DiseaseDepartment) it.next();
                            list.add(dd);
                        }
                        adapter.notifyDataSetChanged();
                        view1.setVisibility(View.VISIBLE);
                    }
                });
    }

    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            adapter.changeSelected(position);
            httpinit(position);
        }

    };
    private AdapterView.OnItemSelectedListener slistener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            adapter.changeSelected(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void httpinit(int sectionid) {
        list1 = new ArrayList<>();
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/findDiseaseList")
                .addParams("sectionId", sectionid + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        list1 = gson.fromJson(response, new TypeToken<List<String>>() {
                        }.getType());
                        diseaselibrecyvAdapter = new DiseaselibrecyvAdapter(getContext(), list1);
                        recyv_dise.setAdapter(diseaselibrecyvAdapter);
                        diseaselibrecyvAdapter.notifyDataSetChanged();
                    }
                });


    }
}
