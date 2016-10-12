package com.example.chen.tset.View;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.chen.tset.Data.Pharmacyremind;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.example.chen.tset.Utils.PharmacyDao;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.example.chen.tset.page.PharmacyremindAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * 用药提醒页面
 */
public class PharmacyremindActivity extends MyBaseActivity {
    private LinearLayout ll_return;

    PharmacyremindAdapter adapter;
    List<Pharmacyremind> list;
    PharmacyDao db;
    SharedPsaveuser sp;
    ListView lv_pharmacy_remind;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacyremind);
        findView();
        init();

    }


    private void findView() {
        ll_return = (LinearLayout) findViewById(R.id.ll_return);
        lv_pharmacy_remind = (ListView) findViewById(R.id.lv_pharmacy_remind);
        lv_pharmacy_remind.setVerticalScrollBarEnabled(false);
        ll_return.setOnClickListener(listener);


    }

    private void init() {
        sp = new SharedPsaveuser(this);
        list = new ArrayList<>();
        db = new PharmacyDao(this);
        List<Pharmacyremind> plist = db.chatfind(sp.getTag().getPhone());
        if (plist.size() != 0) {
            list.addAll(plist);
            adapter = new PharmacyremindAdapter(this, list);
            lv_pharmacy_remind.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }


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
