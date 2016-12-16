package com.example.chen.tset.Utils;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.chen.tset.View.activity.LogActivity;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Administrator on 2016/12/16 0016.
 */
public class MyBaseActivity1 extends SwipeBackActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

        if (null != savedInstanceState) {
            //activity由系统加载的时候savedInstanceState不为空
            Intent intent = new Intent(MyBaseActivity1.this, LogActivity.class);
            startActivity(intent);
        }

    }
}
