package com.example.chen.tset.View.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.chen.tset.Data.entity.Pharmacyremind;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.example.chen.tset.page.adapter.PharmacyremindAdapter;

import java.util.List;


/**
 * 用药提醒页面，已废除
 */
public class PharmacyremindActivity extends MyBaseActivity {
    private LinearLayout ll_return;
    PharmacyremindAdapter adapter;
    List<Pharmacyremind> list;
    SharedPsaveuser sp;
    ListView lv_pharmacy_remind;
    private String remindId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacyremind);
        findView();


    }


    private void findView() {
        remindId = getIntent().getStringExtra("remindId");
        ll_return = (LinearLayout) findViewById(R.id.ll_return);
        lv_pharmacy_remind = (ListView) findViewById(R.id.lv_pharmacy_remind);
        lv_pharmacy_remind.setVerticalScrollBarEnabled(false);
        ll_return.setOnClickListener(listener);

    }




    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_return:
                    finish();
                    break;
            }
        }
    };


}
