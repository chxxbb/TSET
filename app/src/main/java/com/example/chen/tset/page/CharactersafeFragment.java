package com.example.chen.tset.page;

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
import android.widget.Toast;

import com.example.chen.tset.Data.Consult;
import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.Lecture;
import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.example.chen.tset.View.ConsultPageActivity;
import com.example.chen.tset.View.LogActivity;
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
 * 资讯页面
 */
public class CharactersafeFragment extends Fragment {
    View view;
    CharactersafeAdapter1 adapter;
    private ListView lv_charactersafe;
    private RelativeLayout rl_nonetwork;
    List<Consult> list;
    Gson gson;
    int i;

    SharedPsaveuser sp;

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_charactersafe, null);

        findView();





        sp=new SharedPsaveuser(getContext());

        init();

        return view;
    }

    private void findView() {
        lv_charactersafe = (ListView) view.findViewById(R.id.lv_charactersafe);
        rl_nonetwork = (RelativeLayout) view.findViewById(R.id.rl_nonetwork);
        lv_charactersafe.setOnItemClickListener(listener);
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    private void init() {
        gson = new Gson();
        list = new ArrayList<>();
        adapter = new CharactersafeAdapter1(getContext(), list);
        lv_charactersafe.setAdapter(adapter);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("i",i+"");
                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/FindCyclopediaListByCategoryId")
                        .addParams("categoryId", i + "")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                handler.sendEmptyMessage(1);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.e("资讯返回", response);
                                if(!response.equals("1")){
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
                    adapter.notifyDataSetChanged();
                    break;
                case 1:
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

}
