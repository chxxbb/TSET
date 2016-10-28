package com.example.chen.tset.View;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.Lecture;
import com.example.chen.tset.R;
import com.example.chen.tset.page.LectureroomAdapter;
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

public class LectureoomActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LectureroomAdapter adapter;
    private RelativeLayout rl_nonetwork, rl_loading;
    List<Lecture> list;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_lectureroom);
        findView();
        init();
        gson = new Gson();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void findView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        rl_nonetwork = (RelativeLayout) findViewById(R.id.rl_nonetwork);
        rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
        recyclerView.setVerticalScrollBarEnabled(false);
        //RelativeLayout分成2列
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        list = new ArrayList<>();
        adapter = new LectureroomAdapter(this, list);
        recyclerView.setAdapter(adapter);

    }


    private void init() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/FindLectureAll")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                handler.sendEmptyMessage(0);
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
                                handler.sendEmptyMessage(1);
                            }
                        });
            }
        }).start();


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    rl_loading.setVisibility(View.GONE);
                    rl_nonetwork.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    adapter.setList(list);
                    rl_loading.setVisibility(View.GONE);
                    break;


            }
        }
    };
}
