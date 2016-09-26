package com.example.chen.tset.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Chen on 2016/9/9.
 */

/*  广播接收器 ，响应发送广播的操作  *//* 接受广播 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    /* 覆写该方法，对广播事件执行响应的动作  */
    @Override
    public void onReceive(Context context, Intent intent) {

        /* 获取Intent对象中的数据 */
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            String str = bundle.getString(JPushInterface.EXTRA_MESSAGE);

            System.out.println("收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            Log.e("11", bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了通知");
            // 在这里可以做些统计，或者做些其他工作
            Toast.makeText(context, "wangwangwang!", Toast.LENGTH_LONG).show();
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            System.out.println("用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
            Toast.makeText(context, "fuck", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Unhandled intent - " + intent.getAction(), Toast.LENGTH_LONG).show();
        }
    }
}
