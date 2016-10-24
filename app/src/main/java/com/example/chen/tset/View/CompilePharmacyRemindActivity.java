package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.Pharmacyremindinit;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.ListenerManager;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class CompilePharmacyRemindActivity extends AppCompatActivity {


    private TextView tv_startdate, tv_time1, tv_time2, tv_time3, tv_content1, tv_content2, tv_content3, tv_passet;

    private ScrollView scrollview;

    private String remindId;

    Pharmacyremindinit pharmacyremindinit;

    Gson gson = new Gson();

    private RelativeLayout rl_delete_pharmacy;

    private LinearLayout ll_time2, ll_time3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compile_pharmacy_remind);
        fidView();
        init();

    }

    private void fidView() {
        remindId = getIntent().getStringExtra("remindId");
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        scrollview.setVerticalScrollBarEnabled(false);
        tv_passet = (TextView) findViewById(R.id.tv_passet);
        tv_startdate = (TextView) findViewById(R.id.tv_startdate);

        tv_time1 = (TextView) findViewById(R.id.tv_time1);
        tv_content1 = (TextView) findViewById(R.id.tv_content1);
        tv_time2 = (TextView) findViewById(R.id.tv_time2);
        tv_content2 = (TextView) findViewById(R.id.tv_content2);
        tv_time3 = (TextView) findViewById(R.id.tv_time3);
        tv_content3 = (TextView) findViewById(R.id.tv_content3);
        rl_delete_pharmacy = (RelativeLayout) findViewById(R.id.rl_delete_pharmacy);
        ll_time2 = (LinearLayout) findViewById(R.id.ll_time2);
        ll_time3 = (LinearLayout) findViewById(R.id.ll_time3);


        tv_passet.setOnClickListener(listener);
        rl_delete_pharmacy.setOnClickListener(deletelistener);
    }


    private void init() {
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/FindRemindByid")
                .addParams("remindId", remindId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(CompilePharmacyRemindActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        pharmacyremindinit = gson.fromJson(response, Pharmacyremindinit.class);
                        tv_startdate.setText(pharmacyremindinit.getRemindDate());
                        tv_time1.setText(pharmacyremindinit.getTime1());
                        tv_time2.setText(pharmacyremindinit.getTime2());
                        tv_time3.setText(pharmacyremindinit.getTime3());
                        tv_content1.setText(pharmacyremindinit.getContent1());
                        tv_content2.setText(pharmacyremindinit.getContent2());
                        tv_content3.setText(pharmacyremindinit.getContent3());
                        //如果只有1条或2条健康记录则隐藏，空白的记录栏
                        if (pharmacyremindinit.getTime2() == null || pharmacyremindinit.getTime2().equals("")) {
                            ll_time2.setVisibility(View.GONE);
                        }

                        if (pharmacyremindinit.getTime3() == null || pharmacyremindinit.getTime3().equals("")) {
                            ll_time3.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private View.OnClickListener deletelistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            OkHttpUtils
                    .post()
                    .url(Http_data.http_data + "/DeleteRemindListByTimeStamp")
                    .addParams("timeStamp", pharmacyremindinit.getTimestamp())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Log.e("失败", "失败");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (response.equals("0")) {
                                //发送广播通知诊疗页面健康日历更新
                                ListenerManager.getInstance().sendBroadCast("更新日历页面");
                                Toast.makeText(CompilePharmacyRemindActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(CompilePharmacyRemindActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    };


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_passet:
                    finish();
                    break;

            }
        }
    };
}
