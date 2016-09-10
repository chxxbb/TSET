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
 * 医生详情页面，
 */
public class DoctorparticularsActivity extends AppCompatActivity {
    private ListView lv_docttorparticulas;
    private DoctorparticularsAdapter adapter;
    private LinearLayout ll_return;
    private View view;
    List<Doctorcomment> list;
    List<Doctor> list1;
    Gson gson = new Gson();
    private TextView tv_title, tv_name, tv_hospital, tv_bioo, tv_bis, tv_bit, tv_bif, tv_sum, tv_adept;
    private Button btn_chatmoney, btn_callmoney;
    private CircleImageView iv_icon;
    private RelativeLayout rl_nonetwork, rl_loading;
    String doctor_id=null;


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
        doctor_id=getIntent().getStringExtra("doctot_id");
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
        list1 = new ArrayList<>();
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/findDatabank")
                .addParams("id", doctor_id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(DoctorparticularsActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        rl_loading.setVisibility(View.GONE);
                        rl_nonetwork.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Type listtype = new TypeToken<LinkedList<Doctor>>() {
                        }.getType();
                        LinkedList<Doctor> leclist = gson.fromJson(response, listtype);
                        for (Iterator it = leclist.iterator(); it.hasNext(); ) {
                            Doctor doctor = (Doctor) it.next();
                            list1.add(doctor);
                        }
                        tv_name.setText(list1.get(0).getName());
                        tv_title.setText(list1.get(0).getTitle());
                        tv_hospital.setText(list1.get(0).getHospital());
                        btn_callmoney.setText("￥" + list1.get(0).getCallmoney() + "/10分钟");
                        btn_chatmoney.setText("￥" + list1.get(0).getChatmoney() + "/次");
                        ImageLoader.getInstance().displayImage(list1.get(0).getIcon(), iv_icon);
                        tv_bioo.setText(list1.get(0).getBioo());
                        tv_bis.setText(list1.get(0).getBis());
                        tv_bit.setText(list1.get(0).getBit());
                        tv_bif.setText(list1.get(0).getBif());
                        tv_sum.setText("用户评论 （" + list1.get(0).getSum() + "人）");
                        tv_adept.setText(list1.get(0).getAdept());
                        rl_loading.setVisibility(View.GONE);
                    }
                });
    }
    //医生评论
    private void comment() {
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/findUserComment")
                .addParams("doctor_id", doctor_id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
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
            switch (v.getId()){
                case R.id.btn_callmoney:
                    Intent intent=new Intent(DoctorparticularsActivity.this, EvaluatepageActivity.class);
                    intent.putExtra("doctorid",doctor_id);
                    startActivity(intent);
                    break;
                case R.id.ll_return:
                    finish();
                    break;


            }
        }
    };
}
