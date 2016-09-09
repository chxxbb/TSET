package com.example.chen.tset.page;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.Lecture;
import com.example.chen.tset.Data.User;
import com.example.chen.tset.R;
import com.example.chen.tset.View.HomeActivity;
import com.example.chen.tset.View.SetdataActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.utils.L;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class LectureroomFragment extends Fragment {
    View view;
    private RecyclerView recyclerView;
    private LectureroomAdapter adapter;
    private RelativeLayout rl_nonetwork, rl_loading;
    List<Lecture> list;
    Gson gson;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lectureroom, null);
        findView();
        init();
        gson = new Gson();
        return view;
    }


    private void findView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        rl_nonetwork = (RelativeLayout) view.findViewById(R.id.rl_nonetwork);
        rl_loading = (RelativeLayout) view.findViewById(R.id.rl_loading);
        recyclerView.setVerticalScrollBarEnabled(false);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }


    private void init() {
        list = new ArrayList<>();
        adapter = new LectureroomAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/findKnowledgeLectureList" + "?categoryId")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        rl_loading.setVisibility(View.GONE);
                        rl_nonetwork.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("讲堂返回", response);
                        Type listtype = new TypeToken<LinkedList<Lecture>>() {
                        }.getType();
                        LinkedList<Lecture> leclist = gson.fromJson(response, listtype);
                        for (Iterator it = leclist.iterator(); it.hasNext(); ) {
                            Lecture lecture = (Lecture) it.next();
                            list.add(lecture);
                        }
                        adapter.setList(list);
                        rl_loading.setVisibility(View.GONE);
                    }
                });

    }

}
