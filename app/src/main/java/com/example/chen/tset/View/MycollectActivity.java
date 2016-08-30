package com.example.chen.tset.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.R;
import com.example.chen.tset.page.CharactersafeAdapter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class MycollectActivity extends AppCompatActivity {
    private ListView lv_collect;
    CharactersafeAdapter adapter;
    List<String> list;
    private LinearLayout ll_collectretur;

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
        ll_collectretur.setOnClickListener(listener);
        lv_collect.setOnItemClickListener(lvlitener);

    }

    private void init() {
        list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        adapter = new CharactersafeAdapter(this, list);
        lv_collect.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initHttp() {
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/findCollectList")
                .addParams("id", "1")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(MycollectActivity.this, "失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Toast.makeText(MycollectActivity.this, response, Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private AdapterView.OnItemClickListener lvlitener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MycollectActivity.this, ConsultPageActivity.class);
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
