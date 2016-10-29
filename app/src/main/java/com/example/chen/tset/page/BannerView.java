package com.example.chen.tset.page;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.chen.tset.Data.ConsultingRemindState;
import com.example.chen.tset.Data.DiseaseBanner;
import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.R;
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
 *
 */
public class BannerView extends LinearLayout {
    private ViewPager adViewPager;
    private List<ImageView> bannerViewList = new ArrayList<ImageView>();
    private Timer bannerTimer;
    private Handler handler;
    private Handler handler1;

    private BannerTimerTask bannerTimerTask;
    private ImageView iv_lead_spot1, iv_lead_spot2, iv_lead_spot3, iv_lead_spot4;

    List<String> pics;

    List<String> data;


    public BannerView(Context context) {
        super(context);

    }

    public BannerView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        data = new ArrayList<>();
        init();
        handler1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                initView(context);
            }
        };


        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.banner_view, this);


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
                        .url(Http_data.http_data + "/FindBannerListByCategoryCode2")
                        .build()
                        .execute(new StringCallback() {

                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.e("banner失败", "失败");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.e("banner返回", response);

                                Type listtype = new TypeToken<LinkedList<DiseaseBanner>>() {
                                }.getType();
                                LinkedList<DiseaseBanner> leclist = gson.fromJson(response, listtype);
                                for (Iterator it = leclist.iterator(); it.hasNext(); ) {
                                    DiseaseBanner diseaseBanner = (DiseaseBanner) it.next();

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


        BannerViewAdapter adapter = new BannerViewAdapter(context, bannerViewList,data);
        adViewPager.setAdapter(adapter);
        adViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //设置每一页的点
                if (position == 3) {
                    iv_lead_spot1.setImageResource(R.drawable.lead_slide_sign1);
                    iv_lead_spot2.setImageResource(R.drawable.lead_slide_sign1);
                    iv_lead_spot3.setImageResource(R.drawable.lead_slide_sign1);
                    iv_lead_spot4.setImageResource(R.drawable.lead_slide_sign);
                } else if (position == 0) {
                    iv_lead_spot1.setImageResource(R.drawable.lead_slide_sign);
                    iv_lead_spot2.setImageResource(R.drawable.lead_slide_sign1);
                    iv_lead_spot4.setImageResource(R.drawable.lead_slide_sign1);
                    iv_lead_spot3.setImageResource(R.drawable.lead_slide_sign1);
                } else if (position == 1) {
                    iv_lead_spot1.setImageResource(R.drawable.lead_slide_sign1);
                    iv_lead_spot2.setImageResource(R.drawable.lead_slide_sign);
                    iv_lead_spot4.setImageResource(R.drawable.lead_slide_sign1);
                    iv_lead_spot3.setImageResource(R.drawable.lead_slide_sign1);
                } else if (position == 2) {
                    iv_lead_spot1.setImageResource(R.drawable.lead_slide_sign1);
                    iv_lead_spot2.setImageResource(R.drawable.lead_slide_sign1);
                    iv_lead_spot4.setImageResource(R.drawable.lead_slide_sign1);
                    iv_lead_spot3.setImageResource(R.drawable.lead_slide_sign);
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
