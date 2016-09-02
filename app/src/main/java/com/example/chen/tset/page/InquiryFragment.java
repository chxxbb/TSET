package com.example.chen.tset.page;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.Inquiry;
import com.example.chen.tset.Data.Lecture;
import com.example.chen.tset.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;


public class InquiryFragment extends Fragment {
    View view;
    InquiryAdapter adapter;
    private ListView lv_inquiry;
    List<Inquiry> list;
    private LinearLayout ll_city;
    private Dialog setHeadDialog;
    private View dialogView;
    Gson gson;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inquiry, null);
        findView();
        init();
        httpinit();
        return view;
    }


    private void findView() {
        lv_inquiry = (ListView) view.findViewById(R.id.lv_inquiry);
        ll_city = (LinearLayout) view.findViewById(R.id.ll_city);
        lv_inquiry.setVerticalScrollBarEnabled(false);
        ll_city.setOnClickListener(listener);
    }

    private void init() {
        list = new ArrayList<>();
        adapter = new InquiryAdapter(getContext(), list);
        lv_inquiry.setAdapter(adapter);
    }

    private void httpinit() {
        gson = new Gson();
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/findInquiryList")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("问诊返回", response);
                        Type listtype = new TypeToken<LinkedList<Inquiry>>() {
                        }.getType();
                        LinkedList<Inquiry> leclist = gson.fromJson(response, listtype);
                        for (Iterator it = leclist.iterator(); it.hasNext(); ) {
                            Inquiry inquiry = (Inquiry) it.next();
                            list.add(inquiry);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
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
