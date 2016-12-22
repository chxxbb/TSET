package com.example.chen.tset.page.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.entity.Consult;
import com.example.chen.tset.Data.entity.DiseaseBanner;
import com.example.chen.tset.Data.entity.FindAllHot;
import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.entity.Inquiry;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.BannerImageLoader;
import com.example.chen.tset.Utils.db.HomeBannerDao;
import com.example.chen.tset.Utils.db.HomeDoctorDao;
import com.example.chen.tset.Utils.db.HomeEassayDao;
import com.example.chen.tset.Utils.db.HomeFindAllHotDao;
import com.example.chen.tset.Utils.IListener;
import com.example.chen.tset.Utils.ListenerManager;
import com.example.chen.tset.View.activity.ConsultPageActivity;
import com.example.chen.tset.View.activity.DoctorparticularsActivity;
import com.example.chen.tset.View.activity.HealthconditionActivity;
import com.example.chen.tset.View.activity.InquiryActivity;
import com.example.chen.tset.View.activity.LectureoomActivity;
import com.example.chen.tset.View.activity.RegistrationAtivity;
import com.example.chen.tset.View.activity.WebActivity;
import com.example.chen.tset.page.view.AutoVerticalScrollTextView;
import com.example.chen.tset.page.adapter.HomeDoctorRecommendAdapter;
import com.example.chen.tset.page.view.ListViewForScrollView;
import com.example.chen.tset.page.view.RoundCornerImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerClickListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


import java.lang.reflect.Type;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import in.srain.cube.views.ptr.util.PtrLocalDisplay;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/14 0014.
 * 首页
 */
public class HomepageFragment extends Fragment {
    View view;
    private AutoVerticalScrollTextView verticalScrollTV;
    private int number = 0;
    private boolean isRunning = true;
    private Banner diseaseBannerView;
    private ScrollView scrollView;
    private LinearLayout ll_patriarch_lecture_room, ll_home_order_registration, ll_home_health_record, ll_diagnosistreat_manage, ll_home_article_more, ll_home_doctor_more;

    private ListViewForScrollView home_listView;

    HomeDoctorRecommendAdapter adapter;


    private List<String> strings = new ArrayList<>();

    Gson gson = new Gson();

    List<FindAllHot> findAllHotList;

    List<Consult> consultList = new ArrayList<>();

    List<Inquiry> inquiryList = new ArrayList<>();

    //文章标题
    private TextView tv_home_essay_title, tv_home_essay_title1;

    //文章图片
    private RoundCornerImageView tv_home_essay_icon, tv_home_essay_icon1;

    //文章内容
    private TextView tv_home_essay_content, tv_home_essay_content1;

    //文章时间
    private TextView tv_home_essay_time, tv_home_essay_time1;

    private LinearLayout ll_home_essay, ll_home_essay1;

    //收藏数
    private TextView tv_home_essay_collectnumber, tv_home_essay_collectnumber1;

    HomeDoctorDao db;

    HomeFindAllHotDao findAllHotdb;

    HomeEassayDao homeEassaydb;

    //加载中
    RelativeLayout rl_loading;

    Context c;
    HomeBannerDao homeBannerdb;

