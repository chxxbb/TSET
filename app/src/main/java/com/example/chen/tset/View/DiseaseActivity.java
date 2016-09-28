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
import com.example.chen.tset.Utils.MyBaseActivity;
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

/**
 * 疾病详情页面
 */
public class DiseaseActivity extends MyBaseActivity {
    private LinearLayout ll_return;
    private ScrollView scrollView;
    private TextView tv_content, tv_acontent, tv_acontent1, tv_title, tv_title1, tv_bcontent, tv_dcontent, tv_dname, tv_uname, tv_section, tv_ucontent;
    private CircleImageView iv_icon, iv_dicon;
    private RelativeLayout rl_nonetwork, rl_loading;
    Gson gson = new Gson();
    Disease disease;
    String disease1;

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
      disease1 = getIntent().getStringExtra("disease");
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/FindDiseaseByName")
                        .addParams("diseaseName", disease1)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                                handler.sendEmptyMessage(0);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.e("疾病详情返回", response);
                                if (response.equals("[]")) {
                                    handler.sendEmptyMessage(1);
                                } else {
                                    disease=gson.fromJson(response,Disease.class);
                                    handler.sendEmptyMessage(2);




                                }
                            }
                        });
            }
        }).start();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(DiseaseActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    rl_loading.setVisibility(View.GONE);
                    rl_nonetwork.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    Toast.makeText(DiseaseActivity.this, "没有这个疾病", Toast.LENGTH_SHORT).show();
                    rl_loading.setVisibility(View.GONE);
                    rl_nonetwork.setVisibility(View.VISIBLE);
                    break;

                case 2:

                    tv_content.setText("        " + disease.getBio());
                    tv_acontent.setText("        " + disease.getCure());
                    if (disease.getPrompt() == null) {
                        tv_acontent1.setText("无");
                    } else {
                        tv_acontent1.setText("        " + disease.getPrompt());
                    }
                    tv_title.setText(disease1);
                    tv_title1.setText(disease1);
                    tv_bcontent.setText("        " + disease.getSymptom());
                    tv_dcontent.setText("        " + disease.getDoctorAnswerQuestion());
                    tv_dname.setText(disease.getDoctorName());
                    tv_uname.setText(disease.getUserName());
                    tv_section.setText(disease.getSectionName());
                    tv_ucontent.setText("        " + disease.getUserPutQuestion());
                    ImageLoader.getInstance().displayImage(disease.getUserIcon(), iv_icon);
                    ImageLoader.getInstance().displayImage(disease.getDoctorIcon(), iv_dicon);
                    rl_loading.setVisibility(View.GONE);
                    break;


            }
        }
    };
}
