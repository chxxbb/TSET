package com.example.chen.tset.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.chen.tset.R;
import com.example.chen.tset.page.ReservationlistvAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReservationActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout ll_reservationreturn;
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
        ll_reservationreturn= (LinearLayout) findViewById(R.id.ll_reservationreturn);
        ll_reservationreturn.setOnClickListener(this);
        lv_reser.setOnItemClickListener(listener);
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

    @Override
    public void onClick(View v) {
        finish();
    }
    private AdapterView.OnItemClickListener listener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent(ReservationActivity.this,ReservationlistActivity.class);
            startActivity(intent);
        }
    };
}
