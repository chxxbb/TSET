package com.example.chen.tset.View;


import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.MyDoctor;
import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.example.chen.tset.page.MyDoctorAdapter;
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
 * 我的医生
 */
public class MyDoctorActivity extends MyBaseActivity {
    private ListView lv_mydoctor;
    Gson gson;
    MyDoctorAdapter adapter;
    List<MyDoctor> list;
    private LinearLayout ll_rut;
    private RelativeLayout rl_nonetwork, rl_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_doctor);
        findView();
        init();
        httpinit();
    }

    private void findView() {
        lv_mydoctor = (ListView) findViewById(R.id.lv_mydoctor);
        ll_rut = (LinearLayout) findViewById(R.id.ll_rut);
        rl_nonetwork = (RelativeLayout) findViewById(R.id.rl_nonetwork);
        rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
        ll_rut.setOnClickListener(listener);
        lv_mydoctor.setOnItemClickListener(lvlistener);
        lv_mydoctor.setVerticalScrollBarEnabled(false);
    }

    private void init() {
        list = new ArrayList<>();
        adapter = new MyDoctorAdapter(MyDoctorActivity.this, list);
        lv_mydoctor.setAdapter(adapter);
    }


    private void httpinit() {
//        new Thread(new R)
        SharedPsaveuser sp=new SharedPsaveuser(this);
        gson = new Gson();
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/FindDoctorListByUserId")
                .addParams("userId", sp.getTag().getId() + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(MyDoctorActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        rl_loading.setVisibility(View.GONE);
                        rl_nonetwork.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("我的医生返回", response);
                        Type listtype = new TypeToken<LinkedList<MyDoctor>>() {
                        }.getType();
                        LinkedList<MyDoctor> leclist = gson.fromJson(response, listtype);
                        for (Iterator it = leclist.iterator(); it.hasNext(); ) {
                            MyDoctor inquiry = (MyDoctor) it.next();
                            list.add(inquiry);
                        }
                        adapter.notifyDataSetChanged();
                        rl_loading.setVisibility(View.GONE);
                    }
                });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:

                    break;
                case 1:

                    break;

            }
        }
    };

    private AdapterView.OnItemClickListener lvlistener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Intent intent = new Intent(MyDoctorActivity.this, DoctorparticularsActivity.class);
//            //医生ID
//            intent.putExtra("doctot_id", list.get(position).getDoctorId() + "");
//            startActivity(intent);
        }
    };


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}
