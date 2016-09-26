package com.example.chen.tset.View;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.Doctor;
import com.example.chen.tset.Data.Doctorcomment;
import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.Information;
import com.example.chen.tset.R;
import com.example.chen.tset.page.DoctorparticularsAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * 医生详情页面，与我的医生详情，问诊详情共用同一个接口
 */
public class DoctorparticularsActivity extends AppCompatActivity {
    private ListView lv_docttorparticulas;
    private DoctorparticularsAdapter adapter;
    private LinearLayout ll_return;
    private View view;
    List<Doctorcomment> list;
    List<Doctor> list1;
    Gson gson = new Gson();
    private TextView tv_title, tv_name, tv_hospital, tv_bioo, tv_bis, tv_bit, tv_bif, tv_sum, tv_adept, tv_grade;
    private Button btn_chatmoney, btn_callmoney;
    private CircleImageView iv_icon;
    private RelativeLayout rl_nonetwork, rl_loading;
    String doctor_id = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorparticulars);
        findView();
        httpinit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        comment();
    }

    private void findView() {
        doctor_id = getIntent().getStringExtra("doctot_id");
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_name = (TextView) findViewById(R.id.tv_name);
        btn_chatmoney = (Button) findViewById(R.id.btn_chatmoney);
        btn_callmoney = (Button) findViewById(R.id.btn_callmoney);
        tv_hospital = (TextView) findViewById(R.id.tv_hospital);
        iv_icon = (CircleImageView) findViewById(R.id.iv_icon);
        lv_docttorparticulas = (ListView) findViewById(R.id.lv_docttorparticulas);
        ll_return = (LinearLayout) findViewById(R.id.ll_return);
        //添加listview头部
        view = View.inflate(this, R.layout.doctorparticulars_listv_headerview, null);
        lv_docttorparticulas.addHeaderView(view);
        tv_bif = (TextView) view.findViewById(R.id.tv_bif);
        tv_bioo = (TextView) view.findViewById(R.id.tv_bioo);
        tv_bit = (TextView) view.findViewById(R.id.tv_bit);
        tv_bis = (TextView) view.findViewById(R.id.tv_bis);
        tv_sum = (TextView) view.findViewById(R.id.tv_sum);
        tv_grade = (TextView) view.findViewById(R.id.tv_grade);
        tv_adept = (TextView) view.findViewById(R.id.tv_adept);
        rl_nonetwork = (RelativeLayout) findViewById(R.id.rl_nonetwork);
        rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
        lv_docttorparticulas.setVerticalScrollBarEnabled(false);
        list = new ArrayList<>();
        adapter = new DoctorparticularsAdapter(this, list);
        lv_docttorparticulas.setAdapter(adapter);
        ll_return.setOnClickListener(listener);
        btn_callmoney.setOnClickListener(listener);

    }

    //医生信息
    private void httpinit() {
        Log.e("医生ID", doctor_id);
        list1 = new ArrayList<>();
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/FindDoctorById")
                .addParams("doctorId", doctor_id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(DoctorparticularsActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        rl_loading.setVisibility(View.GONE);
                        rl_nonetwork.setVisibility(View.VISIBLE);
                        view.setVisibility(View.GONE);
                        lv_docttorparticulas.setBackgroundColor(android.graphics.Color.parseColor("#ffffff"));
                        list = null;
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("医生详情返回", response);

                        Doctor doctor = gson.fromJson(response, Doctor.class);
                        tv_name.setText(doctor.getName());
                        tv_title.setText(doctor.getTitle());
                        tv_hospital.setText(doctor.getHospital());
                        btn_callmoney.setText("￥" + doctor.getCallCost() + "/10分钟");
                        btn_chatmoney.setText("￥" + doctor.getChatCost() + "/次");
                        ImageLoader.getInstance().displayImage(doctor.getIcon(), iv_icon);
                        tv_bioo.setText(doctor.getSeniority1());
                        tv_bis.setText(doctor.getSeniority2());
                        tv_bit.setText(doctor.getSeniority3());
                        tv_bif.setText(doctor.getSeniority4());
                        tv_sum.setText("用户评论 （" + doctor.getCommentCount() + "人）");
                        tv_adept.setText(doctor.getAdept());
                        rl_loading.setVisibility(View.GONE);
                    }
                });
    }

    //医生评论
    private void comment() {
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/FindDoctorCommentByDoctorId")
                .addParams("doctorId", doctor_id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        lv_docttorparticulas.setBackgroundColor(android.graphics.Color.parseColor("#ffffff"));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("医生详情评论返回", response);
                        Type listtype = new TypeToken<LinkedList<Doctorcomment>>() {
                        }.getType();
                        LinkedList<Doctorcomment> leclist = gson.fromJson(response, listtype);
                        for (Iterator it = leclist.iterator(); it.hasNext(); ) {
                            Doctorcomment doctorcomment = (Doctorcomment) it.next();
                            list.add(doctorcomment);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            switch (v.getId()) {
                case R.id.btn_callmoney:
                    Intent intent = new Intent(DoctorparticularsActivity.this, EvaluatepageActivity.class);
                    intent.putExtra("doctorid", doctor_id);
                    startActivity(intent);
                    break;
                case R.id.ll_return:
                    finish();
                    break;


            }
        }
    };
}
