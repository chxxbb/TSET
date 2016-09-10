package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.chen.tset.R;
import com.example.chen.tset.page.InquiryrecordAdapter;

import java.util.ArrayList;
import java.util.List;
/**
 * 问诊历史
 */
public class InquiryrecordActivity extends AppCompatActivity {
    private ListView lv_inquiryrecord;
    private InquiryrecordAdapter adapter;
    private List<String> list;
    private LinearLayout ll_rut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiryrecord);
        findView();
        init();
    }


    private void findView() {
        lv_inquiryrecord = (ListView) findViewById(R.id.lv_inquiryrecord);
        ll_rut = (LinearLayout) findViewById(R.id.ll_rut);
        ll_rut.setOnClickListener(listener);
        lv_inquiryrecord.setVerticalScrollBarEnabled(false);
    }

    private void init() {
        list = new ArrayList<>();
        list.add("上午 9:00");
        list.add("上午 10:00");
        list.add("上午 11:00");
        list.add("上午 12:00");
        list.add("上午 13:00");
        list.add("上午 14:00");
        list.add("上午 15:00");
        list.add("上午 16:00");
        list.add("上午 17:00");
        list.add("上午 18:00");
        adapter = new InquiryrecordAdapter(this, list);
        lv_inquiryrecord.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

}
