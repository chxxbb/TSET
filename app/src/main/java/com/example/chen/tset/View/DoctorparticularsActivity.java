package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.chen.tset.R;
import com.example.chen.tset.page.DoctorparticularsAdapter;

import java.util.ArrayList;
import java.util.List;

public class DoctorparticularsActivity extends AppCompatActivity {
    private ListView lv_docttorparticulas;
    private DoctorparticularsAdapter adapter;
    private View view;
    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorparticulars);
        findView();
    }

    private void findView() {
        lv_docttorparticulas = (ListView) findViewById(R.id.lv_docttorparticulas);
        view = View.inflate(this, R.layout.doctorparticulars_listv_headerview, null);
        lv_docttorparticulas.addHeaderView(view);
        list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        adapter = new DoctorparticularsAdapter(this, list);
        lv_docttorparticulas.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
