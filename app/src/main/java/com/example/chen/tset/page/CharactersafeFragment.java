package com.example.chen.tset.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.chen.tset.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class CharactersafeFragment extends Fragment {
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
}
