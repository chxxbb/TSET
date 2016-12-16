package com.example.chen.tset.page.fragment;


import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.chen.tset.Data.entity.DiseaseBanner;
import com.example.chen.tset.Data.entity.DiseaseDepartment;
import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.BannerImageLoader;
import com.example.chen.tset.Utils.IListener;
import com.example.chen.tset.Utils.ListenerManager;
import com.example.chen.tset.View.activity.WebActivity;
import com.example.chen.tset.page.adapter.DiseaseliblistvAdapter;
import com.example.chen.tset.page.adapter.DiseaselibrecyvAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerClickListener;
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
 * 疾病库页面
 */
public class DiseaselibFragment extends Fragment implements IListener {
    View view;
    DiseaseliblistvAdapter adapter;
    DiseaselibrecyvAdapter diseaselibrecyvAdapter;
    ListView listview_dise;
    RecyclerView recyv_dise;
    //左边listview集合
    List<DiseaseDepartment> list;
    //右边RecyclerView集合
    List<String> list1;
    Gson gson = new Gson();
    String[] a = null;
    private RelativeLayout rl_nonetwork, rl_loading;
    private View view1;
    int mSelect = 0;
    int pos = 0;

    private Banner diseaseBannerView;

    List<DiseaseBanner> bannerList = new ArrayList<>();
    List<String> data = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_diseaselib, null);

        try {

            //注册广播
            ListenerManager.getInstance().registerListtener(this);
            findView();
            listviewinit();
            bannerinit();
            httpinit(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void bannerinit() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                final Gson gson = new Gson();
                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/FindBannerListByCategoryCode2")
                        .build()
                        .execute(new StringCallback() {

                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {


                                Type listtype = new TypeToken<LinkedList<DiseaseBanner>>() {
                                }.getType();
                                LinkedList<DiseaseBanner> leclist = gson.fromJson(response, listtype);
                                for (Iterator it = leclist.iterator(); it.hasNext(); ) {
                                    DiseaseBanner diseaseBanner = (DiseaseBanner) it.next();
                                    bannerList.add(diseaseBanner);

                                }
                                handler.sendEmptyMessage(3);

                            }

                        });

            }
        }).start();
    }


    private void findView() {
        listview_dise = (ListView) view.findViewById(R.id.listview_dise);
        recyv_dise = (RecyclerView) view.findViewById(R.id.recyv_dise);
        view1 = view.findViewById(R.id.view1);
        rl_nonetwork = (RelativeLayout) view.findViewById(R.id.rl_nonetwork);
        rl_loading = (RelativeLayout) view.findViewById(R.id.rl_loading);
        diseaseBannerView = (Banner) view.findViewById(R.id.bannerView);

        diseaseBannerView.setDelayTime(100000);
        listview_dise.setOnItemClickListener(listener);
        listview_dise.setOnItemSelectedListener(slistener);
        recyv_dise.setHasFixedSize(true);
        recyv_dise.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        listview_dise.setVerticalScrollBarEnabled(false);
        recyv_dise.setVerticalScrollBarEnabled(false);
        list = new ArrayList<>();

        list1 = new ArrayList<>();

        adapter = new DiseaseliblistvAdapter(getContext(), list);
        listview_dise.setAdapter(adapter);

        diseaselibrecyvAdapter = new DiseaselibrecyvAdapter(getContext(), list1);

        recyv_dise.setAdapter(diseaselibrecyvAdapter);


        rl_nonetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rl_loading.setVisibility(View.VISIBLE);


                httpinit(0);
                bannerinit();
                listviewinit();


            }
        });


        diseaseBannerView.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
//                Intent intent = new Intent(getContext(), WebActivity.class);
//                intent.putExtra("url", bannerList.get(position - 1).getSite());
//                startActivity(intent);
            }
        });

    }

    private void listviewinit() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/FindSectionList")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                handler.sendEmptyMessage(1);

                            }

                            @Override
                            public void onResponse(String response, int id) {


                                view1.setVisibility(View.GONE);
                                Type listtype = new TypeToken<LinkedList<DiseaseDepartment>>() {
                                }.getType();
                                LinkedList<DiseaseDepartment> leclist = gson.fromJson(response, listtype);
                                for (Iterator it = leclist.iterator(); it.hasNext(); ) {
                                    DiseaseDepartment dd = (DiseaseDepartment) it.next();
                                    list.add(dd);
                                }
                                handler.sendEmptyMessage(0);

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
                    diseaseBannerView.setVisibility(View.VISIBLE);
                    recyv_dise.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                    view1.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    view1.setVisibility(View.GONE);
                    rl_loading.setVisibility(View.GONE);
                    recyv_dise.setVisibility(View.GONE);
                    diseaseBannerView.setVisibility(View.GONE);
                    rl_nonetwork.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    diseaseBannerView.setVisibility(View.VISIBLE);
                    recyv_dise.setVisibility(View.VISIBLE);
                    diseaselibrecyvAdapter.setList(list1);
                    diseaselibrecyvAdapter.notifyDataSetChanged();

                    rl_nonetwork.setVisibility(View.GONE);
                    rl_loading.setVisibility(View.GONE);
                    break;
                case 3:

                    for (int i = 0; i < bannerList.size(); i++) {
                        data.add(bannerList.get(i).getCover());
                    }

                    diseaseBannerView.setImageLoader(new BannerImageLoader());
                    //设置图片集合
                    diseaseBannerView.setImages(data);
                    //banner设置方法全部调用完毕时最后调用
                    diseaseBannerView.start();
                    break;

            }
        }
    };

    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            adapter.changeSelected(position);
            //根据点击的条目加载不同的数据
            if (pos != position) {
                httpinit(position);
            }

            pos = position;


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

    private void httpinit(final int sectionid) {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/findDiseaseList")
                        .addParams("sectionId", sectionid + "")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                handler.sendEmptyMessage(1);
                            }

                            @Override
                            public void onResponse(String response, int id) {


                                list1 = gson.fromJson(response, new TypeToken<List<String>>() {
                                }.getType());

                                handler.sendEmptyMessage(2);
                            }
                        });
            }
        });
        thread1.start();


    }


    @Override
    public void notifyAllActivity(String str) {

    }
}
