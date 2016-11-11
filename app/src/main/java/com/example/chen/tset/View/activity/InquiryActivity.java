package com.example.chen.tset.View.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.entity.Inquiry;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.IListener;
import com.example.chen.tset.Utils.ListenerManager;
import com.example.chen.tset.page.adapter.InquiryAdapter;
import com.example.chen.tset.page.adapter.InquirySecletAdapter;
import com.example.chen.tset.page.adapter.InquirylistAdapter;
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
public class InquiryActivity extends AppCompatActivity implements IListener {
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

    private RelativeLayout rl_nonetwork, rl_loading;
    Gson gson;
    InquirylistAdapter listadapter;

    private DropDownMenu dropDownMenu;
    private String headers[] = {"全部地区", "全部科室", "智能排序"};
    View contentView;

    //用户所点击的选项标签
    String tag = "默认";
    InquirySecletAdapter secletAdapter;


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
        //城市的集合
        citylist = new ArrayList<>();
        //职称排序，智能排序集合
        titlelist = new ArrayList<>();
        //科室排序
        selectlist = new ArrayList<>();
        contentView = getLayoutInflater().inflate(R.layout.contentview, null);
        lv_inquiry = (ListView) contentView.findViewById(R.id.lv_inquiry);

        rl_nonetwork = (RelativeLayout) findViewById(R.id.rl_nonetwork);
        rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
        ll_rutre_inquiry = (LinearLayout) findViewById(R.id.ll_rutre_inquiry);
        dropDownMenu = (DropDownMenu) findViewById(R.id.dropDownMenu);
        //去除listview滚动条
        lv_inquiry.setVerticalScrollBarEnabled(false);
        ll_rutre_inquiry.setOnClickListener(listener);

        //listview头部布局,
        View view1 = View.inflate(this, R.layout.inquiry_listview_stern, null);

        //设置二级菜单内容体（listview，listview没有放在布局中，放在了二级菜单内容体重）
        dropDownMenu.setDropDownMenu(Arrays.asList(headers), initViewData(), contentView);


        //点击进入医生详情页面
        lv_inquiry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(InquiryActivity.this, DoctorparticularsActivity.class);
                //根据所选择的排序，点击时获取所点击医生ID
                if (tag.equals("全部地区") || tag == null || tag.equals("") || tag.equals("智能排序") || tag.equals("全部科室") || tag.equals("默认")) {
                    intent.putExtra("doctot_id", list.get(position).getId());
                } else if (tag.equals("成都") || tag.equals("深圳")) {
                    intent.putExtra("doctot_id", citylist.get(position).getId());
                } else if (tag.equals("职称排序")) {
                    intent.putExtra("doctot_id", titlelist.get(position).getId());
                } else {
                    //根据科室排序点击
                    intent.putExtra("doctot_id", selectlist.get(position).getId());
                }

                startActivity(intent);

            }
        });
    }


    //设置不同样式的二级菜单，点击菜单
    private List<HashMap<String, Object>> initViewData() {
        List<HashMap<String, Object>> viewDatas = new ArrayList<>();
        HashMap<String, Object> map;
        for (int i = 0; i < headers.length; i++) {
            map = new HashMap<String, Object>();
            map.put(DropDownMenu.KEY, DropDownMenu.TYPE_CUSTOM);

            //设置弹出不同的二级菜单
            if (i == 0) {
                map.put(DropDownMenu.VALUE, citydialog());
                map.put(DropDownMenu.SELECT_POSITION, 0);
            } else if (i == 1) {
                map.put(DropDownMenu.VALUE, sectionsort());
                map.put(DropDownMenu.SELECT_POSITION, 0);
            } else {
                map.put(DropDownMenu.VALUE, defaultsort());
                map.put(DropDownMenu.SELECT_POSITION, 0);
            }

            viewDatas.add(map);
        }
        return viewDatas;
    }


    //科室排序及点击事件
    private View sectionsort() {
        View v = View.inflate(InquiryActivity.this, R.layout.inquiry_select_dialog, null);
        ListView lv_registration = (ListView) v.findViewById(R.id.lv_registration);
        data = new ArrayList<>();
        //添加科室到菜单中
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
        //屏蔽listview滑动条
        lv_registration.setVerticalScrollBarEnabled(false);
        secletAdapter = new InquirySecletAdapter(InquiryActivity.this, data);
        lv_registration.setAdapter(secletAdapter);
        secletAdapter.notifyDataSetChanged();

        lv_registration.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dropDownMenu.setTabText(0, "全部地区");//设置tab标签文字
                dropDownMenu.closeMenu();//关闭menu
                dropDownMenu.setTabText(2, "智能排序");
                selectlist.clear();
                if (position == 0) {
                    listinit(list);
                    tag = "全部科室";
                    dropDownMenu.setTabText(1, "全部科室");
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        //在集合中查找所选科室的医生，放入一个新的集合中
                        if ((data.get(position)).equals(list.get(i).getSection())) {
                            selectlist.add(list.get(i));
                            tag = data.get(position);
                        }
                    }
                    if (selectlist.size() == 0) {
                        Toast.makeText(InquiryActivity.this, "没有此科室的医生", Toast.LENGTH_SHORT).show();
                    } else {
                        listinit(selectlist);
                    }
                    dropDownMenu.setTabText(1, data.get(position));
                }

            }
        });

        return v;
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
                tag = "职称排序";

                //筛选所点击的科室医生
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

    //
    private void listinit(List<Inquiry> list) {
        adapter = new InquiryAdapter(InquiryActivity.this, list);
        lv_inquiry.setAdapter(adapter);
    }


    //医生列表数据
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
                        dropDownMenu.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        Type listtype = new TypeToken<LinkedList<Inquiry>>() {
                        }.getType();
                        LinkedList<Inquiry> leclist = gson.fromJson(response, listtype);
                        for (Iterator it = leclist.iterator(); it.hasNext(); ) {
                            Inquiry inquiry = (Inquiry) it.next();
                            list.add(inquiry);
                        }


                        adapter.notifyDataSetChanged();
                        //隐藏加载中图标
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ListenerManager.getInstance().unRegisterListener(this);
    }

    @Override
    public void notifyAllActivity(String str) {

    }
}
