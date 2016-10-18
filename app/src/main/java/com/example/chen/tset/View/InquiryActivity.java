package com.example.chen.tset.View;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.Inquiry;
import com.example.chen.tset.R;
import com.example.chen.tset.page.InquiryAdapter;
import com.example.chen.tset.page.InquirylistAdapter;
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

public class InquiryActivity extends AppCompatActivity {
    View view;
    InquiryAdapter adapter;
    private ListView lv_inquiry;
    //全部医生列表
    List<Inquiry> list;
    //科室列表
    List<Inquiry> selectlist;
    //城市列表
    List<Inquiry> citylist;
    //职称列表
    List<Inquiry> titlelist;
    List<String> data;
    private LinearLayout ll_city, ll_development, ll_sort;
    private Dialog setHeadDialog;
    private View dialogView;
    private RelativeLayout rl_nonetwork, rl_loading;
    Gson gson;
    InquirylistAdapter listadapter;
    private TextView tv_section, tv_city, tv_sort;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        list = new ArrayList<>();
        findView();
        listinit(list);
        httpinit();
    }

    private void findView() {
        lv_inquiry = (ListView) findViewById(R.id.lv_inquiry);
        ll_city = (LinearLayout) findViewById(R.id.ll_city);
        ll_development = (LinearLayout) findViewById(R.id.ll_development);
        ll_sort = (LinearLayout) findViewById(R.id.ll_sort);
        tv_section = (TextView) findViewById(R.id.tv_section);
        tv_city = (TextView) findViewById(R.id.tv_city);
        rl_nonetwork = (RelativeLayout) findViewById(R.id.rl_nonetwork);
        rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
        tv_sort = (TextView) findViewById(R.id.tv_sort);
        lv_inquiry.setVerticalScrollBarEnabled(false);
        ll_city.setOnClickListener(listener);
        ll_development.setOnClickListener(listener);
        ll_sort.setOnClickListener(listener);

        View view1 = View.inflate(this, R.layout.inquiry_listview_stern, null);

        lv_inquiry.addFooterView(view1);


        lv_inquiry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(InquiryActivity.this, DoctorparticularsActivity.class);
                //根据所选择的排序，点击时获取所点击医生ID
                if (tv_section.getText().toString().equals("全部科室") || tv_city.getText().toString().equals("全部地区") || tv_sort.getText().toString().equals("智能排序") || tv_sort.getText().toString().equals("默认排序")) {
                    intent.putExtra("doctot_id", list.get(position).getId());
                } else if (tv_city.getText().toString().equals("成都") || tv_city.getText().toString().equals("重庆")) {
                    intent.putExtra("doctot_id", citylist.get(position).getId());
                } else if (tv_sort.getText().toString().equals("职称排序")) {
                    intent.putExtra("doctot_id", titlelist.get(position).getId());
                } else {
                    intent.putExtra("doctot_id", selectlist.get(position).getId());
                }

                startActivity(intent);
            }
        });
    }


    private void listinit(List<Inquiry> list) {
        adapter = new InquiryAdapter(InquiryActivity.this, list);
        lv_inquiry.setAdapter(adapter);
    }

    private void httpinit() {
        gson = new Gson();
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/FindDoctorList")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        rl_loading.setVisibility(View.GONE);
                        rl_nonetwork.setVisibility(View.VISIBLE);
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
                        rl_loading.setVisibility(View.GONE);
                    }
                });
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_city:
                    //选择城市排序
                    cityshowDialog();
                    break;
                case R.id.ll_development:
                    //选择科室排序
                    developmentshowDialog();
                    break;
                case R.id.ll_sort:
                    //选择默认排序
                    sortshowDialog();
                    break;
            }

        }
    };

    private void sortshowDialog() {
//        setHeadDialog = new AlertDialog.Builder(getContext()).create();
        setHeadDialog = new Dialog(InquiryActivity.this, R.style.CustomDialog);
        setHeadDialog.show();
        dialogView = View.inflate(InquiryActivity.this, R.layout.inquiry_sort_dialog, null);
        setHeadDialog.getWindow().setContentView(dialogView);
        WindowManager.LayoutParams lp = setHeadDialog.getWindow()
                .getAttributes();
        setHeadDialog.getWindow().setAttributes(lp);
        sortdialogclick();
    }

    private void sortdialogclick() {
        titlelist = new ArrayList<>();
        Button btn_sortcancel = (Button) dialogView.findViewById(R.id.btn_sortcancel);
        Button btn_default = (Button) dialogView.findViewById(R.id.btn_default);
        Button btn_title = (Button) dialogView.findViewById(R.id.btn_title);
        RelativeLayout rl_inquiry_sort = (RelativeLayout) dialogView.findViewById(R.id.rl_inquiry_sort);

        rl_inquiry_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
            }
        });
        btn_sortcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
            }
        });
        btn_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listinit(list);
                tv_city.setText("全部地区");
                tv_sort.setText("职称排序");
                tv_sort.setText("默认排序");
                setHeadDialog.dismiss();
            }
        });
        btn_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_city.setText("全部地区");
                tv_sort.setText("职称排序");
                tv_section.setText("全部科室");
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getTitle().equals("主任医师")) {
                        titlelist.add(list.get(i));
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getTitle().equals("副主任医师")) {
                        titlelist.add(list.get(i));
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getTitle().equals("主治医师")) {
                        titlelist.add(list.get(i));
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getTitle().equals("执业医师")) {
                        titlelist.add(list.get(i));
                    }
                }
                listinit(titlelist);
                setHeadDialog.dismiss();
            }
        });
        lv_inquiry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(InquiryActivity.this, DoctorparticularsActivity.class);
                //根据所选择的排序，点击时获取所点击医生ID
                if (tv_sort.getText().toString().equals("智能排序") || tv_sort.getText().toString().equals("默认排序")) {
                    intent.putExtra("doctot_id", list.get(position).getId());
                } else if (tv_sort.getText().toString().equals("职称排序")) {
                    intent.putExtra("doctot_id", titlelist.get(position).getId());
                }
                startActivity(intent);
            }
        });


    }

    //科室弹出框
    private void developmentshowDialog() {
//        setHeadDialog = new AlertDialog.Builder(getContext()).create();
        setHeadDialog = new Dialog(InquiryActivity.this, R.style.CustomDialog);
        setHeadDialog.show();
        dialogView = View.inflate(InquiryActivity.this, R.layout.registration_dialog, null);
        ListView lv_registration = (ListView) dialogView.findViewById(R.id.lv_registration);
        data = new ArrayList<>();
        data.add("全部科室");
        data.add("行为发育");
        data.add("小儿神经");
        data.add("内分泌");
        data.add("儿童皮肤");
        data.add("耳鼻喉");
        data.add("小儿外科");
        data.add("眼科");
        data.add("儿童口腔");
        data.add("小儿呼吸");
        data.add("儿童保健");
        data.add("小儿消化");
        data.add("其他");
        lv_registration.setVerticalScrollBarEnabled(false);
        listadapter = new InquirylistAdapter(InquiryActivity.this, data);
        lv_registration.setAdapter(listadapter);
        listadapter.notifyDataSetChanged();
        setHeadDialog.getWindow().setContentView(dialogView);
        WindowManager.LayoutParams lp = setHeadDialog.getWindow().getAttributes();
        setHeadDialog.getWindow().setAttributes(lp);
        developmentdialogclick();
    }

    private void developmentdialogclick() {
        selectlist = new ArrayList<>();
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        RelativeLayout rl_regisration = (RelativeLayout) dialogView.findViewById(R.id.rl_regisration);
        rl_regisration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
            }
        });
        ListView lv_registration = (ListView) dialogView.findViewById(R.id.lv_registration);
        lv_registration.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_city.setText("全部地区");
                tv_sort.setText("默认排序");
                if (position == 0) {
                    listinit(list);
                    setHeadDialog.dismiss();
                    tv_section.setText("全部科室");
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        //在集合中查找所选科室的医生，放入一个新的集合中
                        if ((data.get(position)).equals(list.get(i).getSection())) {
                            selectlist.add(list.get(i));
                        }
                    }
                    if (selectlist.size() == 0) {
                        Toast.makeText(InquiryActivity.this, "没有此科室的医生", Toast.LENGTH_SHORT).show();
                    } else {
                        listinit(selectlist);
                    }
                    tv_section.setText(data.get(position));
                }
                setHeadDialog.dismiss();
            }
        });
        lv_inquiry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(InquiryActivity.this, DoctorparticularsActivity.class);
                //根据所选择的排序，点击时获取所点击医生ID
                if (tv_section.getText().toString().equals("全部科室")) {
                    intent.putExtra("doctot_id", list.get(position).getId());
                } else {
                    intent.putExtra("doctot_id", selectlist.get(position).getId());
                }

                startActivity(intent);
            }
        });

    }

    //城市弹出框
    public void cityshowDialog() {
        setHeadDialog = new Dialog(InquiryActivity.this, R.style.CustomDialog);
        setHeadDialog.show();
        dialogView = View.inflate(InquiryActivity.this, R.layout.inquiry_city_dialog, null);
        setHeadDialog.getWindow().setContentView(dialogView);
        WindowManager.LayoutParams lp = setHeadDialog.getWindow()
                .getAttributes();

        setHeadDialog.getWindow().setAttributes(lp);
        citydialogclick();
    }

    //城市点击事件
    private void citydialogclick() {
        citylist = new ArrayList<>();
        Button btn_region = (Button) dialogView.findViewById(R.id.btn_region);
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        Button btn_chnegdu = (Button) dialogView.findViewById(R.id.btn_chengdu);
        Button btn_shenzheng = (Button) dialogView.findViewById(R.id.btn_shenzheng);
        RelativeLayout rl = (RelativeLayout) dialogView.findViewById(R.id.rl);
        RelativeLayout rl_city = (RelativeLayout) dialogView.findViewById(R.id.rl_city);
        rl_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
            }
        });
        //在集合中查找所选城市的医生，放入一个新的集合中
        btn_region.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listinit(list);
                tv_sort.setText("默认排序");
                tv_section.setText("全部科室");
                tv_city.setText("全部地区");
                setHeadDialog.dismiss();
            }
        });
        btn_chnegdu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tv_sort.setText("默认排序");
                tv_section.setText("全部科室");
                tv_city.setText("成都");
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getHospital().equals("成都天使儿童医院")) {
                        citylist.add(list.get(i));
                    }
                }
                listinit(citylist);
                setHeadDialog.dismiss();
            }
        });
        btn_shenzheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_sort.setText("默认排序");
                tv_section.setText("全部科室");
                tv_city.setText("深圳");
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getHospital().equals("深圳天使儿童医院")) {
                        citylist.add(list.get(i));
                    }
                }
                listinit(citylist);
                setHeadDialog.dismiss();
            }
        });


        lv_inquiry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(InquiryActivity.this, DoctorparticularsActivity.class);
                //根据所选择的排序，点击时获取所点击医生ID
                if (tv_city.getText().toString().equals("全部地区")) {
                    intent.putExtra("doctot_id", list.get(position).getId());
                } else if (tv_city.getText().toString().equals("成都") || tv_city.getText().toString().equals("深圳")) {
                    intent.putExtra("doctot_id", citylist.get(position).getId());
                }

                startActivity(intent);
            }
        });
    }
}