    List<DiseaseBanner> bannerList = new ArrayList<>();
    List<String> bannerData = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_homepage, null);

        try {
            homeBannerdb = new HomeBannerDao(getContext());
            findView();

            //热点推荐
            findAllHotinit();

            //热门文章
            homeEssayinit();

            bannerinit();

            //医生推荐
            homeDoctorInit();


            init();

        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }

    private void bannerinit() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/FindBannerListByCategoryCode1")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {

                                Type listtype = new TypeToken<LinkedList<DiseaseBanner>>() {
                                }.getType();
                                LinkedList<DiseaseBanner> leclist = gson.fromJson(response, listtype);
                                homeBannerdb.delbanner();

                                bannerList.clear();

                                bannerData.clear();

                                for (Iterator it = leclist.iterator(); it.hasNext(); ) {
                                    DiseaseBanner diseaseBanner = (DiseaseBanner) it.next();
                                    //将banner数据添加到数据库中
                                    homeBannerdb.addbanner(diseaseBanner);

                                    bannerList.add(diseaseBanner);

                                    bannerData.add(diseaseBanner.getCover());
                                }


                                handler.sendEmptyMessage(10);

                            }

                        });

            }
        }).start();
    }


    private void findView() {
        db = new HomeDoctorDao(getContext());

        findAllHotdb = new HomeFindAllHotDao(getContext());

        homeEassaydb = new HomeEassayDao(getContext());

        findAllHotList = new ArrayList<>();
        //疾病库banner
        diseaseBannerView = (Banner) view.findViewById(R.id.bannerView);

        diseaseBannerView.setDelayTime(10000);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);

        //使scrollView显示在头部，重写了listview解决scrollview与listview冲突，但会出现默认显示listview的情况
        scrollView.smoothScrollTo(1, 1);

        tv_home_essay_title = (TextView) view.findViewById(R.id.tv_home_essay_title);

        tv_home_essay_title1 = (TextView) view.findViewById(R.id.tv_home_essay_title1);

        tv_home_essay_icon = (RoundCornerImageView) view.findViewById(R.id.tv_home_essay_icon);

        tv_home_essay_icon1 = (RoundCornerImageView) view.findViewById(R.id.tv_home_essay_icon1);

        tv_home_essay_content = (TextView) view.findViewById(R.id.tv_home_essay_content);

        tv_home_essay_content1 = (TextView) view.findViewById(R.id.tv_home_essay_content1);

        tv_home_essay_time = (TextView) view.findViewById(R.id.tv_home_essay_time);

        tv_home_essay_time1 = (TextView) view.findViewById(R.id.tv_home_essay_time1);

        ll_home_essay = (LinearLayout) view.findViewById(R.id.ll_home_essay);

        ll_home_essay1 = (LinearLayout) view.findViewById(R.id.ll_home_essay1);

        rl_loading = (RelativeLayout) view.findViewById(R.id.rl_loading);

        rl_loading.setVisibility(View.GONE);

        tv_home_essay_collectnumber = (TextView) view.findViewById(R.id.tv_home_essay_collectnumber);
        tv_home_essay_collectnumber1 = (TextView) view.findViewById(R.id.tv_home_essay_collectnumber1);


        //家长讲堂
        ll_patriarch_lecture_room = (LinearLayout) view.findViewById(R.id.ll_patriarch_lecture_room);

        //预约挂号
        ll_home_order_registration = (LinearLayout) view.findViewById(R.id.ll_home_order_registration);

        //健康小鸡
        ll_home_health_record = (LinearLayout) view.findViewById(R.id.ll_home_health_record);

        //诊疗管理
        ll_diagnosistreat_manage = (LinearLayout) view.findViewById(R.id.ll_diagnosistreat_manage);

        //更多文章
        ll_home_article_more = (LinearLayout) view.findViewById(R.id.ll_home_article_more);

        //更多医生
        ll_home_doctor_more = (LinearLayout) view.findViewById(R.id.ll_home_doctor_more);

        home_listView = (ListViewForScrollView) view.findViewById(R.id.home_listView);

        //一个自定义可以上下滚动的textview
        verticalScrollTV = (AutoVerticalScrollTextView) view.findViewById(R.id.textview_auto_roll);


        //隐藏滚动条
        scrollView.setVerticalScrollBarEnabled(false);


        //先使用数据局库数据显示
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (db.findHomeDoctor().size() != 0 && homeEassaydb.findHomeEassay().size() != 0 && homeBannerdb.findHomebanner().size() != 0) {
                    inquiryList.addAll(db.findHomeDoctor());
                    handler.sendEmptyMessage(4);
                    handler.sendEmptyMessage(5);
                    handler.sendEmptyMessage(11);


                } else {

                    handler.sendEmptyMessage(8);

                }

            }
        }).start();

        ll_patriarch_lecture_room.setOnClickListener(listener);
        ll_home_order_registration.setOnClickListener(listener);
        ll_home_health_record.setOnClickListener(listener);
        ll_diagnosistreat_manage.setOnClickListener(listener);
        ll_home_doctor_more.setOnClickListener(listener);
        ll_home_article_more.setOnClickListener(listener);
        ll_home_essay.setOnClickListener(listener);
        ll_home_essay1.setOnClickListener(listener);
        home_listView.setOnItemClickListener(lvlistener);


        //上下文滚动的textView的点击事件
        verticalScrollTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra("url", findAllHotList.get(number % strings.size()).getSite());
                startActivity(intent);
            }
        });

        diseaseBannerView.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                if (bannerList.get(position - 1).getCyclopediaId().equals("") || bannerList.get(position - 1).getCyclopediaId() == null) {
                    Intent intent = new Intent(getContext(), WebActivity.class);
                    intent.putExtra("url", bannerList.get(position - 1).getSite());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), ConsultPageActivity.class);
                    intent.putExtra("information", bannerList.get(position - 1).getCyclopediaId());
                    intent.putExtra("collect", "0");
                    startActivity(intent);
                }

            }
        });
    }

    //使首页一直保持在头部，当fragment处于暂停或显示状态时都会调用此方法nmlgb
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        scrollView.smoothScrollTo(1, 1);
    }


    //热点推荐数据
    private void findAllHotinit() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/FindAllHot")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                handler.sendEmptyMessage(7);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Type listtype = new TypeToken<LinkedList<FindAllHot>>() {
                                }.getType();

                                LinkedList<FindAllHot> leclist = gson.fromJson(response, listtype);

                                //清空热点推荐数据库
                                findAllHotdb.delHomeFindAllHot();

                                strings.clear();

                                findAllHotList.clear();
                                for (Iterator it = leclist.iterator(); it.hasNext(); ) {
                                    FindAllHot findAllHot = (FindAllHot) it.next();
                                    //获取到的数据放入集合中
                                    findAllHotList.add(findAllHot);
                                    //设置滚动条目
                                    strings.add(findAllHot.getTitle());

                                    //将获取到的最新热点推荐数据添加到数据库中
                                    findAllHotdb.addHomeFindAllHot(findAllHot);

                                }
                                handler.sendEmptyMessage(3);
                            }
                        });
            }
        }).start();

    }

    //文章数据
    private void homeEssayinit() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/FindCyclopediaRandomTwo")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Type listtype = new TypeToken<LinkedList<Consult>>() {
                                }.getType();
                                LinkedList<Consult> leclist = gson.fromJson(response, listtype);

                                homeEassaydb.delHomeEassay();

                                consultList.clear();

                                for (Iterator it = leclist.iterator(); it.hasNext(); ) {
                                    Consult consult = (Consult) it.next();
                                    consultList.add(consult);
                                    //将数据添加到数据库中
                                    homeEassaydb.addHomeEassay(consult);

                                }

                                handler.sendEmptyMessage(1);

                            }
                        });

            }
        }).start();

    }

    //推荐医生数据
    private void homeDoctorInit() {
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/FindDoctorRandomFive")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        handler.sendEmptyMessage(9);
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        inquiryList.clear();

                        Type listtype = new TypeToken<LinkedList<Inquiry>>() {
                        }.getType();

                        LinkedList<Inquiry> leclist = gson.fromJson(response, listtype);

                        for (Iterator it = leclist.iterator(); it.hasNext(); ) {

                            Inquiry inquiry = (Inquiry) it.next();

                            inquiryList.add(inquiry);
                        }

                        handler.sendEmptyMessage(2);

                    }
                });
    }

    private void init() {

        //实例化适配器
        adapter = new HomeDoctorRecommendAdapter(getContext(), inquiryList);

        home_listView.setAdapter(adapter);

    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //设置热点推荐滑动数据
                case 0:
                    //设置滚动方式及滚动完成设置textView
                    verticalScrollTV.next();
                    //获取滚动的次数
                    number++;
                    //设置滚动后的text
                    verticalScrollTV.setText(strings.get(number % strings.size()));
                    break;
                //设置文章内容
                case 1:
                    //设置文章标题
                    tv_home_essay_title.setText(consultList.get(0).getTitle());
                    tv_home_essay_title1.setText(consultList.get(1).getTitle());

                    //设置文章内容
                    tv_home_essay_content.setText(consultList.get(0).getContent());
                    tv_home_essay_content1.setText(consultList.get(1).getContent());

                    //设置文章时间
                    tv_home_essay_time.setText(consultList.get(0).getTime());
                    tv_home_essay_time1.setText(consultList.get(1).getTime());

                    //设置文章图片
                    ImageLoader.getInstance().displayImage(consultList.get(0).getIcon(), tv_home_essay_icon);
                    ImageLoader.getInstance().displayImage(consultList.get(1).getIcon(), tv_home_essay_icon1);

                    //设置收藏数
                    tv_home_essay_collectnumber.setText(consultList.get(0).getCollectCount() + "");
                    tv_home_essay_collectnumber1.setText(consultList.get(1).getCollectCount() + "");

                    //隐藏加载中
                    rl_loading.setVisibility(View.GONE);

                    scrollView.smoothScrollTo(1, 1);
                    break;
                //设置医生推荐数据
                case 2:
                    //listView刷新
                    adapter.notifyDataSetChanged();
                    //清空医生推荐数据库，并添加数据到数据库中
                    db.deldotor();
                    for (int i = 0; i < inquiryList.size(); i++) {
                        db.addHomeDoctor(inquiryList.get(i));
                    }
                    break;
                //设置热点推荐滑动时间
                case 3:
                    //设置第一次显示的数据
                    verticalScrollTV.setText(strings.get(0));

                    //设置滚动时间
                    new Thread() {
                        @Override
                        public void run() {
                            while (isRunning) {
                                //每隔5秒通知滚动一次
                                SystemClock.sleep(5000);
                                handler.sendEmptyMessage(0);
                            }
                        }
                    }.start();

                    break;

                case 4:
                    adapter.notifyDataSetChanged();

                    break;

                //设置网络连接失败时文章数据
                case 5:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (homeEassaydb.findHomeEassay().size() != 0) {

                                consultList.addAll(homeEassaydb.findHomeEassay());
                            }

                            handler.sendEmptyMessage(1);


//                            ???????????  fuck


                        }
                    }).start();
                    break;

                case 7:

                    //默认使用数据库数据
                    List<FindAllHot> findList;
                    findList = findAllHotdb.findHomeFindAllHot();
                    findAllHotList.addAll(findList);

                    if (findList.size() != 0) {

                        for (int i = 0; i < findList.size(); i++) {

                            strings.add(findList.get(i).getTitle());
                        }
                        handler.sendEmptyMessage(3);

                    }
                    break;

                case 8:

                    rl_loading.setVisibility(View.VISIBLE);

                    break;

                case 9:

                    Toast.makeText(getContext(), "网络连接失败", Toast.LENGTH_SHORT).show();

                    rl_loading.setVisibility(View.GONE);

                    break;

                case 10:
                    diseaseBannerView.setImageLoader(new BannerImageLoader());
                    //设置图片集合
                    diseaseBannerView.setImages(bannerData);
                    //banner设置方法全部调用完毕时最后调用
                    diseaseBannerView.start();
                    break;

                case 11:
                    bannerList.clear();

                    bannerData.clear();

                    bannerList = homeBannerdb.findHomebanner();

                    for (int i = 0; i < bannerList.size(); i++) {
                        bannerData.add(bannerList.get(i).getCover());
                    }

                    handler.sendEmptyMessage(10);
                    break;

            }

        }
    };

    //推荐医生点击事件
    private AdapterView.OnItemClickListener lvlistener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getContext(), DoctorparticularsActivity.class);
            intent.putExtra("doctot_id", inquiryList.get(position).getId());
            startActivity(intent);
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        //关闭滚动
        isRunning = false;
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //跳转至讲堂页面
                case R.id.ll_patriarch_lecture_room:
                    Intent intent = new Intent(getContext(), LectureoomActivity.class);
                    startActivity(intent);
                    break;
                //跳转至一键挂号页面
                case R.id.ll_home_order_registration:
                    Intent intent1 = new Intent(getContext(), RegistrationAtivity.class);
                    startActivity(intent1);
                    break;
                //跳转健康状况页面
                case R.id.ll_home_health_record:
                    Intent intent2 = new Intent(getContext(), HealthconditionActivity.class);
                    startActivity(intent2);
                    break;
                //跳转至名医库
                case R.id.ll_home_doctor_more:
                    Intent intent3 = new Intent(getContext(), InquiryActivity.class);
                    startActivity(intent3);
                    break;
                //诊疗页面
                case R.id.ll_diagnosistreat_manage:
                    //发送广播通知显示诊疗页面
                    ListenerManager.getInstance().sendBroadCast("显示诊疗页面");
                    break;
                //资讯页面
                case R.id.ll_home_article_more:
                    //发送广播通知显示资讯页面
                    ListenerManager.getInstance().sendBroadCast("显示资讯页面");

                    break;

                //跳转至资讯详情页面
                case R.id.ll_home_essay1:
                    Intent intent4 = new Intent(getContext(), ConsultPageActivity.class);
                    intent4.putExtra("information", consultList.get(1).getId() + "");
                    intent4.putExtra("collect", "0");
                    startActivity(intent4);
                    break;

                //跳转到资讯详情页面
                case R.id.ll_home_essay:
                    Intent intent5 = new Intent(getContext(), ConsultPageActivity.class);
                    intent5.putExtra("information", consultList.get(0).getId() + "");
                    intent5.putExtra("collect", "0");
                    startActivity(intent5);
                    break;
            }
        }
    };


    @Override
    public void onDetach() {
        super.onDetach();

    }


}
