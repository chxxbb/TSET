package com.example.chen.tset.View.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.entity.Information;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.IListener;
import com.example.chen.tset.Utils.ListenerManager;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.example.chen.tset.page.adapter.CharactersafeAdapter;
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
public class MycollectActivity extends MyBaseActivity implements IListener {
    private SwipeMenuListView lv_collect;
    CharactersafeAdapter adapter;
    List<Information> list;
    private LinearLayout ll_collectretur;
    private RelativeLayout rl_nonetwork, rl_loading;
    Gson gson;
    SharedPsaveuser sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycollect);

        try {

            ListenerManager.getInstance().registerListtener(this);
            findView();
            init();
            initHttp();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void findView() {
        lv_collect = (SwipeMenuListView) findViewById(R.id.lv_collect);
        ll_collectretur = (LinearLayout) findViewById(R.id.ll_collectretur);
        rl_nonetwork = (RelativeLayout) findViewById(R.id.rl_nonetwork);
        rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
        lv_collect.setVerticalScrollBarEnabled(false);
        ll_collectretur.setOnClickListener(listener);
        lv_collect.setOnItemClickListener(lvlitener);

        DisplayMetrics dm = new DisplayMetrics();
        dm = this.getResources().getDisplayMetrics();
        final float density = dm.density;

        //listview的item向左滑动可出现删除收藏按钮
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                //设置删除收藏按钮长宽，颜色，字体
                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                openItem.setBackground(new ColorDrawable(Color.RED));
                openItem.setWidth((int) (75 * density));
                openItem.setTitle("删除");
                openItem.setTitleSize(17);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);
            }
        };

        lv_collect.setMenuCreator(creator);
        lv_collect.setOnMenuItemClickListener(onmentlistener);

    }

    private void init() {
        gson = new Gson();
        list = new ArrayList<>();
        adapter = new CharactersafeAdapter(this, list);
        lv_collect.setAdapter(adapter);

    }


    private void initHttp() {
        sp = new SharedPsaveuser(this);
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/findCollectList")
                .addParams("userId", sp.getTag().getId() + "")
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


                        list.clear();
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

    private SwipeMenuListView.OnMenuItemClickListener onmentlistener = new SwipeMenuListView.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
            OkHttpUtils
                    .post()
                    .url(Http_data.http_data + "/DeleteCollectById")
                    .addParams("id", list.get(position).getId() + "")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Log.e("失败", "失败");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (response.equals("0")) {
                                Toast.makeText(MycollectActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                list.clear();
                                initHttp();

                            }
                        }
                    });

            return false;
        }
    };


    //从其他页面接送到广播，如果为“更新我的收藏”则此页面刷新
    @Override
    public void notifyAllActivity(String str) {
        if (str.equals("更新我的收藏")) {

            initHttp();
        }
    }
}
