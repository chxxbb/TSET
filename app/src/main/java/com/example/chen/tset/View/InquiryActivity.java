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
import com.zxl.library.DropDownMenu;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;

/**
 * 在线问诊页面
 */
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
    private LinearLayout ll_rutre_inquiry;
    //    ll_city, ll_development, ll_sort,
    private Dialog setHeadDialog;
    private View dialogView;
    private RelativeLayout rl_nonetwork, rl_loading;
    Gson gson;
    InquirylistAdapter listadapter;
    private TextView tv_section, tv_city, tv_sort;
    private DropDownMenu dropDownMenu;
    private String headers[] = {"全部地区", "全部科室", "智能排序"};
    View contentView;

    String tag = null;


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
        citylist = new ArrayList<>();
        contentView = getLayoutInflater().inflate(R.layout.contentview, null);
        lv_inquiry = (ListView) contentView.findViewById(R.id.lv_inquiry);

        tv_section = (TextView) findViewById(R.id.tv_section);
        tv_city = (TextView) findViewById(R.id.tv_city);
        rl_nonetwork = (RelativeLayout) findViewById(R.id.rl_nonetwork);
        rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
        ll_rutre_inquiry = (LinearLayout) findViewById(R.id.ll_rutre_inquiry);
        dropDownMenu = (DropDownMenu) findViewById(R.id.dropDownMenu);
        lv_inquiry.setVerticalScrollBarEnabled(false);
        ll_rutre_inquiry.setOnClickListener(listener);
        View view1 = View.inflate(this, R.layout.inquiry_listview_stern, null);

        dropDownMenu.setDropDownMenu(Arrays.asList(headers), initViewData(), contentView);


        lv_inquiry.addFooterView(view1);


        lv_inquiry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                if (list.size() != 0) {
//                    Intent intent = new Intent(InquiryActivity.this, DoctorparticularsActivity.class);
//                    //根据所选择的排序，点击时获取所点击医生ID，跳转到医生详情页面，跳转到聊天页面写在adapter中
//                    if (tv_section.getText().toString().equals("全部科室") || tv_city.getText().toString().equals("全部地区") || tv_sort.getText().toString().equals("智能排序") || tv_sort.getText().toString().equals("默认排序")) {
//                        intent.putExtra("doctot_id", list.get(position).getId());
//                    } else if (tv_city.getText().toString().equals("成都") || tv_city.getText().toString().equals("重庆")) {
//                        intent.putExtra("doctot_id", citylist.get(position).getId());
//                    } else if (tv_sort.getText().toString().equals("职称排序")) {
//                        intent.putExtra("doctot_id", titlelist.get(position).getId());
//                    } else {
//                        intent.putExtra("doctot_id", selectlist.get(position).getId());
//                    }
//
//                    startActivity(intent);
//                }

                Intent intent = new Intent(InquiryActivity.this, DoctorparticularsActivity.class);
                //根据所选择的排序，点击时获取所点击医生ID
                if (tag.equals("全部地区") || tag == null || tag.equals("")) {
                    intent.putExtra("doctot_id", list.get(position).getId());
                } else if (tag.equals("成都") || tag.equals("深圳")) {
                    intent.putExtra("doctot_id", citylist.get(position).getId());
                }

                startActivity(intent);

            }
        });
    }

    private List<HashMap<String, Object>> initViewData() {
        List<HashMap<String, Object>> viewDatas = new ArrayList<>();
        HashMap<String, Object> map;
        for (int i = 0; i < headers.length; i++) {
            map = new HashMap<String, Object>();
            map.put(DropDownMenu.KEY, DropDownMenu.TYPE_CUSTOM);

            if (i == 0) {
                map.put(DropDownMenu.VALUE, citydialog());
                map.put(DropDownMenu.SELECT_POSITION, 0);
            } else if (i == 1) {
                map.put(DropDownMenu.VALUE, citydialog());
                map.put(DropDownMenu.SELECT_POSITION, 0);
            } else {
                map.put(DropDownMenu.VALUE, defaultsort());
                map.put(DropDownMenu.SELECT_POSITION, 0);
            }

            viewDatas.add(map);
        }
        return viewDatas;
    }

    //设置智能排序及点击事件
    private View defaultsort() {
        View v = View.inflate(InquiryActivity.this, R.layout.inquiry_sort_dialog, null);
        //智能排序
        LinearLayout ll_capacity_sort = (LinearLayout) v.findViewById(R.id.ll_capacity_sort);
        //职称排序
        LinearLayout ll_title_sort = (LinearLayout) v.findViewById(R.id.ll_title_sort);

        ll_capacity_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropDownMenu.setTabText(0, "全部地区");//设置tab标签文字
                dropDownMenu.closeMenu();//关闭menu
                dropDownMenu.setTabText(1, "全部科室");
                dropDownMenu.setTabText(2, "智能排序");
                tag = "智能排序";
                listinit(list);
            }
        });

        ll_title_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropDownMenu.setTabText(0, "全部地区");//设置tab标签文字
                dropDownMenu.closeMenu();//关闭menu
                dropDownMenu.setTabText(1, "全部科室");
                dropDownMenu.setTabText(2, "职称排序");
            }
        });
        return v;
    }


    //设定地区排序菜单及点击事件
    private View citydialog() {
        View v = View.inflate(InquiryActivity.this, R.layout.inquiry_city_dialog, null);
        //全部地区
        LinearLayout ll_all_areas = (LinearLayout) v.findViewById(R.id.ll_all_areas);
        //深圳
        LinearLayout ll_city_shenzheng = (LinearLayout) v.findViewById(R.id.ll_city_shenzheng);
        //成都
        LinearLayout ll_city_chengdu = (LinearLayout) v.findViewById(R.id.ll_city_chengdu);

        ll_all_areas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropDownMenu.setTabText(0, "全部地区");//设置tab标签文字
                dropDownMenu.closeMenu();//关闭menu
                dropDownMenu.setTabText(1, "全部科室");
                dropDownMenu.setTabText(2, "智能排序");
                tag = "全部地区";
                listinit(list);
            }
        });

        ll_city_shenzheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropDownMenu.setTabText(0, "深圳");//设置tab标签文字
                dropDownMenu.setTabText(1, "全部科室");
                dropDownMenu.setTabText(2, "智能排序");
                dropDownMenu.closeMenu();//关闭menu
                tag = "深圳";
                citylist.clear();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getHospital().equals("深圳天使儿童医院")) {
                        citylist.add(list.get(i));
                    }
                }
                listinit(citylist);
            }
        });

        ll_city_chengdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropDownMenu.setTabText(0, "成都");//设置tab标签文字
                dropDownMenu.setTabText(1, "全部科室");
                dropDownMenu.setTabText(2, "智能排序");
                dropDownMenu.closeMenu();//关闭menu
                tag = "成都";
                citylist.clear();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getHospital().equals("成都天使儿童医院")) {
                        citylist.add(list.get(i));
                    }
                }
                listinit(citylist);
            }
        });


        return v;
    }


    @Override
    public void onBackPressed() {
        //退出activity前关闭菜单
        if (dropDownMenu.isShowing()) {
            dropDownMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
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
                case R.id.ll_rutre_inquiry:
                    finish();
                    break;
            }

        }
    };

    //默认排序
    private void sortshowDialog() {
//        setHeadDialog = new AlertDialog.Builder(getContext()).create();
        //设置弹出框主题
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


}
