package com.example.chen.tset.View;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.Consultparticulars;
import com.example.chen.tset.Data.Disease;
import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

public class DiseaseActivity extends AppCompatActivity {
    private LinearLayout ll_return;
    private ScrollView scrollView;
    private TextView tv_content, tv_acontent, tv_acontent1, tv_title, tv_title1, tv_bcontent, tv_dcontent, tv_dname, tv_uname, tv_section, tv_ucontent;
    private CircleImageView iv_icon, iv_dicon;
    private RelativeLayout rl_nonetwork, rl_loading;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease);
        findView();
        httpinit();
    }

    private void findView() {
        ll_return = (LinearLayout) findViewById(R.id.ll_return);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_acontent = (TextView) findViewById(R.id.tv_acontent);
        tv_acontent1 = (TextView) findViewById(R.id.tv_acontent1);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title1 = (TextView) findViewById(R.id.tv_title1);
        tv_bcontent = (TextView) findViewById(R.id.tv_bcontent);
        tv_dcontent = (TextView) findViewById(R.id.tv_dcontent);
        tv_dname = (TextView) findViewById(R.id.tv_dname);
        tv_uname = (TextView) findViewById(R.id.tv_uname);
        tv_section = (TextView) findViewById(R.id.tv_section);
        tv_ucontent = (TextView) findViewById(R.id.tv_ucontent);
        iv_icon = (CircleImageView) findViewById(R.id.iv_icon);
        iv_dicon = (CircleImageView) findViewById(R.id.iv_dicon);
        rl_nonetwork = (RelativeLayout) findViewById(R.id.rl_nonetwork);
        rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
        scrollView.setVerticalScrollBarEnabled(false);
        ll_return.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };


    private void httpinit() {
        final String disease1=getIntent().getStringExtra("disease");
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/findIntroduction")
                .addParams("title", disease1)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(DiseaseActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        rl_loading.setVisibility(View.GONE);
                        rl_nonetwork.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("疾病详情返回", response);
                        if (response.equals("[]")){
                            Toast.makeText(DiseaseActivity.this, "没有这个疾病", Toast.LENGTH_SHORT).show();
                            rl_loading.setVisibility(View.GONE);
                            rl_nonetwork.setVisibility(View.VISIBLE);
                        }else {


                        Type listtype = new TypeToken<LinkedList<Disease>>() {
                        }.getType();
                        LinkedList<Disease> leclist = gson.fromJson(response, listtype);
                        Iterator it = leclist.iterator();
                        Disease disease = (Disease) it.next();
                        tv_content.setText(disease.getContent());
                        tv_acontent.setText(disease.getAcontent());
                        tv_acontent1.setText(disease.getAcontent());
                        tv_title.setText(disease1);
                        tv_title1.setText(disease1);
                        tv_bcontent.setText(disease.getBcontent());
                        tv_dcontent.setText(disease.getDcontent());
                        tv_dname.setText(disease.getDname());
                        tv_uname.setText(disease.getUname());
                        tv_section.setText(disease.getSection());
                        tv_ucontent.setText(disease.getUcontent());
                        ImageLoader.getInstance().displayImage(disease.getIcon(), iv_icon);
                        ImageLoader.getInstance().displayImage(disease.getDicon(), iv_dicon);
                        rl_loading.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
