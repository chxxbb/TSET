package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.chen.tset.R;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.example.chen.tset.page.InquiryrecordAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;

/**
 * 问诊历史
 */
public class InquiryrecordActivity extends MyBaseActivity {
    private ListView lv_inquiryrecord;
    private InquiryrecordAdapter adapter;
    private List<String> list;
    private LinearLayout ll_rut;
    List<Conversation> clist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiryrecord);

        clist=JMessageClient.getConversationList();
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
        if(list.size()==0){

        }else {
            for(int i=0;i<clist.size();i++){
                Date d = new Date(clist.get(i).getLastMsgDate());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                list.add(sdf.format(d));
            }
            adapter = new InquiryrecordAdapter(this, list);
            lv_inquiryrecord.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

}
