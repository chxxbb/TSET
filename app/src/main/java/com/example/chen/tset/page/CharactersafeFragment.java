package com.example.chen.tset.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.Lecture;
import com.example.chen.tset.R;
import com.example.chen.tset.View.ConsultPageActivity;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class CharactersafeFragment extends Fragment{
    View view;
    CharactersafeAdapter adapter;
    private ListView lv_charactersafe;
    List<String> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_charactersafe, null);
        findView();
        init();
        return view;
    }

    private void findView() {
        lv_charactersafe = (ListView) view.findViewById(R.id.lv_charactersafe);
        lv_charactersafe.setOnItemClickListener(listener);
    }

    private void init() {
        list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        adapter = new CharactersafeAdapter(getContext(), list);
        lv_charactersafe.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private AdapterView.OnItemClickListener listener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent(getContext(), ConsultPageActivity.class);
            startActivity(intent);
        }
    };

}
