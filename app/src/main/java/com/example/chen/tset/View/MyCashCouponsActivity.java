package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.chen.tset.R;
import com.example.chen.tset.page.MyCashCouponsAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyCashCouponsActivity extends AppCompatActivity {
    private LinearLayout ll_rut;
    private ListView lv_mycash;
    MyCashCouponsAdapter adapter;
    List<String> list=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cash_coupons);
        findView();
        init();
    }



    private void findView() {
        //返回
        ll_rut= (LinearLayout) findViewById(R.id.ll_rut);

        lv_mycash= (ListView) findViewById(R.id.lv_mycash);

        ll_rut.setOnClickListener(listener);
    }

    private View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_rut:
                    finish();
                    break;
            }
        }
    };

    private void init() {
        list.add("");
        list.add("已使用");
        list.add("");
        adapter=new MyCashCouponsAdapter(this,list);
        lv_mycash.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
