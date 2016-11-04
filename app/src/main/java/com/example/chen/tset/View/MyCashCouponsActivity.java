package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chen.tset.R;
import com.example.chen.tset.Utils.IListener;
import com.example.chen.tset.Utils.ListenerManager;
import com.example.chen.tset.page.MyCashCouponsAdapter;

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
        ListenerManager.getInstance().registerListtener(this);
        type = getIntent().getStringExtra("type");
        findView();
        init();
    }


    private void findView() {
        //返回
        ll_rut = (LinearLayout) findViewById(R.id.ll_rut);

        lv_mycash = (ListView) findViewById(R.id.lv_mycash);

        ll_rut.setOnClickListener(listener);

        lv_mycash.setOnItemClickListener(lvlistener);
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
            if (type.equals("mypage")) {

            } else if (type.equals("registration")) {
                if (position == 0) {
                    Toast.makeText(MyCashCouponsActivity.this, "不可使用", Toast.LENGTH_SHORT).show();
                } else if (position == 1) {
                    Toast.makeText(MyCashCouponsActivity.this, "已经使用过了", Toast.LENGTH_SHORT).show();
                } else if (position == 2) {
                    ListenerManager.getInstance().sendBroadCast("更新支付弹出框");
                    finish();

                }

            } else {
                if (position == 0) {
                    finish();
                    if (type.equals("inquiry")) {
                        ListenerManager.getInstance().sendBroadCast("更新问诊支付弹出框");
                    } else if (type.equals("HomeDoctorRecommend")) {
                        ListenerManager.getInstance().sendBroadCast("更新首页问诊支付弹出框");
                    } else if (type.equals("disease")) {
                        ListenerManager.getInstance().sendBroadCast("更新疾病详情问诊支付弹出框");
                    }


                } else if (position == 1) {
                    Toast.makeText(MyCashCouponsActivity.this, "已经使用过了", Toast.LENGTH_SHORT).show();
                } else if (position == 2) {
                    Toast.makeText(MyCashCouponsActivity.this, "不可使用", Toast.LENGTH_SHORT).show();

                }
            }
        }
    };

    private void init() {
        list.add("");
        list.add("已使用");
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
