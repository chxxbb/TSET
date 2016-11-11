package com.example.chen.tset.page.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chen.tset.Data.entity.DiseaseBanner;
import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.db.HomeBannerDao;
import com.example.chen.tset.page.adapter.BannerViewAdapter;
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
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/11/8 0008.
 */
public class LectureroomAmongBanner extends LinearLayout {
    private ViewPager adViewPager;
    private List<ImageView> bannerViewList = new ArrayList<ImageView>();
    private Timer bannerTimer;
    private Handler handler;
    private Handler handler1;

    private BannerTimerTask bannerTimerTask;
    private ImageView iv_lead_spot1, iv_lead_spot2, iv_lead_spot3, iv_lead_spot4;

    private TextView tv_lectureroom_among_bannertext, tv_lecurre_banner_title;

    List<String> pics;

    List<String> data;

    List<String> titleList;

    List<String> list = new ArrayList<>();

    HomeBannerDao db;


    public LectureroomAmongBanner(Context context) {
        super(context);

    }

    public LectureroomAmongBanner(final Context context, AttributeSet attrs) {
        super(context, attrs);
        db = new HomeBannerDao(context);
        data = new ArrayList<>();
        titleList = new ArrayList<>();
        init();
        handler1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        //如果无数据则使用数据库数据显示
                        if (db.findHomebanner().size() != 0) {

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    List<DiseaseBanner> list = db.findHomebanner();
                                    for (int i = 0; i < list.size(); i++) {
                                        pics.add(list.get(i).getCover());
                                        data.add(list.get(i).getSite());
                                    }
                                    handler1.sendEmptyMessage(1);
                                }
                            }).start();

                        }

                        break;
                    case 1:
                        initView(context);
                        break;
                }
            }
        };


        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.lectureroom_among_banner_view, this);


        handler = new Handler() {
            public void handleMessage(Message msg) {
                adViewPager.setCurrentItem(msg.what);
                super.handleMessage(msg);

            }
        };
        bannerTimer = new Timer();


    }

    private void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pics = new ArrayList<>();
                final Gson gson = new Gson();
                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/FindBannerListByCategoryCode1")
                        .build()
                        .execute(new StringCallback() {

                            @Override
                            public void onError(Call call, Exception e, int id) {
                                handler1.sendEmptyMessage(0);
                            }

                            @Override
                            public void onResponse(String response, int id) {

                                Type listtype = new TypeToken<LinkedList<DiseaseBanner>>() {
                                }.getType();
                                LinkedList<DiseaseBanner> leclist = gson.fromJson(response, listtype);
                                db.delbanner();
                                for (Iterator it = leclist.iterator(); it.hasNext(); ) {
                                    DiseaseBanner diseaseBanner = (DiseaseBanner) it.next();
                                    //将banner数据添加到数据库中
                                    db.addbanner(diseaseBanner);

                                    pics.add(diseaseBanner.getCover());

                                    data.add(diseaseBanner.getSite());
                                }
                                handler1.sendEmptyMessage(1);

                            }


                        });

            }
        }).start();


    }


    private void initView(final Context context) {

        adViewPager = (ViewPager) this.findViewById(R.id.viewPager1);
        iv_lead_spot1 = (ImageView) this.findViewById(R.id.iv_lead_spot1);
        iv_lead_spot2 = (ImageView) this.findViewById(R.id.iv_lead_spot2);
        iv_lead_spot3 = (ImageView) this.findViewById(R.id.iv_lead_spot3);
        iv_lead_spot4 = (ImageView) this.findViewById(R.id.iv_lead_spot4);
        tv_lectureroom_among_bannertext = (TextView) this.findViewById(R.id.tv_lectureroom_among_bannertext);
        tv_lecurre_banner_title = (TextView) this.findViewById(R.id.tv_lecurre_banner_title);

        tv_lectureroom_among_bannertext.setText("不再有蛋疼1");
        tv_lecurre_banner_title.setText("李狗蛋1号 / 25'61'");

        list.add("李狗蛋1号 / 25'61'");
        list.add("李狗蛋2号 / 25'62'");
        list.add("李狗蛋3号 / 25'63'");
        list.add("李狗蛋4号 / 25'64'");

        titleList.add("不再有蛋疼1");
        titleList.add("不再有蛋疼2");
        titleList.add("不再有蛋疼3");
        titleList.add("不再有蛋疼4");


        TextPaint tp = tv_lectureroom_among_bannertext.getPaint();
        tp.setFakeBoldText(true);


        ImageView imageView;


        //初始化数据,改变图片

        for (int i = 0; i < pics.size(); i++) {
            imageView = new ImageView(context);
            //加载图片
            ImageLoader.getInstance().displayImage(pics.get(i), imageView);
            //图片铺满
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            bannerViewList.add(imageView);

        }


        BannerViewAdapter adapter = new BannerViewAdapter(context, bannerViewList, data);
        adViewPager.setAdapter(adapter);
        adViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //设置每一页的点
                if (position == 3) {
                    iv_lead_spot1.setImageResource(R.drawable.lectureroom_among_banner_show);
                    iv_lead_spot2.setImageResource(R.drawable.lectureroom_among_banner_show);
                    iv_lead_spot3.setImageResource(R.drawable.lectureroom_among_banner_show);
                    iv_lead_spot4.setImageResource(R.drawable.lectureroom_among_banner_noshow);
                    tv_lectureroom_among_bannertext.setText(titleList.get(3));
                    tv_lecurre_banner_title.setText(list.get(3));

                } else if (position == 0) {
                    iv_lead_spot1.setImageResource(R.drawable.lectureroom_among_banner_noshow);
                    iv_lead_spot2.setImageResource(R.drawable.lectureroom_among_banner_show);
                    iv_lead_spot4.setImageResource(R.drawable.lectureroom_among_banner_show);
                    iv_lead_spot3.setImageResource(R.drawable.lectureroom_among_banner_show);
                    tv_lectureroom_among_bannertext.setText(titleList.get(0));
                    tv_lecurre_banner_title.setText(list.get(0));

                } else if (position == 1) {
                    iv_lead_spot1.setImageResource(R.drawable.lectureroom_among_banner_show);
                    iv_lead_spot2.setImageResource(R.drawable.lectureroom_among_banner_noshow);
                    iv_lead_spot4.setImageResource(R.drawable.lectureroom_among_banner_show);
                    iv_lead_spot3.setImageResource(R.drawable.lectureroom_among_banner_show);
                    tv_lectureroom_among_bannertext.setText(titleList.get(1));
                    tv_lecurre_banner_title.setText(list.get(1));

                } else if (position == 2) {
                    iv_lead_spot1.setImageResource(R.drawable.lectureroom_among_banner_show);
                    iv_lead_spot2.setImageResource(R.drawable.lectureroom_among_banner_show);
                    iv_lead_spot4.setImageResource(R.drawable.lectureroom_among_banner_show);
                    iv_lead_spot3.setImageResource(R.drawable.lectureroom_among_banner_noshow);
                    tv_lectureroom_among_bannertext.setText(titleList.get(2));
                    tv_lecurre_banner_title.setText(list.get(2));

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    //启动banner自动轮播
    public void bannerStartPlay() {
        if (bannerTimer != null) {
            if (bannerTimerTask != null)
                bannerTimerTask.cancel();
            bannerTimerTask = new BannerTimerTask();
            bannerTimer.schedule(bannerTimerTask, 3000, 3000);//3秒后执行，每隔3秒执行一次
        }
    }

    //暂停banner自动轮播
    public void bannerStopPlay() {
        if (bannerTimerTask != null)
            bannerTimerTask.cancel();
    }

    class BannerTimerTask extends TimerTask {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            Message msg = new Message();
            if (bannerViewList.size() <= 1)
                return;
            int currentIndex = adViewPager.getCurrentItem();
            if (currentIndex == bannerViewList.size() - 1)
                msg.what = 0;
            else
                msg.what = currentIndex + 1;

            handler.sendMessage(msg);
        }

    }
}
