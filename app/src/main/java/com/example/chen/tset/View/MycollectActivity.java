package com.example.chen.tset.View;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.Information;
import com.example.chen.tset.Data.Lecture;
import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.example.chen.tset.page.CharactersafeAdapter;
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
 * 我的收藏
 */
public class MycollectActivity extends MyBaseActivity {
    private ListView lv_collect;
    CharactersafeAdapter adapter;
    List<Information> list;
    private LinearLayout ll_collectretur;
    private RelativeLayout rl_nonetwork, rl_loading;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycollect);
        findView();
        init();
        initHttp();
    }


    private void findView() {
        lv_collect = (ListView) findViewById(R.id.lv_collect);
        ll_collectretur = (LinearLayout) findViewById(R.id.ll_collectretur);
        rl_nonetwork = (RelativeLayout) findViewById(R.id.rl_nonetwork);
        rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
        lv_collect.setVerticalScrollBarEnabled(false);
        ll_collectretur.setOnClickListener(listener);
        lv_collect.setOnItemClickListener(lvlitener);

    }

    private void init() {
        gson = new Gson();
        list = new ArrayList<>();
        adapter = new CharactersafeAdapter(this, list);
        lv_collect.setAdapter(adapter);

    }


    private void initHttp() {
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/findCollectList")
                .addParams("userId", User_Http.user.getId() + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(MycollectActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        rl_loading.setVisibility(View.GONE);
                        rl_nonetwork.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("收藏返回", response);
                        Type listtype = new TypeToken<LinkedList<Information>>() {
                        }.getType();
                        LinkedList<Information> leclist = gson.fromJson(response, listtype);
                        for (Iterator it = leclist.iterator(); it.hasNext(); ) {
                            Information information = (Information) it.next();
                            list.add(information);
                        }
                        adapter.notifyDataSetChanged();
                        rl_loading.setVisibility(View.GONE);

                    }
                });
    }

    private AdapterView.OnItemClickListener lvlitener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //向下个页面传递判断是否为收藏页面和文章ID
            Intent intent = new Intent(MycollectActivity.this, ConsultPageActivity.class);
            intent.putExtra("collect", "1");
            intent.putExtra("information", list.get(position).getCyclopediaId());
            startActivity(intent);
        }
    };
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_collectretur:
                    finish();
                    break;
            }
        }
    };
}
