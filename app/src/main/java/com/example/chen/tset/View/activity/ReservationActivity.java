package com.example.chen.tset.View.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.entity.Reservation;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.IListener;
import com.example.chen.tset.Utils.ListenerManager;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.example.chen.tset.page.adapter.ReservationlistvAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;

/**
 * 我的预约
 */
public class ReservationActivity extends MyBaseActivity implements View.OnClickListener,IListener {
    private LinearLayout ll_reservationreturn;
    ReservationlistvAdapter adapter;
    private ListView lv_reser;
    private RelativeLayout rl_nonetwork, rl_loading;
    List<Reservation> list;
    List<Reservation> data;
    Gson gson = new Gson();
    SharedPsaveuser sp;

    //233

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservationactivity);
        data = new ArrayList<>();
        sp=new SharedPsaveuser(this);
        //注册广播，用于用户取消订单后通知我的预约页面更新
        ListenerManager.getInstance().registerListtener(this);
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

        list = new ArrayList<>();
        adapter = new ReservationlistvAdapter(this, list);
        lv_reser.setAdapter(adapter);
    }




    private void init() {

        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/FindOrderListByUserId")
                .addParams("userId", sp.getTag().getId() + "")
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
                        list.clear();
                        Type listtype = new TypeToken<LinkedList<Reservation>>() {
                        }.getType();
                        LinkedList<Reservation> leclist = gson.fromJson(response, listtype);
                        for (Iterator it = leclist.iterator(); it.hasNext(); ) {
                            Reservation reservation = (Reservation) it.next();
                            list.add(reservation);
                        }
                        //使list倒序排列，从而最上面显示的是最新的一条挂号
                        Collections.reverse(list);
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
            //根据预约ID获取预约详情
            Intent intent = new Intent(ReservationActivity.this, ReservationlistActivity.class);
            intent.putExtra("ReservationID", list.get(position).getId() + "");
            startActivity(intent);
        }
    };


    //从其他页面发送的广播，如果为“更新我的预约”则刷新此页面
    @Override
    public void notifyAllActivity(String str) {
        if(str.equals("更新我的预约")){
            init();
        }
    }
}
