package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.chen.tset.R;
import com.example.chen.tset.page.ReservationlistvAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReservationActivity extends AppCompatActivity {

    ReservationlistvAdapter adapter;
    private ListView lv_reser;
    List<String> list;
    //233

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservationactivity);
        findView();
        init();
    }

    private void findView() {
        lv_reser = (ListView) findViewById(R.id.lv_reser);
    }

    private void init() {
        list = new ArrayList<>();
        list.add("12324");
        list.add("12324");
        list.add("12324");
        list.add("12324");
        list.add("12324");
        list.add("12324");
        list.add("12324");
        list.add("12324");
        adapter = new ReservationlistvAdapter(this, list);
        lv_reser.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
