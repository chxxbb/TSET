package com.example.chen.tset.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.Information;
import com.example.chen.tset.Data.Pharmacyremind;
import com.example.chen.tset.R;
import com.example.chen.tset.page.PharmacyremindAdapter;
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

public class PharmacyremindActivity extends AppCompatActivity {
    private ListView lv_pharmacy_remind;
    private LinearLayout ll_add_remind;
    PharmacyremindAdapter adapter;
    List<Pharmacyremind> list;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacyremind);
        findView();
        init();
    }


    private void findView() {
        lv_pharmacy_remind = (ListView) findViewById(R.id.lv_pharmacy_remind);
        ll_add_remind = (LinearLayout) findViewById(R.id.ll_add_remind);
        ll_add_remind.setOnClickListener(listener);
        list = new ArrayList<>();
        adapter = new PharmacyremindAdapter(this, list);
        lv_pharmacy_remind.setAdapter(adapter);
        lv_pharmacy_remind.setVerticalScrollBarEnabled(false);

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_add_remind:
                    Intent intent = new Intent(PharmacyremindActivity.this, CompileremindActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    private void init() {
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/findRemind")
                .addParams("id", "1")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(PharmacyremindActivity.this, "失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Type listtype = new TypeToken<LinkedList<Pharmacyremind>>() {
                        }.getType();
                        LinkedList<Pharmacyremind> leclist = gson.fromJson(response, listtype);
                        for (Iterator it = leclist.iterator(); it.hasNext(); ) {
                            Pharmacyremind pharmacyremind = (Pharmacyremind) it.next();
                            list.add(pharmacyremind);
                        }
                        adapter.notifyDataSetChanged();
                    }

                });
    }
}
