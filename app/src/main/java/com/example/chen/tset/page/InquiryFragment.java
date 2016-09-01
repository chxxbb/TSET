package com.example.chen.tset.page;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.chen.tset.R;

import java.util.ArrayList;
import java.util.List;


public class InquiryFragment extends Fragment {
    View view;
    InquiryAdapter adapter;
    private ListView lv_inquiry;
    List<String> list;
    private LinearLayout ll_city;
    private Dialog setHeadDialog;
    private View dialogView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inquiry, null);
        findView();
        init();
        return view;
    }

    private void findView() {
        lv_inquiry = (ListView) view.findViewById(R.id.lv_inquiry);
        ll_city = (LinearLayout) view.findViewById(R.id.ll_city);
        ll_city.setOnClickListener(listener);
    }

    private void init() {
        list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        adapter = new InquiryAdapter(getContext(), list);
        lv_inquiry.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog();
        }
    };

    public void showDialog() {
        setHeadDialog = new AlertDialog.Builder(getContext()).create();
        setHeadDialog.show();
        dialogView = View.inflate(getContext(), R.layout.registration_city_case, null);
        setHeadDialog.getWindow().setContentView(dialogView);
        WindowManager.LayoutParams lp = setHeadDialog.getWindow()
                .getAttributes();
        setHeadDialog.getWindow().setAttributes(lp);
    }
}
