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
import com.example.chen.tset.Data.Reservation;
import com.example.chen.tset.R;
import com.example.chen.tset.page.ReservationlistvAdapter;
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

public class ReservationActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout ll_reservationreturn;
    ReservationlistvAdapter adapter;
    private ListView lv_reser;
    private RelativeLayout rl_nonetwork, rl_loading;
    List<Reservation> list;
    Gson gson = new Gson();

    //233

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservationactivity);
        findView();
        init();
    }

    private void findView() {
        lv_reser = (ListView) findViewById(R.id.lv_reser);
        ll_reservationreturn = (LinearLayout) findViewById(R.id.ll_reservationreturn);
        rl_nonetwork = (RelativeLayout) findViewById(R.id.rl_nonetwork);
        rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
        lv_reser.setVerticalScrollBarEnabled(false);
        ll_reservationreturn.setOnClickListener(this);
        lv_reser.setOnItemClickListener(listener);
    }


    private void init() {
        list = new ArrayList<>();
        adapter = new ReservationlistvAdapter(this, list);
        lv_reser.setAdapter(adapter);
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/findReservation")
                .addParams("user_id", "1")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(ReservationActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        rl_loading.setVisibility(View.GONE);
                        rl_nonetwork.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("我的预约返回", response);
                        Type listtype = new TypeToken<LinkedList<Reservation>>() {
                        }.getType();
                        LinkedList<Reservation> leclist = gson.fromJson(response, listtype);
                        for (Iterator it = leclist.iterator(); it.hasNext(); ) {
                            Reservation reservation = (Reservation) it.next();
                            list.add(reservation);
                        }
                        adapter.notifyDataSetChanged();
                        rl_loading.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(ReservationActivity.this, ReservationlistActivity.class);
            startActivity(intent);
        }
    };
}
