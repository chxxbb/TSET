package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.chen.tset.R;
import com.example.chen.tset.page.CharactersafeAdapter;

import java.util.ArrayList;
import java.util.List;

public class MycollectActivity extends AppCompatActivity {
    private ListView lv_collect;
    CharactersafeAdapter adapter;
    List<String> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycollect);
        findView();
        init();
    }

    private void findView() {
        lv_collect= (ListView) findViewById(R.id.lv_collect);
    }
    private void init() {
        list=new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        adapter=new CharactersafeAdapter(this,list);
        lv_collect.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
