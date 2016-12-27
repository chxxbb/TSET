package com.example.chen.tset.Utils;

import android.app.Application;
import android.util.Log;

import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.qiniu.android.common.Zone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by Chen on 2016/6/8.
 */
public class ContextUtil extends Application {
    private static ContextUtil instance;

    public static ContextUtil getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.color.a) //加载中图片
                .showImageOnFail(R.drawable.defeated_picture) //加载失败图片
                .showImageForEmptyUri(R.drawable.defeated_picture)  //图片地址失效图片
                .cacheInMemory(true) // 打开内存缓存
                .cacheOnDisk(true) // 打开硬盘缓存
                .resetViewBeforeLoading(true) // 在ImageView加载前清除它上面的图片
                .build();
        // ImageLoader配置
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheSize(5 * 1024 * 1024) // 内存缓存
                .defaultDisplayImageOptions(options) // 设置显示选项
                .threadPoolSize(3)
                .build();

        // 初始化ImageLoad
        ImageLoader.getInstance().init(config);

        JMessageClient.init(getApplicationContext());
        JPushInterface.setDebugMode(true);

    }


}
