package com.example.chen.tset.page.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.entity.Consult;
import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.example.chen.tset.View.activity.ConsultPageActivity;
import com.example.chen.tset.page.adapter.CharactersafeAdapter1;
import com.example.chen.tset.page.view.LoadListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/8/25 0025.
 * 资讯页面
 */
public class CharactersafeFragment extends Fragment implements LoadListView.IloadListener {
    View view;
    CharactersafeAdapter1 adapter;
    private LoadListView lv_charactersafe;
    private RelativeLayout rl_nonetwork, rl_loading;
    List<Consult> list;
    Gson gson;

    SharedPsaveuser sp;

    PtrClassicFrameLayout ptrClassicFrameLayout;

    int page = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_charactersafe, null);

        findView();

        sp = new SharedPsaveuser(getContext());
        gson = new Gson();
        list = new ArrayList<>();
        adapter = new CharactersafeAdapter1(getContext(), list);
        lv_charactersafe.setAdapter(adapter);

        init();

        return view;
    }

    private void findView() {
        lv_charactersafe = (LoadListView) view.findViewById(R.id.lv_charactersafe);
        rl_nonetwork = (RelativeLayout) view.findViewById(R.id.rl_nonetwork);
        rl_loading = (RelativeLayout) view.findViewById(R.id.rl_loading);
        ptrClassicFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptrClassicFrameLayout);


        lv_charactersafe.setInterface(this);

        lv_charactersafe.setVerticalScrollBarEnabled(false);


        //2次下拉时间间隔
        ptrClassicFrameLayout.setDurationToCloseHeader(1000);


        //下拉刷新
        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

                //加载更多聊天记录
                ptrClassicFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 0;
                        list.clear();
                        init();

                    }
                    //设定加载更多聊天记录需要的时间
                }, 2000);

            }
        });


        lv_charactersafe.setOnItemClickListener(listener);


        rl_nonetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_loading.setVisibility(View.VISIBLE);
                rl_nonetwork.setVisibility(View.GONE);
                init();

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    private void init() {


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/FindCyclopediaList")
                        .addParams("pageCount", page + "")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                handler.sendEmptyMessage(1);
                            }

                            @Override
                            public void onResponse(String response, int id) {


                                if (response.equals("1")) {
                                    Toast.makeText(getContext(), "数据获取失败", Toast.LENGTH_SHORT).show();


                                } else if (response.equals("[]")) {
                                    Toast.makeText(getContext(), "已经没有更多数据了", Toast.LENGTH_SHORT).show();
                                } else {

                                    Type listtype = new TypeToken<LinkedList<Consult>>() {
                                    }.getType();

                                    LinkedList<Consult> leclist = gson.fromJson(response, listtype);
                                    for (Iterator it = leclist.iterator(); it.hasNext(); ) {
                                        Consult consult = (Consult) it.next();
                                        list.add(consult);
                                    }

                                    handler.sendEmptyMessage(0);

                                }

                            }
                        });
            }
        });
        thread.start();
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    lv_charactersafe.setVisibility(View.VISIBLE);
                    ptrClassicFrameLayout.refreshComplete();
                    adapter.notifyDataSetChanged();
                    rl_loading.setVisibility(View.GONE);
                    break;
                case 1:
                    lv_charactersafe.setVisibility(View.GONE);
                    ptrClassicFrameLayout.refreshComplete();
                    rl_loading.setVisibility(View.GONE);
                    rl_nonetwork.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };


    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getContext(), ConsultPageActivity.class);
            intent.putExtra("information", list.get(position).getId() + "");
            //根据点击页面判断是否为收藏页面，如果为隐藏赞
            intent.putExtra("collect", "0");
            startActivity(intent);
        }
    };


    @Override
    public void onLoad() {
        //加载更多延迟3秒
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub

                page = page + 20;
                init();
                // 通知listview加载完毕
                lv_charactersafe.loadComplete();


            }
        }, 3000);
    }


}
