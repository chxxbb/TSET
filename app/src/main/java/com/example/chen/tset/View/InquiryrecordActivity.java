package com.example.chen.tset.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.chen.tset.Data.Inquiryrecord;
import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.InquiryrecordDao;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.example.chen.tset.page.InquiryrecordAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;

/**
 * 问诊历史
 */
public class InquiryrecordActivity extends MyBaseActivity {
    private ListView lv_inquiryrecord;
    private InquiryrecordAdapter adapter;
    private LinearLayout ll_rut;
    InquiryrecordDao db;
    List<Inquiryrecord> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiryrecord);
        list = new ArrayList<>();
        db = new InquiryrecordDao(this);
        findView();
        init();


    }


    private void findView() {
        lv_inquiryrecord = (ListView) findViewById(R.id.lv_inquiryrecord);
        ll_rut = (LinearLayout) findViewById(R.id.ll_rut);
        lv_inquiryrecord.setVerticalScrollBarEnabled(false);
        lv_inquiryrecord.setOnItemClickListener(lvlistener);
    }

    private void init() {
        list = db.chatfind(User_Http.user.getPhone());
        Log.e("231", list.toString());
        adapter = new InquiryrecordAdapter(this, list);
        lv_inquiryrecord.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private AdapterView.OnItemClickListener lvlistener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(InquiryrecordActivity.this, ChatpageActivity.class);
            intent.putExtra("name", list.get(position).getDoctorname());
            intent.putExtra("icon", list.get(position).getDoctoricon());
            intent.putExtra("doctorID", list.get(position).getId());
            intent.putExtra("username", list.get(position).getDoctorid());
            startActivity(intent);
        }
    };


}
