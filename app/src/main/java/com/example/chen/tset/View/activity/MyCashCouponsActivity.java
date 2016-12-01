package com.example.chen.tset.View.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chen.tset.R;
import com.example.chen.tset.Utils.IListener;
import com.example.chen.tset.Utils.ListenerManager;
import com.example.chen.tset.page.adapter.MyCashCouponsAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyCashCouponsActivity extends AppCompatActivity implements IListener {
    private LinearLayout ll_rut;
    private ListView lv_mycash;
    MyCashCouponsAdapter adapter;
    List<String> list = new ArrayList<>();
    String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cash_coupons);

        try {
            ListenerManager.getInstance().registerListtener(this);
            type = getIntent().getStringExtra("type");
            findView();
            init();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void findView() {
        //返回
        ll_rut = (LinearLayout) findViewById(R.id.ll_rut);

        lv_mycash = (ListView) findViewById(R.id.lv_mycash);

        ll_rut.setOnClickListener(listener);
        try {
            lv_mycash.setOnItemClickListener(lvlistener);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_rut:
                    finish();
                    break;
            }
        }
    };


    private AdapterView.OnItemClickListener lvlistener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (type.equals("pay")) {
                finish();
            } else {

            }
        }
    };

    private void init() {
        list.add("");
        adapter = new MyCashCouponsAdapter(this, list);
        lv_mycash.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void notifyAllActivity(String str) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ListenerManager.getInstance().unRegisterListener(this);
    }
}
