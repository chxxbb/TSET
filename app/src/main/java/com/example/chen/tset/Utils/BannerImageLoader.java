package com.example.chen.tset.Utils;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.loader.ImageLoader;


/**
 * Created by Administrator on 2016/12/8 0007.
 */
public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage((String) path, imageView);
    }


}
