package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.chen.tset.R;
import com.example.chen.tset.page.PharmacyremindAdapter;

import java.util.ArrayList;
import java.util.List;

public class PharmacyremindActivity extends AppCompatActivity {
    private ListView lv_pharmacy_remind;
    PharmacyremindAdapter adapter;
    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacyremind);
        findView();
    }

    private void findView() {
        lv_pharmacy_remind= (ListView) findViewById(R.id.lv_pharmacy_remind);
        list=new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        adapter=new PharmacyremindAdapter(this,list);
        lv_pharmacy_remind.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lv_pharmacy_remind.setVerticalScrollBarEnabled(false);

    }
}
