package com.example.chen.tset.page;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.chen.tset.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 *
 */
public class BannerView extends LinearLayout {
    private ViewPager adViewPager;
    private List<ImageView> bannerViewList = new ArrayList<ImageView>();
    private Timer bannerTimer;
    private Handler handler;
    private BannerTimerTask bannerTimerTask;
    private ImageView iv_lead_spot1, iv_lead_spot2, iv_lead_spot3, iv_lead_spot4;

    public BannerView(Context context) {
        super(context);

    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.banner_view, this);
        initView(context);
        handler = new Handler() {
            public void handleMessage(Message msg) {
                adViewPager.setCurrentItem(msg.what);
                super.handleMessage(msg);

            }
        };
        bannerTimer = new Timer();
    }

    private void initView(final Context context) {
        adViewPager = (ViewPager) this.findViewById(R.id.viewPager1);
        iv_lead_spot1 = (ImageView) this.findViewById(R.id.iv_lead_spot1);
        iv_lead_spot2 = (ImageView) this.findViewById(R.id.iv_lead_spot2);
        iv_lead_spot3 = (ImageView) this.findViewById(R.id.iv_lead_spot3);
        iv_lead_spot4 = (ImageView) this.findViewById(R.id.iv_lead_spot4);




        ImageView imageView;
        //初始化数据,改变图片
        int[] pics = new int[]{
                R.color.pink,
                R.color.colorPrimary,
                R.color.fontgray,
                R.color.backgroundblue
        };

        for (int i = 0; i < pics.length; i++) {
            imageView = new ImageView(context);
            imageView.setImageResource(pics[i]);
            bannerViewList.add(imageView);
        }



        BannerViewAdapter adapter = new BannerViewAdapter(context, bannerViewList);
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
